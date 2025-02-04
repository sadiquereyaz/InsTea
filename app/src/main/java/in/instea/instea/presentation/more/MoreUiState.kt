package `in`.instea.instea.presentation.more

import `in`.instea.instea.domain.datamodel.SubjectAttendanceSummaryModel
import `in`.instea.instea.presentation.more.composable.TaskModel
import java.time.LocalDate

data class MoreUiState (
    val taskList: List<TaskModel> = emptyList(),
    val attendanceSummaries: List<SubjectAttendanceSummaryModel> = emptyList(),
    val selectedTimestamp: LocalDate = LocalDate.now(),
    var expandedIndex: Int? = null,
    val classmateList: List<classmate> = emptyList(),
    var isLoading: Boolean = false,
    var moveToAuth: Boolean = false,
    var errorMessage: String? = null,
    var showSnackBar: Boolean = false
)