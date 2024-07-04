package `in`.instea.instea.model.schedule

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class AttendanceType (val icon: ImageVector){
    Present(
        icon = Icons.Default.CheckCircle
    ),
    Absent(
        icon = Icons.Default.Clear
    ),
    Cancelled(
        icon = Icons.Default.Warning
    )
}

data class DayDateModel(
    val day: String, val date: String
)

data class ReminderModel(
    val remindBefore: String = "24:00",
    val repeat: Boolean = false,
)
data class TaskModel(
    val reminderBefore: String = "24:00",
    val task: String = "Assignment submit on monday"
)

data class SubjectModel(
    var subjectName: String,
    var task: String = "Add Task",
    var attendanceType: AttendanceType = AttendanceType.Absent,
    var startTime: String,
    var endTime: String,
    var reminder: Boolean = false
)