package `in`.instea.instea.presentation.schedule

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.local.entity.ScheduleModel
import `in`.instea.instea.data.local.entity.SubjectModel
import `in`.instea.instea.domain.repo.ScheduleRepository
import `in`.instea.instea.domain.repo.TaskReminderRepository
import `in`.instea.instea.utility.NotificationConstant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

class EditScheduleViewModel(
    savedStateHandle: SavedStateHandle,
    val scheduleRepository: ScheduleRepository,
    private val workManagerTaskRepository: TaskReminderRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditScheduleUiState())
    val uiState: StateFlow<EditScheduleUiState> = _uiState

    //    private val currentSchedule: ScheduleModel = checkNotNull(savedStateHandle["scheduleModel"])
    private val argDay: String = checkNotNull(savedStateHandle[EditScheduleDestination.DAY_ARG])
    private val argSchId: Int = checkNotNull(savedStateHandle[EditScheduleDestination.ID_ARG])

    init {
        fetchInitialInfo()
    }

    private fun fetchInitialInfo() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                if (argSchId > -1) {
                    // editable schedule
                    val scheduleDetail = scheduleRepository.getScheduleById(argSchId)
                    _uiState.update {
                        it.copy(
                            selectedSubject = scheduleDetail.subject,
                            scheduleId = argSchId,
                            subjectId = scheduleDetail.subjectId,
                            subject = scheduleDetail.subject,
//                            selectedDay = day,
                            startTime = scheduleDetail.startTime,
                            endTime = scheduleDetail.endTime,
                        )
                    }
                } else {
                    // new schedule
                    _uiState.update {
                        it.copy(
                            editScreenType = EditScreenType.AddScheduleScreen
                        )
                    }
                }
                // default requirement
                scheduleRepository.getAllSubjectFlow().collect { subjects ->
                    _uiState.value = _uiState.value.copy(
                        subjectModelList = subjects,        //for dropdown
                        selectedDay = argDay,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    fun onSubjectSelected(sub: SubjectModel) {
        _uiState.update { it.copy(selectedSubject = sub.subject, subjectId = sub.subjectId) }
    }

    suspend fun addSubject(subject: String) {
        _uiState.update {
            it.copy(
                selectedSubject = subject,
                subjectId = scheduleRepository.upsertSubject(subject).toInt()
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
                    subjectError = "Subject can't be blank!"
                )
            }
            return false
        }
        val conflictError = checkTimeConflict(startTime, endTime, day, uiStateValue.scheduleId)
        if (conflictError.isNullOrBlank()) {
            // no conflict
            scheduleRepository.upsertSchedule(
                ScheduleModel(
                    scheduleId = uiStateValue.scheduleId,
                    subjectId = uiStateValue.subjectId,
                    subject = uiStateValue.selectedSubject,
                    startTime = startTime,
                    endTime = endTime,
                    day = day
                )
            )
            _uiState.value = _uiState.value.copy(errorMessage = null)
            return true
        } else {
            _uiState.value = _uiState.value.copy(
//                errorMessage = "Time Conflict with $conflictError on ${_uiState.value.selectedDay}",
                errorMessage = conflictError,
                isLoading = false
            )
            showSnackBar()
            return false
        }
    }

    suspend fun onDeleteClick() {
        viewModelScope.launch {
            _uiState.value.isLoading = true
            scheduleRepository.deleteScheduleById(
                uiState.value.scheduleId
            )
            cancelReminder()
        }
    }

    private fun cancelReminder(
    ) {
        val reminderKey = NotificationConstant.getDailyClassReminderKey(
            scheduleId = uiState.value.scheduleId
        )
        workManagerTaskRepository.cancelScheduledWork(reminderKey)
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
        if (startTime >= endTime) return "Start time cannot be greater than or equal to end time"
        val daySchedulesList = scheduleRepository.getAllScheduleByDay(day)
        val conflictingSchedule = daySchedulesList.firstOrNull { schedule ->
            val sT = startTime.plusMinutes(1)
            val eT = endTime.minusMinutes(1)
            // Exclude the current schedule
            schedule.scheduleId != currentScheduleId &&
                    (
                            (sT in schedule.startTime..schedule.endTime) ||
                                    (eT in schedule.startTime..schedule.endTime) ||
                                    (sT <= schedule.startTime && endTime >= schedule.endTime)
                            )
        }
//        return conflictingSchedule?.subject
        return if (conflictingSchedule == null) null else "Time Conflict with ${conflictingSchedule.subject} on $day"
    }
}