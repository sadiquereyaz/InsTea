package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.DayDateModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.schedule.ScheduleUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ScheduleViewModel(
    private val scheduleRepository: ScheduleRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    // Generate dayDateList initially
    private val _dayDateList: List<DayDateModel> = generateDayDateList()

    // Use MutableStateFlow for state variables that need updates
    private val _calendar = Calendar.getInstance()
    private val _currentMonth = MutableStateFlow(
        _calendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        ) ?: ""
    )
    private val _currentYear = MutableStateFlow(_calendar.get(Calendar.YEAR) % 100)
    private val _selectedDateIndex = MutableStateFlow(15) // Initial selected index (16th position)

    private fun generateDayDateList(): List<DayDateModel> {
        val calendar = Calendar.getInstance()
        val dayDateList = mutableListOf<DayDateModel>()
        calendar.add(Calendar.DAY_OF_YEAR, -15) // Start 15 days before current day

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

    // Expose state variables as StateFlow for observation
    val currentMonth: StateFlow<String> = _currentMonth
    val currentYear: StateFlow<Int> = _currentYear
    val selectedDateIndex: StateFlow<Int> = _selectedDateIndex

    private val scheduleListFlow: Flow<List<ScheduleModel>> = scheduleRepository.getClassList()

    val scheduleUiState: StateFlow<ScheduleUiState> = combine(
        scheduleListFlow,
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
            selectedMonth = _currentMonth.value,
            selectedYear = _currentYear.value,
            selectedDateIndex = _selectedDateIndex.value
        )
    )

    suspend fun updateAttendance(scheduleId: Int) {
        scheduleRepository.updateAttendance(scheduleId)
    }

    suspend fun updateTask(scheduleId: Int, task: String) {
        scheduleRepository.updateTask(scheduleId = scheduleId, task = task)
    }

    suspend fun onDateClick(selectedIndex: Int, currentDateIndex: Int = 15) {
        val selectedDateCalendar = Calendar.getInstance()
        selectedDateCalendar.set(Calendar.DAY_OF_MONTH, _calendar.get(Calendar.DAY_OF_MONTH))
        selectedDateCalendar.set(Calendar.YEAR, _calendar.get(Calendar.YEAR))
        selectedDateCalendar.add(Calendar.DAY_OF_YEAR, selectedIndex - currentDateIndex)

        viewModelScope.launch { // Launch a coroutine to update state in background
            _currentMonth.value = selectedDateCalendar.getDisplayName(
                Calendar.MONTH, Calendar.LONG, Locale.getDefault()
            ) ?: ""
            _currentYear.value = selectedDateCalendar.get(Calendar.YEAR) % 100
            _selectedDateIndex.value = selectedIndex

            // Optional: Re-generate dayDateList if needed
//             if (needsRegeneration(selectedIndex, currentDateIndex)) {
//               _dayDateList = generateDayDateList(selectedDateCalendar)
//             }
        }
    }
}