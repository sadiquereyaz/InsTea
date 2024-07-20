package `in`.instea.instea.data.viewmodel

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

    fun setStartTime(time: LocalTime) {
        _uiState.update { currentState -> currentState.copy(startTime = time) }
    }

    fun setEndTime(endTime: LocalTime) {
        _uiState.value = _uiState.value.copy(endTime = endTime)
    }

    suspend fun saveSchedule() {
        val uiStateValue = _uiState.value
        val startTime = uiStateValue.startTime
        val endTime = uiStateValue.endTime
        val day = uiStateValue.selectedDay
        val isConflict = checkTimeConflict(startTime, endTime, day)
         if (!isConflict) {
             scheduleRepository.upsertSchedule(
                 subject = uiStateValue.selectedSubject,
                 scheduleId = 0,
                 startTime = startTime,
                 endTime = endTime,
                 day = day
             )
             _uiState.value = _uiState.value.copy(errorMessage = null)
         } else {
             _uiState.value = _uiState.value.copy(errorMessage = "Time conflict with another class")
         }
    }

    private suspend fun checkTimeConflict(
        startTime: LocalTime,
        endTime: LocalTime,
        day: String
    ): Boolean {
        val daySchedulesList = scheduleRepository.getAllScheduleByDay(day)
        return daySchedulesList.any {
            (it.startTime >= startTime && it.endTime >= endTime)
        }
    }
}