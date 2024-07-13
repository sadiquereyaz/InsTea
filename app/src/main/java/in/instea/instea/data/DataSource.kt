package `in`.instea.instea.data

import `in`.instea.instea.model.schedule.AttendanceType
import `in`.instea.instea.model.schedule.ReminderModel
import `in`.instea.instea.model.schedule.SubjectModel

object DataSource {

    val departments = listOf(
        "Computer Science",
        "Mechanical Engineering",
        "Electrical Engineering",
        "Civil Engineering"
    )
    val semesters = listOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII")
    val universities = listOf(
        "Jamia Millia Islamia",
        "Aligarh Muslim University",
        "Jawaharlal University",
        "Delhi University"
    )
    val platforms= listOf("LinkedIn","Instagram")
    val graduatingYears = listOf("2023", "2024", "2025", "2026", "2027", "2028", "2029", "2030")

    val classSubjectData: Map<String, List<SubjectModel>> = mapOf(
        "Mon" to listOf(
            SubjectModel(
                subjectName = "Engineering Mathematics",
                task = "Chapter 1: Algebra Assignment to be submitted on Monday",
                attendanceType = AttendanceType.Present,
                startTime = "09:00",
                endTime = "10:00",
                reminder = ReminderModel(repeat = true, remindBefore12 = true)
            ),
            SubjectModel(
                subjectName = "Physics",
                attendanceType = AttendanceType.Absent,
                startTime = "11:00",
                endTime = "12:00",
                reminder = ReminderModel(remindBefore24 = true)
            )
        ),
        "Tue" to listOf(
            SubjectModel(
                subjectName = "Chemistry",
                task = "Chapter 3: Organic Chemistry",
                attendanceType = AttendanceType.Present,
                startTime = "10:00",
                endTime = "11:00",
                reminder = ReminderModel(repeat = true, remindBefore12 = true)
            ),
            SubjectModel(
                subjectName = "Biology",
                task = "Chapter 4: Genetics",
                attendanceType = AttendanceType.Absent,
                startTime = "13:00",
                endTime = "14:00",
                reminder = ReminderModel(remindBefore24 = true)
            )
        ),
        "Wed" to listOf(
            SubjectModel(
                subjectName = "History",
                task = "Chapter 5: World War II",
                attendanceType = AttendanceType.Absent,
                startTime = "09:00",
                endTime = "10:00",
                reminder = ReminderModel(repeat = true, remindBefore12 = true)
            ),
            SubjectModel(
                subjectName = "Geography",
                task = "Chapter 6: Climate Change",
                attendanceType = AttendanceType.Present,
                startTime = "11:00",
                endTime = "12:00",
                reminder = ReminderModel(remindBefore24 = true)
            )
        ),
        "Thu" to listOf(
            SubjectModel(
                subjectName = "Economics",
                task = "Chapter 7: Market Structures",
                attendanceType = AttendanceType.Present,
                startTime = "10:00",
                endTime = "11:00",
                reminder = ReminderModel(repeat = true, remindBefore12 = true)
            ),
            SubjectModel(
                subjectName = "Political Science",
                task = "Chapter 8: Government Systems",
                attendanceType = AttendanceType.Absent,
                startTime = "12:00",
                endTime = "13:00",
                reminder = ReminderModel(remindBefore24 = true)
            )
        ),
        "Fri" to listOf(
            SubjectModel(
                subjectName = "Computer Science",
                task = "Chapter 9: Programming Basics",
                attendanceType = AttendanceType.Absent,
                startTime = "09:00",
                endTime = "10:00",
                reminder = ReminderModel(repeat = true, remindBefore12 = true)
            ),
            SubjectModel(
                subjectName = "Art",
                task = "Chapter 10: Renaissance Art",
                attendanceType = AttendanceType.Absent,
                startTime = "11:00",
                endTime = "12:00",
                reminder = ReminderModel(remindBefore24 = true)
            )
        ),
        "Sat" to listOf(
            SubjectModel(
                subjectName = "Physical Education",
                task = "Chapter 11: Fitness",
                attendanceType = AttendanceType.Absent,
                startTime = "10:00",
                endTime = "11:00",
                reminder = ReminderModel(repeat = true, remindBefore12 = true)
            ),
            SubjectModel(
                subjectName = "Music",
                task = "Chapter 12: Classical Music",
                attendanceType = AttendanceType.Absent,
                startTime = "12:00",
                endTime = "13:00",
                reminder = ReminderModel(remindBefore24 = true)
            )
        ),
        "Sun" to listOf(
            SubjectModel(
                subjectName = "Philosophy",
                task = "Chapter 13: Ethics",
                attendanceType = AttendanceType.Absent,
                startTime = "09:00",
                endTime = "10:00",
                reminder = ReminderModel(repeat = true, remindBefore12 = true)
            ),
            SubjectModel(
                subjectName = "Psychology",
                task = "Chapter 14: Cognitive Development",
                attendanceType = AttendanceType.Absent,
                startTime = "11:00",
                endTime = "12:00",
                reminder = ReminderModel(remindBefore24 = true)
            )
        )
    )
}