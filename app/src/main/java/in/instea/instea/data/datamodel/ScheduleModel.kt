package `in`.instea.instea.data.datamodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val repeat: Boolean = false,
    val remindBefore12: Boolean =false,
    val remindBefore24: Boolean =false,
)
data class TaskModel(
    val reminderBefore: String = "24:00",
    val task: String = "Assignment submit on monday"
)

//@Entity(tableName = "schedule")
//data class SubjectModel(
//    var subjectName: String,
//    var task: String = "Add Task",
//    var attendanceType: AttendanceType = AttendanceType.Absent,
//    var startTime: String,
//    var endTime: String,
//    var reminder: ReminderModel = ReminderModel()
//)

@Entity(tableName = "schedule")
data class ScheduleModel(
    @PrimaryKey(autoGenerate = true)
    val scheduleId: Int=0,
    val subject: String=" ",
    var task: String = "Add Task",
    var attendance: String = "Absent"
)
