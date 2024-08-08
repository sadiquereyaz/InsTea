package `in`.instea.instea.data.repo

import `in`.instea.instea.data.dao.ScheduleDao
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.datamodel.SubjectAttendanceSummaryModel
import `in`.instea.instea.data.datamodel.SubjectModel
import `in`.instea.instea.data.datamodel.TaskAttendanceModel
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun getScheduleAndTaskList(day: String, timeStamp: Int): List<CombinedScheduleTaskModel>
    suspend fun upsertTask(task: String, scheduleId: Int, timeStamp: Int)
    suspend fun upsertAttendance(attendance: AttendanceType, scheduleId: Int, timeStamp: Int)
    suspend fun upsertSchedule(schedule: ScheduleModel)
    suspend fun getScheduleById(id: Int): ScheduleModel
    suspend fun getAllSubjects(): List<String>
    suspend fun getAllScheduleByDay(day: String): List<ScheduleModel>
    suspend fun deleteScheduleById(id: Int)
    suspend fun getSubjectAttendanceSummary(timestamp: Int): List<SubjectAttendanceSummaryModel>
    suspend fun upsertSubject(sub: String)
    suspend fun getAllSubjectFlow(): Flow<List<String>>
}

class LocalScheduleRepository(private val scheduleDao: ScheduleDao) : ScheduleRepository {
    override suspend fun getScheduleAndTaskList(
        day: String,
        timeStamp: Int
    ): List<CombinedScheduleTaskModel> =
        scheduleDao.getScheduleAndTaskList(selectedDay = day, selectedDate = timeStamp)

    override suspend fun upsertTask(task: String, scheduleId: Int, timeStamp: Int) {
        if (scheduleDao.checkTaskAttendanceRowExistence(scheduleId = scheduleId, timestamp = timeStamp) == null) {
            scheduleDao.insertTaskAttendance(TaskAttendanceModel(task = task, scheduleId = scheduleId, timestamp = timeStamp))
        } else {
            scheduleDao.updateTask(task = task, scheduleId = scheduleId, timestamp = timeStamp)
        }
    }

    override suspend fun upsertAttendance(
        attendance: AttendanceType,
        scheduleId: Int,
        timeStamp: Int
    ) {
        if (scheduleDao.checkTaskAttendanceRowExistence(scheduleId = scheduleId, timestamp = timeStamp) == null) {
            scheduleDao.insertTaskAttendance(TaskAttendanceModel(attendance = attendance, scheduleId = scheduleId, timestamp = timeStamp))
        } else {
            scheduleDao.updateAttendance(attendance = attendance, scheduleId = scheduleId, timestamp = timeStamp)
        }
    }

    override suspend fun upsertSchedule(schedule: ScheduleModel) {
        scheduleDao.upsertSchedule(schedule)
    }

    override suspend fun getScheduleById(id: Int): ScheduleModel = scheduleDao.getScheduleById(id)
    override suspend fun getAllSubjects(): List<String> = scheduleDao.getAllSubject()
    override suspend fun getAllSubjectFlow(): Flow<List<String>> = scheduleDao.getAllSubjectFlow()
    override suspend fun getAllScheduleByDay(day: String): List<ScheduleModel> = scheduleDao.getAllScheduleByDay(day)
    override suspend fun deleteScheduleById(id: Int) = scheduleDao.deleteById(id)
    override suspend fun getSubjectAttendanceSummary(timestamp: Int): List<SubjectAttendanceSummaryModel> = scheduleDao.getSubjectAttendanceSummary(startOfTimestamp = timestamp)
    override suspend fun upsertSubject(sub: String) = scheduleDao.upsertSchedule(subject = SubjectModel(0, sub))
}