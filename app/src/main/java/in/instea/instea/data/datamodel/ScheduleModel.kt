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
    primaryKeys = ["subjectId", "day"]
)
data class ScheduleModel(
    val subjectId: Int,
    var startTime: LocalTime,
    var endTime: LocalTime,
    var day: String = "",
    val dailyReminder: Boolean = false
)

@Entity(
    tableName = "taskAttendance",
    primaryKeys = ["subjectId", "timestamp"]   //composite key
)
data class TaskAttendanceModel(
    val subjectId: Int, // Foreign key referencing ScheduleModel
    val timestamp: Int, // Timestamp to track the date
    var attendance: AttendanceType? = null,  // Task or note for the class on the specific date
    var task: String? = null,  // Task or note for the class on the specific date
    val taskReminder: Boolean? = null
)

@Entity(tableName = "subject_table")
data class SubjectModel(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int = 0, // Foreign key referencing ScheduleModel
    val subject: String
)

data class CombinedScheduleTaskModel(
    val subjectId: Int,
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

