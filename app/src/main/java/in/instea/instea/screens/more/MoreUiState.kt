package `in`.instea.instea.screens.more

import `in`.`in`.instea.instea.screens.more.composable.taskModel
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.SubjectAttendanceSummaryModel
import java.time.LocalDate

data class MoreUiState (
    val taskList: List<taskModel> = emptyList(),
    val attendanceSummaries: List<SubjectAttendanceSummaryModel> = emptyList(),
    val selectedTimestamp: LocalDate = LocalDate.now(),
    var expandedIndex: Int? = null,
    var isLoading: Boolean = false,
    var moveToAuth: Boolean = false,
    var errorMessage: String? = null,
    var showSnackBar: Boolean = false
)