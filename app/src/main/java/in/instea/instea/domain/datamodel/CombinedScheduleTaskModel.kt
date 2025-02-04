package `in`.instea.instea.domain.datamodel

import java.time.LocalTime

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