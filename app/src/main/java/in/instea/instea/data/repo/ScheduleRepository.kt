package `in`.instea.instea.data.repo

import `in`.instea.instea.data.dao.ScheduleDao
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.datamodel.TaskAttendanceModel

interface ScheduleRepository {

    suspend fun getScheduleAndTaskList(day: String, timeStamp: Int): List<CombinedScheduleTaskModel>
    suspend fun upsertTask(task: String, taskId: Int, scheduleId: Int, timeStamp: Int)
    suspend fun upsertAttendance(attendance: AttendanceType, taskId: Int, scheduleId: Int, timeStamp: Int)
    suspend fun upsertSchedule(schedule: ScheduleModel)
    suspend fun getScheduleById(id: Int): ScheduleModel
    suspend fun getAllSubjects(): List<String>
    suspend fun getAllScheduleByDay(day: String): List<ScheduleModel>
    suspend fun deleteScheduleById(id: Int)

}

class LocalScheduleRepository(private val scheduleDao: ScheduleDao) : ScheduleRepository {
    override suspend fun getScheduleAndTaskList(day: String, timeStamp: Int): List<CombinedScheduleTaskModel> =
        scheduleDao.getScheduleAndTaskList(selectedDay = day, selectedDate = timeStamp)

    override suspend fun upsertTask(task: String, taskId: Int, scheduleId: Int, timeStamp: Int) {
        if (taskId == 0) {
            scheduleDao.insertTask(TaskAttendanceModel(task = task, scheduleId = scheduleId, timestamp = timeStamp))
        } else {
            scheduleDao.updateTask(taskId = taskId, task = task)
        }
    }

    override suspend fun upsertAttendance(attendance: AttendanceType, taskId: Int, scheduleId: Int, timeStamp: Int) {
        if (taskId == 0) {
            scheduleDao.insertAttendance(TaskAttendanceModel(attendance = attendance, scheduleId = scheduleId, timestamp = timeStamp))
        } else {
            scheduleDao.updateAttendance(taskId = taskId, attendance = attendance)
        }
    }

    override suspend fun upsertSchedule(schedule: ScheduleModel) { scheduleDao.upsertSchedule(schedule) }
    override suspend fun getScheduleById(id: Int): ScheduleModel = scheduleDao.getScheduleById(id)
    override suspend fun getAllSubjects(): List<String> = scheduleDao.getAllSubject()
    override suspend fun getAllScheduleByDay(day: String): List<ScheduleModel> = scheduleDao.getAllScheduleByDay(day)
    override suspend fun deleteScheduleById(id: Int) = scheduleDao.deleteById(id)
}