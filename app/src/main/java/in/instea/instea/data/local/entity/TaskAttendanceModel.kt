package `in`.instea.instea.data.local.entity

import androidx.room.Entity
import `in`.instea.instea.domain.datamodel.AttendanceType
import java.time.LocalTime

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