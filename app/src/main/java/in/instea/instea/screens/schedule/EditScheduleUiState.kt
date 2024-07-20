package `in`.instea.instea.screens.schedule

import java.sql.Time
import java.time.LocalTime

data class EditScheduleUiState(
    var selectedSubject: String = "Subject Name",
    var selectedDay: String = "Monday",
    var startTime: LocalTime = LocalTime.now(),
    var endTime: LocalTime = LocalTime.now().plusHours(1),
    val subjectList: List<String> = listOf("opt 1", "opt 2", "opt 3"),
    val dayList: List<String> = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

sealed class EditScreenType(val isEditable:Boolean, val positiveButtonText: String){
    data object EditScreen: EditScreenType(true, "Save")
    data object AddScheduleScreen: EditScreenType(false, "Add")
}
