package `in`.instea.instea.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

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