package `in`.instea.instea.presentation.schedule


import `in`.instea.instea.data.local.entity.SubjectModel
import java.time.LocalTime

data class EditScheduleUiState(
    var selectedDay: String = "",
    var startTime: LocalTime = LocalTime.now(),
    var endTime: LocalTime = startTime.plusHours(1),
    val subjectModelList: List<SubjectModel> = listOf(),
    var subjectId: Int = 0,
    var subject: String = "",

    var scheduleId: Int = 0,

    var selectedSubject: String = "",
    var subjectError: String? = null,

    val dayList: List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
    var readOnly: Boolean = true,

    var showSnackBar: Boolean = false,
    var isLoading: Boolean = false,
    val errorMessage: String? = null,
    var editScreenType: EditScreenType = EditScreenType.EditScreen,
)

sealed class EditScreenType(val isEditable: Boolean, val positiveButtonText: String) {
    data object EditScreen : EditScreenType(true, "Save")
    data object AddScheduleScreen : EditScreenType(false, "Add")
}
