package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.schedule.EditScheduleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

class EditScheduleViewModel(
    val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditScheduleUiState())
    val uiState: StateFlow<EditScheduleUiState> = _uiState

    init {
        fetchSubjects()
    }

    private fun fetchSubjects() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val subjects = scheduleRepository.getAllSubjects()
                _uiState.value = _uiState.value.copy(subjectList = subjects, isLoading = false)
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

    suspend fun saveSchedule(scheduleId: Int = 0) {
        val uiStateValue = _uiState.value
        val startTime = uiStateValue.startTime
        val endTime = uiStateValue.endTime
        val day = uiStateValue.selectedDay
        val isConflict = checkTimeConflict(startTime, endTime, day)
        Log.d("CONFLICT", isConflict.toString())
        if (!isConflict) {
            Log.d("CONFLICT_SAVING", "no conflict")
            scheduleRepository.upsertSchedule(
                subject = uiStateValue.selectedSubject,
                scheduleId = scheduleId,
                startTime = startTime,
                endTime = endTime,
                day = day
            )
            _uiState.value = _uiState.value.copy(errorMessage = null)
        } else {
            Log.d("CONFLICT_elsebranch", "true")
            _uiState.value = _uiState.value.copy(errorMessage = "Time conflict with another class")
        }
    }

    private suspend fun checkTimeConflict(
        startTime: LocalTime,
        endTime: LocalTime,
        day: String
    ): Boolean {
        val daySchedulesList = scheduleRepository.getAllScheduleByDay(day)
        if (startTime >= endTime) return true
        return daySchedulesList.any {
            Log.d("conflict check", it.toString())
            val sT = startTime.plusMinutes(1)
            val eT = endTime.minusMinutes(1)
            (sT in it.startTime..it.endTime) ||
                    (eT in it.startTime..it.endTime) ||
                    (sT <= it.startTime && endTime >= it.endTime)
        }
    }
}