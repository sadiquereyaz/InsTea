package `in`.instea.instea.screens.schedule

import `in`.instea.instea.data.datamodel.ScheduleModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow


data class ScheduleUiState(
    val classList: List<ScheduleModel> = listOf(),
    var selectedDateIndex: Int = 15,
//    val currentDate: String="04",
//    val currentTime: String = "10:00",
    var selectedMonth: String = "August",
)
