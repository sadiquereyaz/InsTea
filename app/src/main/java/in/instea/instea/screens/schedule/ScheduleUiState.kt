package `in`.instea.instea.screens.schedule

import `in`.instea.instea.data.datamodel.DayDateModel
import `in`.instea.instea.data.datamodel.ScheduleModel


data class ScheduleUiState(
    val classList: List<ScheduleModel> = listOf(),
    val dayDateList: List<DayDateModel> = listOf(),
    val selectedDateIndex: Int = 15,      //current day/date
    val selectedDate: Int = 16,
    val selectedDay: Int = 16,
    val selectedMonth: String = "",
    val selectedYear: Int = 0,
)
//before commit