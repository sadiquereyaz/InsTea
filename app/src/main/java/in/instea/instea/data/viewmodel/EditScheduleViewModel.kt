package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.schedule.EditScheduleDestination
import `in`.instea.instea.screens.schedule.EditScheduleUiState
import `in`.instea.instea.screens.schedule.EditScreenType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

class EditScheduleViewModel(
    savedStateHandle: SavedStateHandle,
    val scheduleRepository: ScheduleRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditScheduleUiState())
    val uiState: StateFlow<EditScheduleUiState> = _uiState

    //    private val currentSchedule: ScheduleModel = checkNotNull(savedStateHandle["scheduleModel"])
    private val day: String = checkNotNull(savedStateHandle[EditScheduleDestination.DAY_ARG])
    private val scheduleId: Int = checkNotNull(savedStateHandle[EditScheduleDestination.ID_ARG])

    init {
        fetchInitialInfo()
    }

    private fun fetchInitialInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val subjects = scheduleRepository.getAllSubjects()
                if (scheduleId != 0) {
                    val scheduleDetail = scheduleRepository.getScheduleById(scheduleId)
                    _uiState.update {
                        it.copy(
                            selectedSubject = scheduleDetail.subject,
                            startTime = scheduleDetail.startTime,
                            endTime = scheduleDetail.endTime,
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            editScreenType = EditScreenType.AddScheduleScreen
                        )
                    }
                }
                _uiState.value = _uiState.value.copy(
                    subjectList = subjects,
                    selectedDay = day,
                    scheduleId = scheduleId,
                    isLoading = false
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun onSubjectSelected(subject: String) {
        _uiState.value = _uiState.value.copy(selectedSubject = subject)
    }

    fun onDaySelected(day: String) {
        _uiState.value = _uiState.value.copy(selectedDay = day)
    }

    fun setStartTime(startTime: LocalTime) {
        _uiState.update { currentState -> currentState.copy(startTime = startTime) }
    }

    fun setEndTime(endTime: LocalTime) {
        _uiState.value = _uiState.value.copy(endTime = endTime)
    }

    suspend fun saveSchedule(): Boolean {
        val uiStateValue = _uiState.value
        val startTime = uiStateValue.startTime
        val endTime = uiStateValue.endTime
        val day = uiStateValue.selectedDay
        val isConflict = checkTimeConflict(startTime, endTime, day, scheduleId)
//        Log.d("CONFLICT", isConflict.toString())
        if (!isConflict) {
//            Log.d("CONFLICT_SAVING", "no conflict")
            scheduleRepository.upsertSchedule(
                ScheduleModel(
                    subject = uiStateValue.selectedSubject,
                    scheduleId = scheduleId,
                    startTime = startTime,
                    endTime = endTime,
                    day = day
                )
            )
            _uiState.value = _uiState.value.copy(errorMessage = null)
            return true
        } else {
//            Log.d("CONFLICT_elsebranch", "true")
            _uiState.value = _uiState.value.copy(errorMessage = "Time conflict with another class")
        }
        return false
    }

    suspend fun onDeleteClick() = scheduleRepository.deleteScheduleById(id = scheduleId)

    private suspend fun checkTimeConflict(
        startTime: LocalTime,
        endTime: LocalTime,
        day: String,
        currentScheduleId: Int
    ): Boolean {
        val daySchedulesList = scheduleRepository.getAllScheduleByDay(day)
        if (startTime >= endTime) return true

        return daySchedulesList.any { schedule ->
            val sT = startTime.plusMinutes(1)
            val eT = endTime.minusMinutes(1)
            Log.d("ID", schedule.scheduleId.toString())
            // Exclude the current schedule
            schedule.scheduleId != currentScheduleId &&
                    ((sT in schedule.startTime..schedule.endTime) ||
                            (eT in schedule.startTime..schedule.endTime) ||
                            (sT <= schedule.startTime && endTime >= schedule.endTime))
        }
    }
}