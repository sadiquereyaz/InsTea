package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.DayDateModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.schedule.ScheduleUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
    val calendar = Calendar.getInstance()


    private val _dayDateList: List<DayDateModel> = generateDayDateList()
    private val _currentMonth: String = Calendar.getInstance().getDisplayName(
        Calendar.MONTH, Calendar.LONG, Locale.getDefault()
    ) ?: ""

    private fun generateDayDateList(): List<DayDateModel> {
        val calendar = Calendar.getInstance()
        val dayDateList = mutableListOf<DayDateModel>()
        // Start from 15 days before the current day
        calendar.add(Calendar.DAY_OF_YEAR, -15)     //current date will be at 16th position or 15th index

        repeat(29) {
            val day =
                calendar.getDisplayName(
                    Calendar.DAY_OF_WEEK,
                    Calendar.SHORT,
                    Locale.getDefault()
                )
                    ?: ""
            val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
            dayDateList.add(DayDateModel(day = day, date = date))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return dayDateList
    }

    private val scheduleListFlow: Flow<List<ScheduleModel>> = scheduleRepository.getClassList()

    val scheduleUiState: StateFlow<ScheduleUiState> = scheduleListFlow
        .map { scheduleList ->
            ScheduleUiState(
                dayDateList = _dayDateList,
                classList = scheduleList,
                month = _currentMonth
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ScheduleUiState(dayDateList = _dayDateList, month = _currentMonth)
        )

    suspend fun updateAttendance(scheduleId: Int) {
        scheduleRepository.updateAttendance(scheduleId)
    }
    suspend fun updateTask(scheduleId: Int, task: String) {
        scheduleRepository.updateTask(scheduleId=scheduleId, task=task)
    }

    fun selectDateIndex(index: Int) {
//        _scheduleUiState.value = _scheduleUiState.value.copy(
//            selectedDateIndex = index,
//            classList = DataSource.classSubjectData[dayDateList[index].day] ?: emptyList()
//        )
        /*when (dayDateList[index].day) {
            "Mon" -> {
                _scheduleUiState.value = _scheduleUiState.value.copy(
                    subjectList = DataSource.classSubjectData[dayDateList[index].day] ?: emptyList()
                )
            }

            "Tue" -> {
                _scheduleUiState.value = _scheduleUiState.value.copy(
                    subjectList = classSubjectData[dayDateList[index].day] ?: emptyList()
                )
            }

            "Wed" -> {
                _scheduleUiState.value = _scheduleUiState.value.copy(
                    subjectList = classSubjectData[dayDateList[index].day] ?: emptyList()
                )
            }

            "Thu" -> {
                _scheduleUiState.value = _scheduleUiState.value.copy(
                    subjectList = classSubjectData[dayDateList[index].day] ?: emptyList()
                )
            }

            "Fri" -> {
                _scheduleUiState.value = _scheduleUiState.value.copy(
                    subjectList = classSubjectData[dayDateList[index].day] ?: emptyList()
                )
            }

            "Sat" -> {
                _scheduleUiState.value = _scheduleUiState.value.copy(
                    subjectList = classSubjectData[dayDateList[index].day] ?: emptyList()
                )
            }

            "Sun" -> {
                _scheduleUiState.value = _scheduleUiState.value.copy(
                    subjectList = classSubjectData[dayDateList[index].day] ?: emptyList()
                )
            }

        }*/
    }

    fun updateAttendanceType(attendanceType: AttendanceType) {
//        val updatedSubjectList = _scheduleUiState.value.classList.toMutableList()
//        val subjectIndex = updatedSubjectList.indexOf(subject)
//        if (subjectIndex != -1) {
//            updatedSubjectList[subjectIndex] = subject.copy(attendanceType = attendanceType)
//            _scheduleUiState.value = _scheduleUiState.value.copy(classList = updatedSubjectList)
//        }
//        _scheduleUiState.value = _scheduleUiState.value.copy(
//        )
    }

    fun modifySubjectInRepeatReminderList(subjectName: String, repeat: Boolean) {
//        if (repeat) {
//            reminderRepeatSubjectList.add(subjectName)
////            _scheduleUiState.value = _scheduleUiState.value.copy(subjectList[index].)
//        } else {
//            reminderRepeatSubjectList.remove(subjectName)
//        }
    }

//    fun updateAttendance(index: Int) {
//        _scheduleUiState.update { currentState ->
//            val updatedSubjectList = currentState.classList.toMutableList()
//            updatedSubjectList[index] = updatedSubjectList[index].copy(attendanceType = AttendanceType.Present)
//            currentState.copy(classList = updatedSubjectList)
//        }
//    }
}