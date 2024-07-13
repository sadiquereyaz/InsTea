package `in`.instea.instea.data.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import `in`.instea.instea.data.DataSource
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.DayDateModel
import `in`.instea.instea.data.datamodel.SubjectModel
import `in`.instea.instea.screens.schedule.ScheduleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleViewModel : ViewModel() {

    private val _scheduleUiState = MutableStateFlow(ScheduleUiState())
    val scheduleUiState: StateFlow<ScheduleUiState> = _scheduleUiState.asStateFlow()

    val dayDateList: List<DayDateModel>
    val reminderRepeatSubjectList = mutableSetOf<String>()

    private val _subjectList = mutableStateListOf<SubjectModel>()


    init {
        dayDateList = generateDayDateList()
        selectDateIndex(15)
        _subjectList.addAll(DataSource.classSubjectData["Fri"] ?: emptyList())
        _scheduleUiState.value = ScheduleUiState(subjectList = _subjectList)
    }

    private fun generateDayDateList(): List<DayDateModel> {
        val calendar = Calendar.getInstance()
        val dayDateList = mutableListOf<DayDateModel>()
        // Start from 15 days before the current day
        calendar.add(Calendar.DAY_OF_YEAR, -15)

        repeat(45) {
            val day =
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
                    ?: ""
            val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
            dayDateList.add(DayDateModel(day = day, date = date))
            calendar.add(Calendar.DAY_OF_WEEK, 1)
        }
        return dayDateList
    }

    fun selectDateIndex(index: Int) {
        _scheduleUiState.value = _scheduleUiState.value.copy(
            selectedDateIndex = index,
            subjectList = DataSource.classSubjectData[dayDateList[index].day] ?: emptyList()
        )
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

    fun updateAttendanceType(subject: SubjectModel, attendanceType: AttendanceType) {
        val updatedSubjectList = _scheduleUiState.value.subjectList.toMutableList()
        val subjectIndex = updatedSubjectList.indexOf(subject)
        if (subjectIndex != -1) {
            updatedSubjectList[subjectIndex] = subject.copy(attendanceType = attendanceType)
            _scheduleUiState.value = _scheduleUiState.value.copy(subjectList = updatedSubjectList)
        }
        _scheduleUiState.value = _scheduleUiState.value.copy(
        )
    }

    fun modifySubjectInRepeatReminderList(subjectName:String, repeat: Boolean, subject: SubjectModel){
        if (repeat) {
            reminderRepeatSubjectList.add(subjectName)
//            _scheduleUiState.value = _scheduleUiState.value.copy(subjectList[index].)
        }else{
            reminderRepeatSubjectList.remove(subjectName)
        }
    }

    fun updateAttendance(index: Int) {
        _scheduleUiState.update { currentState ->
            val updatedSubjectList = currentState.subjectList.toMutableList()
            updatedSubjectList[index] = updatedSubjectList[index].copy(attendanceType = AttendanceType.Present)
            currentState.copy(subjectList = updatedSubjectList)
        }
    }
}