package `in`.instea.instea.data.datamodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class AttendanceType(val icon: ImageVector, val title: String) {
    Present(
        title = "Present",
        icon = Icons.Default.CheckCircle
    ),
    Absent(
        title = "Absent",
        icon = Icons.Default.RemoveCircle
    ),
    Cancelled(
        title = "Cancelled",
        icon = Icons.Default.Warning
    ),
    MarkAttendance(
        title = "Attendance",
        icon = Icons.Default.AddTask
    ),
}

data class DayDateModel(
    val day: String, val date: String
)

data class ReminderModel(
    val repeat: Boolean = false,
    val remindBefore12: Boolean = false,
    val remindBefore24: Boolean = false,
)

data class TaskModel(
    val reminderBefore: String = "24:00",
    val task: String = "Assignment submit on monday"
)

/*@Entity(tableName = "schedule")
data class SubjectModel(
    var subjectName: String,
    var task: String = "Add Task",
    var attendanceType: AttendanceType = AttendanceType.Absent,
    var startTime: String,
    var endTime: String,
    var reminder: ReminderModel = ReminderModel()
)*/

@Entity(tableName = "schedule")
data class ScheduleModel(
    @PrimaryKey(autoGenerate = true)
    val scheduleId: Int = 0,
    var subject: String,
    var startTime: LocalTime,
    var endTime: LocalTime,
    var day: String = "",
    val dailyReminder: Boolean = false
)

class TimeConverters {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        return time?.format(formatter)
    }

    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? {
        return timeString?.let {
            LocalTime.parse(it, formatter)
        }
    }
}

@Entity(
    tableName = "taskAttendance",
    primaryKeys = ["scheduleId", "timestamp"]   //composite key
)
data class TaskAttendanceModel(
    val scheduleId: Int, // Foreign key referencing ScheduleModel
    val timestamp: Int, // Timestamp to track the date
    var attendance: AttendanceType? = null,  // Task or note for the class on the specific date
    var task: String? = null,  // Task or note for the class on the specific date
    val taskReminder: Boolean? = null
)

data class CombinedScheduleTaskModel(
    val scheduleId: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val day: String,
    val dailyReminder: Boolean? = false,
    val timestamp: Int? = null,
    val subject: String? = null,
    val attendance: AttendanceType? = AttendanceType.MarkAttendance,
    var task: String? = null,
    val taskReminder: Boolean? = false
)


class AttendanceTypeConverter {
    @TypeConverter
    fun fromAttendanceType(value: AttendanceType): Int {
        return value.ordinal // Store the ordinal (position) of the enum value
    }

    @TypeConverter
    fun toAttendanceType(value: Int): AttendanceType {
        return AttendanceType.values()[value] // Convert the ordinal back to the enum value
    }
}

