package `in`.instea.instea.screens.more

import `in`.instea.instea.data.datamodel.SubjectAttendanceSummaryModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

data class MoreUiState (
    val taskList: List<String> = emptyList(),
    val attendanceSummaries: List<SubjectAttendanceSummaryModel> = emptyList(),
    val selectedMonth: Int = LocalDate.now().month.value
)