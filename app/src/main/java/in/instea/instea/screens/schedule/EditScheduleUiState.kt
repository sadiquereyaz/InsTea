package `in`.instea.instea.screens.schedule

import java.sql.Time
import java.time.LocalDate
import java.time.LocalTime

data class EditScheduleUiState(
    var selectedDay: String="",
    var startTime: LocalTime = LocalTime.now(),
    var endTime: LocalTime = startTime.plusHours(1),
    val subjectList: List<String> = listOf(),
    var scheduleId: Int = 0,
    var selectedSubject: String="",
    val dayList: List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    var editScreenType: EditScreenType = EditScreenType.EditScreen,
)

sealed class EditScreenType(val isEditable:Boolean, val positiveButtonText: String){
    data object EditScreen: EditScreenType(true, "Save")
    data object AddScheduleScreen: EditScreenType(false, "Add")
}
