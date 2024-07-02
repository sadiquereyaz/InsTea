package `in`.instea.instea.data

enum class Attendance {
    Present, Absent, Cancelled
}

data class ClassDetail(
    val subjectName: String,
    val notes: String?,
    val attendance: Attendance?,
    val startTime: String,
    val endTime: String,
    val reminder: Boolean = false
)