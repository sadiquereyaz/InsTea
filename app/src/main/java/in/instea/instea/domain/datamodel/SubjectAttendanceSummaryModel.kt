package `in`.instea.instea.domain.datamodel

data class SubjectAttendanceSummaryModel(
    val subject: String,
    val totalClasses: Int,
    val attendedClasses: Int,
    val absentClasses: Int,
    val percentage: Float? = 89f,    //
)