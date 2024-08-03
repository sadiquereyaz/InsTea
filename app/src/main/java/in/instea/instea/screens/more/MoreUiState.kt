package `in`.instea.instea.screens.more

import `in`.instea.instea.data.datamodel.SubjectAttendanceSummaryModel
import java.time.LocalDate

data class MoreUiState (
    val taskList: List<String> = emptyList(),
    val attendanceSummaries: List<SubjectAttendanceSummaryModel> = emptyList(),
    val selectedTimestamp: LocalDate = LocalDate.now(),
    var expandedIndex: Int? = null,
    var moveToAuth: Boolean = false
)