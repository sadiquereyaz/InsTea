package `in`.instea.instea.data.datamodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.time.LocalTime
import java.time.format.DateTimeFormatter

enum class AttendanceType(val icon: ImageVector, val title: String, val tint: Color?) {
    Present(
        title = "Present",
        icon = Icons.Default.CheckCircle,
        tint = Color(0xFF00BB00)
    ),
    Absent(
        title = "Absent",
        icon = Icons.Default.RemoveCircle,
        tint = Color(0xFFFF3333)
    ),
    Cancelled(
        title = "Cancelled",
        icon = Icons.Default.Warning,
        tint = Color(0xFFFFBE3B)
    ),
    MarkAttendance(
        title = "Attendance",
        icon = Icons.Default.AddTask,
        tint = null
    ),
}

data class DayDateModel(
    val day: String, val date: String
)

data class SubjectAttendanceSummaryModel(
    val subject: String,
    val totalClasses: Int,
    val attendedClasses: Int,
    val absentClasses: Int,
    val percentage: Float? = 89f,    //
)

@Entity(
    tableName = "schedule",
)
data class ScheduleModel(
    @PrimaryKey(autoGenerate = true)
    val scheduleId: Int = 0,
    val subjectId: Int,
    val subject: String,
    var startTime: LocalTime,
    var endTime: LocalTime,
    var day: String = "",
    val dailyReminderTime: LocalTime? = null
)

@Entity(
    tableName = "taskAttendance",
    primaryKeys = ["scheduleId", "subjectId", "timestamp"]
)
data class TaskAttendanceModel(
    val scheduleId: Int,    // Foreign key referencing ScheduleModel
    val subjectId: Int,
    val timestamp: Int, // Timestamp to track the date
    var attendance: AttendanceType? = null,  // Task or note for the class on the specific date
    var task: String? = null,  // Task or note for the class on the specific date
    val taskReminderBefore: Int = 0,
    val classReminderTime: LocalTime?=null,
)

@Entity(tableName = "subject_table")
data class SubjectModel(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int = 0, // Foreign key referencing ScheduleModel
    val subject: String
)

data class CombinedScheduleTaskModel(
    val scheduleId: Int,
    val subjectId: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val day: String,
    val timestamp: Int? = null,
    val subject: String? = null,
    val attendance: AttendanceType? = AttendanceType.MarkAttendance,
    var task: String? = null,
    val dailyReminderTime: LocalTime? = null,
    val classReminderTime: LocalTime? = null,
    val taskReminderBefore: Int = 0
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

