package `in`.instea.instea.domain.repo

import `in`.instea.instea.data.local.entity.ScheduleModel
import `in`.instea.instea.data.local.entity.SubjectModel
import `in`.instea.instea.domain.datamodel.AttendanceType
import `in`.instea.instea.domain.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.domain.datamodel.SubjectAttendanceSummaryModel
import `in`.instea.instea.presentation.more.composable.TaskModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface ScheduleRepository {

    suspend fun getScheduleAndTaskList(day: String, timeStamp: Int): List<CombinedScheduleTaskModel>
    suspend fun upsertTask(
        task: String?,
        subjectId: Int,
        timeStamp: Int,
        scheduleId: Int,
        taskReminderBefore: Int
    )

    suspend fun upsertAttendance(
        attendance: AttendanceType,
        subjectId: Int,
        timeStamp: Int,
        scheduleId: Int
    )

    suspend fun upsertSchedule(schedule: ScheduleModel)
    suspend fun getScheduleById(id: Int): ScheduleModel

    //    suspend fun getAllSubjects(): List<String>
    suspend fun getAllScheduleByDay(day: String): List<ScheduleModel>       // for time conflict
    suspend fun deleteScheduleById(id: Int)
    suspend fun getSubjectAttendanceSummary(timestamp: Int): List<SubjectAttendanceSummaryModel>
    suspend fun upsertSubject(sub: String): Long
    suspend fun getAllSubjectFlow(): Flow<List<SubjectModel>>
    suspend fun getAllTasks(): List<TaskModel>
    suspend fun deleteTaskById(scheduleId: Int, subjectId: Int, timeStamp: Int)
    suspend fun saveDailyClassReminder(scheduleId: Int, time: LocalTime?)
}