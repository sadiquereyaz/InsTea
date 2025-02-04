package `in`.instea.instea.presentation.schedule

import `in`.instea.instea.domain.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.domain.datamodel.DayDateModel


data class ScheduleUiState(
    val scheduleList: List<CombinedScheduleTaskModel> = listOf(),
    val dayDateList: List<DayDateModel> = listOf(),
    val selectedDateIndex: Int = 15,      //current day/date
    val selectedDate: Int = 16,
    val selectedDay: String = "",
    var selectedMonth: String = "",
    val selectedYear: Int = 0,
    var timestamp: Int = 0
)
//before commit