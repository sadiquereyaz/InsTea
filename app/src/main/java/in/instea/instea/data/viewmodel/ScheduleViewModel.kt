package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.DayDateModel
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.schedule.ScheduleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScheduleUiState())
    val uiState: StateFlow<ScheduleUiState> = _uiState

    private val todayDateIndex: Int = 15
    private val _calendar = Calendar.getInstance()

    init {
        getDayDateList()
        onDateClick(_uiState.value.selectedDateIndex)
    }

    private fun getDayDateList() {
        viewModelScope.launch {
            val calendar = Calendar.getInstance()
            val dayDateList = mutableListOf<DayDateModel>()
            calendar.add(Calendar.DAY_OF_YEAR, -todayDateIndex) // Start 15 days before current day

            repeat(60) {
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
            _uiState.update {
                it.copy(dayDateList = dayDateList)
            }
        }
    }

    //    private var timestamp: Int = 0      // TODO: check its value on different date click
    fun onDateClick(selectedIndex: Int) {
        // Launch a coroutine to update state in background
        viewModelScope.launch {
            // getting the timestamp according to selected index
            val selectedDateCalendar = Calendar.getInstance()
            selectedDateCalendar.set(Calendar.DAY_OF_MONTH, _calendar.get(Calendar.DAY_OF_MONTH))
            selectedDateCalendar.set(Calendar.YEAR, _calendar.get(Calendar.YEAR))
            selectedDateCalendar.add(Calendar.DAY_OF_YEAR, selectedIndex - todayDateIndex)


            //updating month on the basis of selected date
            val selectedMonth = selectedDateCalendar.getDisplayName(
                Calendar.MONTH, Calendar.SHORT, Locale.getDefault()
            ) ?: ""
            val selectedYear = selectedDateCalendar.get(Calendar.YEAR) % 100
            val selectedDay = selectedDateCalendar.getDisplayName(
                Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()
            ) ?: ""
            _uiState.update {
                it.copy(
                    selectedMonth = selectedMonth,
                    selectedYear = selectedYear,
                    selectedDateIndex = selectedIndex,
                    selectedDay = selectedDay,
                    timestamp = getTimestampForSelectedDay(selectedDateCalendar.time)
                )
            }
//            timestamp = getTimestampForSelectedDay(selectedDateCalendar.time)
            fetchSchedulesAndTask(/*selectedDay, timestamp*/)
        }
    }

    private fun getTimestampForSelectedDay(selectedDate: Date): Int {
        val formatter = SimpleDateFormat("yyMMdd", Locale.getDefault()) // format for YYMMDD
        val formattedDate = formatter.format(selectedDate)
        return formattedDate.toInt() // convert formatted string to Long
    }

    private fun fetchSchedulesAndTask(/*day: String, timeStamp: Int*/) {
        Log.d("SCHEDULE_FETCH", _uiState.value.selectedDay)
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    scheduleList = scheduleRepository.getScheduleAndTaskList(
                        _uiState.value.selectedDay,
                        _uiState.value.timestamp
                    )
                )
            }
        }
    }

    fun upsertAttendance(scheduleId: Int, attendance: AttendanceType) {
        viewModelScope.launch {
            scheduleRepository.upsertAttendance(
                attendance = attendance,
                scheduleId = scheduleId,
                timeStamp = _uiState.value.timestamp
            )
        }
    }

    suspend fun upsertTask(scheduleId: Int, task: String) {
        scheduleRepository.upsertTask(
            task = task,
            scheduleId = scheduleId,
            timeStamp = _uiState.value.timestamp
        )
    }

}