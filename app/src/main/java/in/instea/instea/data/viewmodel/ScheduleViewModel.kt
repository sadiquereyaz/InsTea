package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.DayDateModel
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.schedule.ScheduleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val todayDateIndex: Int = 15

    // Generate dayDateList initially
    private val _dayDateList: List<DayDateModel> = generateDayDateList()

    // Use MutableStateFlow for state variables that need updates
    private val _calendar = Calendar.getInstance()
    private val _selectedMonth = MutableStateFlow(
        _calendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        ) ?: ""
    )
    private val _selectedDay = MutableStateFlow(
        _calendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        ) ?: ""
    )
    private val _selectedYear =
        MutableStateFlow(_calendar.get(Calendar.YEAR) % 100)        //by default current year
    private val _selectedDateIndex =
        MutableStateFlow(todayDateIndex) // Initial selected index (16th position)

    // Expose state variables as StateFlow for observation
    private val currentMonth: StateFlow<String> = _selectedMonth
    private val currentYear: StateFlow<Int> = _selectedYear
    private val selectedDateIndex: StateFlow<Int> = _selectedDateIndex

    // Use a MutableStateFlow for the current list of schedules
    private val _scheduleList = MutableStateFlow<List<CombinedScheduleTaskModel>>(emptyList())
    private val scheduleList: StateFlow<List<CombinedScheduleTaskModel>> = _scheduleList

    init {
        onDateClick(todayDateIndex)
    }

    val scheduleUiState: StateFlow<ScheduleUiState> = combine(
        scheduleList,
        currentMonth,
        currentYear,
        selectedDateIndex
    ) { scheduleList, month, year, selectedIndex ->
        ScheduleUiState(
            dayDateList = _dayDateList,
            classList = scheduleList,
            selectedMonth = month,
            selectedYear = year,
            selectedDateIndex = selectedIndex
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = ScheduleUiState(
            dayDateList = _dayDateList,
            classList = emptyList(),
            selectedMonth = _selectedMonth.value,
            selectedYear = _selectedYear.value,
            selectedDateIndex = _selectedDateIndex.value,
        )
    )
    private var timestamp: Int = 0      // TODO: check its value on different date click
    fun onDateClick(selectedIndex: Int) {
        // getting the timestamp according to selected index
        val selectedDateCalendar = Calendar.getInstance()
        selectedDateCalendar.set(Calendar.DAY_OF_MONTH, _calendar.get(Calendar.DAY_OF_MONTH))
        selectedDateCalendar.set(Calendar.YEAR, _calendar.get(Calendar.YEAR))
        selectedDateCalendar.add(Calendar.DAY_OF_YEAR, selectedIndex - todayDateIndex)

        // Launch a coroutine to update state in background
        viewModelScope.launch {
            //updating month on the basis of selected date
            _selectedMonth.value = selectedDateCalendar.getDisplayName(
                Calendar.MONTH, Calendar.SHORT, Locale.getDefault()
            ) ?: ""
            _selectedYear.value = selectedDateCalendar.get(Calendar.YEAR) % 100
            _selectedDateIndex.value = selectedIndex
            val selectedDay = selectedDateCalendar.getDisplayName(
                Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()
            ) ?: ""
            timestamp = getTimestampForSelectedDay(selectedDateCalendar.time)
            fetchSchedulesForDay(selectedDay, timestamp)
        }
    }

    private fun getTimestampForSelectedDay(selectedDate: Date): Int {
        val formatter = SimpleDateFormat("yyMMdd", Locale.getDefault()) // format for YYMMDD
        val formattedDate = formatter.format(selectedDate)
        return formattedDate.toInt() // convert formatted string to Long
    }

    private fun fetchSchedulesForDay(day: String, timeStamp: Int) {
        viewModelScope.launch {
            scheduleRepository.getClassListByDayAndTaskByDate(day, timeStamp).collect { schedules ->
                _scheduleList.value = schedules
            }
        }
    }

    private fun generateDayDateList(): List<DayDateModel> {
        val calendar = Calendar.getInstance()
        val dayDateList = mutableListOf<DayDateModel>()
        calendar.add(Calendar.DAY_OF_YEAR, -todayDateIndex) // Start 15 days before current day

        repeat(60) {
            val day =
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
                    ?: ""
            val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
            dayDateList.add(DayDateModel(day = day, date = date))
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return dayDateList
    }

    suspend fun upsertSchedule(
        subject: String,
        scheduleId: Int,
        startTime: String,
        endTime: String,
        day: String
    ) {
        scheduleRepository.upsertSchedule(
            subject = subject,
            scheduleId = scheduleId,
            startTime = startTime,
            endTime = endTime,
            day = day
        )
    }

    suspend fun upsertAttendance(taskId: Int, scheduleId: Int, attendance: AttendanceType) {
        scheduleRepository.upsertAttendance(taskId = taskId, attendance = attendance, scheduleId = scheduleId, timeStamp = timestamp)
    }

    suspend fun upsertTask(scheduleId: Int, taskId: Int, task: String) {
        scheduleRepository.upsertTask(
            taskId = taskId,
            task = task,
            scheduleId = scheduleId,
            timeStamp = timestamp
        )
    }
}