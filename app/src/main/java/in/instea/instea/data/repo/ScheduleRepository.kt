package `in`.instea.instea.data.repo

import android.util.Log
import `in`.`in`.instea.instea.screens.more.composable.taskModel
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
    suspend fun upsertTask(task: String, subjectId: Int, timeStamp: Int)
    suspend fun upsertAttendance(attendance: AttendanceType, scheduleId: Int, timeStamp: Int)
    suspend fun upsertSchedule(schedule: ScheduleModel)
    suspend fun getCurrentSchedule(id: Int, day: String): ScheduleModel
//    suspend fun getAllSubjects(): List<String>
    suspend fun getAllScheduleByDay(day: String): List<ScheduleModel>       // for time conflict
    suspend fun deleteScheduleById(id: Int)
    suspend fun getSubjectAttendanceSummary(timestamp: Int): List<SubjectAttendanceSummaryModel>
    suspend fun upsertSubject(sub: String)
    suspend fun getAllSubjectFlow(): Flow<List<SubjectModel>>
    suspend fun getAllTasks():List<taskModel>
    suspend fun deleteTaskbyId(scheduleId: Int,timeStamp: Int,)
}

class LocalScheduleRepository(private val scheduleDao: ScheduleDao) : ScheduleRepository {
    override suspend fun getScheduleAndTaskList(
        day: String,
        timeStamp: Int
    ): List<CombinedScheduleTaskModel> =
        scheduleDao.getScheduleAndTaskList(selectedDay = day, selectedDate = timeStamp)

    override suspend fun upsertTask(task: String, subjectId: Int, timeStamp: Int) {
        if (scheduleDao.checkTaskAttendanceRowExistence(subjectId = subjectId, timestamp = timeStamp) == null) {
            scheduleDao.insertTaskAttendance(TaskAttendanceModel(task = task, subjectId = subjectId, timestamp = timeStamp))
        } else {
            scheduleDao.updateTask(task = task, scheduleId = subjectId, timestamp = timeStamp)
        }
    }

    override suspend fun upsertAttendance(
        attendance: AttendanceType,
        scheduleId: Int,
        timeStamp: Int
    ) {
        if (scheduleDao.checkTaskAttendanceRowExistence(subjectId = scheduleId, timestamp = timeStamp) == null) {
            scheduleDao.insertTaskAttendance(TaskAttendanceModel(attendance = attendance, subjectId = scheduleId, timestamp = timeStamp))
        } else {
            scheduleDao.updateAttendance(attendance = attendance, scheduleId = scheduleId, timestamp = timeStamp)
        }
    }

    override suspend fun upsertSchedule(schedule: ScheduleModel) {
        scheduleDao.upsertSchedule(schedule)
    }

    override suspend fun getCurrentSchedule(
        id: Int,
        day: String
    ): ScheduleModel = scheduleDao.getScheduleById(id)
//    override suspend fun getAllSubjects(): List<String> = scheduleDao.getAllSubject()
    override suspend fun getAllSubjectFlow(): Flow<List<SubjectModel>> = scheduleDao.getAllSubjectFlow()
    override suspend fun getAllScheduleByDay(day: String): List<ScheduleModel> = scheduleDao.getAllScheduleByDay(day)
    override suspend fun deleteScheduleById(id: Int) = scheduleDao.deleteById(id)
    override suspend fun getSubjectAttendanceSummary(timestamp: Int): List<SubjectAttendanceSummaryModel> = scheduleDao.getSubjectAttendanceSummary(startOfTimestamp = timestamp)
    override suspend fun upsertSubject(sub: String) = scheduleDao.upsertSubject(SubjectModel(subject = sub))
    override suspend fun getAllTasks(): List<taskModel> =scheduleDao.getAllTask()
    override suspend fun deleteTaskbyId(scheduleId: Int,timeStamp: Int)
        { scheduleDao.clearTaskById(scheduleId, timeStamp)
            Log.d("repository", "repo delete task clicked  ")}


}