package `in`.instea.instea.screens.schedule

import `in`.instea.instea.data.datamodel.SubjectModel


data class ScheduleUiState(
    val subjectList:List<SubjectModel> = emptyList(),
    var selectedDateIndex: Int = 15,
//    val currentDate: String="04",
//    val currentTime: String = "10:00",
    var selectedMonth: String = "August",
)
