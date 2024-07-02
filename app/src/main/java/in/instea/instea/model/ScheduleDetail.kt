package `in`.instea.instea.model

import androidx.compose.runtime.Immutable

enum class Attendance {
    Present, Absent, Cancelled
}

@Immutable
data class DayDate(
    val day: String, val date: String
)

@Immutable
data class ClassDetail(
    val subjectName: String,
    val notes: String?,
    val attendance: Attendance?,
    val startTime: String,
    val endTime: String,
    val reminder: Boolean = false
)