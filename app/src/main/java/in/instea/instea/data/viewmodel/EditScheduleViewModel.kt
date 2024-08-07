package `in`.instea.instea.data.viewmodel

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
//            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val subjects = scheduleRepository.getAllSubjects()
                if (scheduleId != 0) {
                    val scheduleDetail = scheduleRepository.getScheduleById(scheduleId)
                    _uiState.update {
                        it.copy(
                            selectedSubject = scheduleDetail.subject,
//                            selectedDay = day,
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
//                    selectedSubject = day,
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
        /*_uiState.value = _uiState.value.copy(
            selectedSubject = subject,
            subjectError = if (subject.length > 18) "Length exceeded" else null
        )*/
        _uiState.update {
            it.copy(
                selectedSubject = subject,
                subjectError = if (subject.length > 18) "Length exceeded" else null
            )
        }
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
        _uiState.value.isLoading = true
        val uiStateValue = _uiState.value
        val startTime = uiStateValue.startTime
        val endTime = uiStateValue.endTime
        val day = uiStateValue.selectedDay
        if (_uiState.value.selectedSubject.isBlank()) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    subjectError = "Subject can't be black!"
                )
            }
            return false
        }
        val conflictError = checkTimeConflict(startTime, endTime, day, scheduleId)
        if (conflictError.isNullOrBlank()) {
            // no conflict
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
            _uiState.value = _uiState.value.copy(
                errorMessage = "Time Conflict with $conflictError on ${_uiState.value.selectedDay}",
                isLoading = false
            )
            showSnackBar()
            return false
        }
    }

    suspend fun onDeleteClick() {
        viewModelScope.launch {
            _uiState.value.isLoading = true
            scheduleRepository.deleteScheduleById(id = scheduleId)
        }
    }

    private fun showSnackBar() {
        _uiState.value.showSnackBar = !_uiState.value.showSnackBar
    }

    private suspend fun checkTimeConflict(
        startTime: LocalTime,
        endTime: LocalTime,
        day: String,
        currentScheduleId: Int
    ): String? {
        val daySchedulesList = scheduleRepository.getAllScheduleByDay(day)
        if (startTime >= endTime) return "Start time cannot be greater than or equal to end time"

        val conflictingSchedule = daySchedulesList.firstOrNull { schedule ->
            val sT = startTime.plusMinutes(1)
            val eT = endTime.minusMinutes(1)
//            Log.d("ID", schedule.scheduleId.toString())
            // Exclude the current schedule
            schedule.scheduleId != currentScheduleId &&
                    ((sT in schedule.startTime..schedule.endTime) ||
                            (eT in schedule.startTime..schedule.endTime) ||
                            (sT <= schedule.startTime && endTime >= schedule.endTime))
        }
        return conflictingSchedule?.subject
    }

    fun onAddClick() {
//        _uiState.value.readOnly = false
        _uiState.update { it.copy(readOnly = !_uiState.value.readOnly) }
    }
}