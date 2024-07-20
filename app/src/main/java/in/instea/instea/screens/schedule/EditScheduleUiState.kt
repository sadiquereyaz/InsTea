package `in`.instea.instea.screens.schedule

import java.sql.Time
import java.time.LocalTime

data class EditScheduleUiState(
    var selectedSubject: String = "Sub",
    var selectedDay: String = "Sat",
    var startTime: LocalTime = LocalTime.now(),
    var endTime: LocalTime = LocalTime.now().plusHours(1),
    val subjectList: List<String> = listOf(),
    val dayList: List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class EditScreenType(val isEditable:Boolean, val positiveButtonText: String){
    data object EditScreen: EditScreenType(true, "Save")
    data object AddScheduleScreen: EditScreenType(false, "Add")
}
