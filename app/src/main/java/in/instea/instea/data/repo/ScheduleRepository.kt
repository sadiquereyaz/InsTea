package `in`.instea.instea.data.repo

import android.util.Log
import `in`.instea.instea.data.local.dao.ScheduleDao
import `in`.instea.instea.data.local.entity.ScheduleModel
import `in`.instea.instea.data.local.entity.SubjectModel
import `in`.instea.instea.data.local.entity.TaskAttendanceModel
import `in`.instea.instea.domain.datamodel.AttendanceType
import `in`.instea.instea.domain.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.domain.datamodel.SubjectAttendanceSummaryModel
import `in`.instea.instea.domain.repo.ScheduleRepository
import `in`.instea.instea.presentation.more.composable.TaskModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime


class LocalScheduleRepository(private val scheduleDao: ScheduleDao) : ScheduleRepository {
    override suspend fun getScheduleAndTaskList(
        day: String,
        timeStamp: Int
    ): List<CombinedScheduleTaskModel> =
        scheduleDao.getScheduleAndTaskList(selectedDay = day, selectedDate = timeStamp)

    override suspend fun upsertTask(
        task: String?,
        subjectId: Int,
        timeStamp: Int,
        scheduleId: Int,
        taskReminderBefore: Int
    ) {
        if (scheduleDao.checkTaskAttendanceRowExistence(
                scheduleId = scheduleId,
                subjectId = subjectId,
                timestamp = timeStamp
            ) == null
        ) {
            scheduleDao.insertTaskAttendance(
                TaskAttendanceModel(
                    task = task,
                    subjectId = subjectId,
                    scheduleId = scheduleId,
                    timestamp = timeStamp,
                    taskReminderBefore = taskReminderBefore
                )
            )
        } else {
            scheduleDao.updateTask(
                task = task,
                subjectId = subjectId,
                scheduleId = scheduleId,
                timestamp = timeStamp,
                taskReminderBefore = taskReminderBefore
            )
        }
    }

    override suspend fun upsertAttendance(
        attendance: AttendanceType,
        subjectId: Int,
        timeStamp: Int,
        scheduleId: Int
    ) {
        if (scheduleDao.checkTaskAttendanceRowExistence(
                scheduleId = scheduleId,
                subjectId = subjectId,
                timestamp = timeStamp
            ) == null
        ) {
            scheduleDao.insertTaskAttendance(
                TaskAttendanceModel(
                    attendance = attendance,
                    subjectId = subjectId,
                    timestamp = timeStamp,
                    scheduleId = scheduleId
                )
            )
        } else {
            scheduleDao.updateAttendance(
                attendance = attendance,
                scheduleId = scheduleId,
                subjectId = subjectId,
                timestamp = timeStamp
            )
        }
    }

    override suspend fun upsertSchedule(schedule: ScheduleModel) {
        scheduleDao.upsertSchedule(schedule)
    }

    override suspend fun getScheduleById(id: Int): ScheduleModel = scheduleDao.getScheduleById(id)

    //    override suspend fun getAllSubjects(): List<String> = scheduleDao.getAllSubject()
    override suspend fun getAllSubjectFlow(): Flow<List<SubjectModel>> =
        scheduleDao.getAllSubjectFlow()

    override suspend fun getAllScheduleByDay(day: String): List<ScheduleModel> =
        scheduleDao.getAllScheduleByDay(day)

    override suspend fun deleteScheduleById(id: Int) =
        scheduleDao.deleteScheduleById(id)

    override suspend fun getSubjectAttendanceSummary(timestamp: Int): List<SubjectAttendanceSummaryModel> =
        scheduleDao.getSubjectAttendanceSummary(startOfTimestamp = timestamp)

    override suspend fun upsertSubject(sub: String): Long =
        scheduleDao.upsertSubject(SubjectModel(subject = sub))

    override suspend fun getAllTasks(): List<TaskModel> = scheduleDao.getAllTask()
    override suspend fun deleteTaskById(scheduleId: Int, subjectId: Int, timeStamp: Int) {
        scheduleDao.clearTask(scheduleId, subjectId, timeStamp)
        Log.d("repository", "repo delete task clicked  ")
    }

    override suspend fun saveDailyClassReminder(scheduleId: Int, time: LocalTime?) {
        Log.d("SCHEDULE_REPO","time: $time")
        scheduleDao.saveDailyClassReminder(scheduleId, time)
    }


}