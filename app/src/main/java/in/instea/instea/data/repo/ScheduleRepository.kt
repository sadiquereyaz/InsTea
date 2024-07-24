package `in`.instea.instea.data.repo

import `in`.instea.instea.data.dao.ScheduleDao
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.datamodel.TaskAttendanceModel
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

interface ScheduleRepository {
    fun getClassListByDayAndTaskByDate(day: String, timeStamp: Int): Flow<List<CombinedScheduleTaskModel>>

    suspend fun upsertTask(task: String, taskId: Int, scheduleId: Int, timeStamp: Int)
    suspend fun upsertAttendance(
        attendance: AttendanceType,
        taskId: Int,
        scheduleId: Int,
        timeStamp: Int
    )

    suspend fun upsertSchedule(
        subject: String,
        scheduleId: Int,
        startTime: LocalTime,
        endTime: LocalTime,
        day: String
    )

    suspend fun getScheduleById(id: Int): ScheduleModel
    suspend fun getAllSubjects(): List<String>
    suspend fun getAllScheduleByDay(day: String): List<ScheduleModel>
    suspend fun deleteScheduleById(id: Int)

}

class LocalScheduleRepository(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {
    override fun getClassListByDayAndTaskByDate(
        day: String,
        timeStamp: Int
    ): Flow<List<CombinedScheduleTaskModel>> =
        scheduleDao.getClassesWithTasksByDayAndDate(selectedDay = day, selectedDate = timeStamp)

    override suspend fun upsertTask(task: String, taskId: Int, scheduleId: Int, timeStamp: Int) {
        if (taskId == 0) {
            scheduleDao.insertTask(
                TaskAttendanceModel(
                    task = task,
                    scheduleId = scheduleId,
                    timestamp = timeStamp
                )
            )
        } else {
            scheduleDao.updateTask(taskId = taskId, task = task)
        }
    }

    override suspend fun upsertAttendance(
        attendance: AttendanceType,
        taskId: Int,
        scheduleId: Int,
        timeStamp: Int
    ) {
        if (taskId == 0) {
            scheduleDao.insertAttendance(
                TaskAttendanceModel(
                    attendance = attendance,
                    scheduleId = scheduleId,
                    timestamp = timeStamp
                )
            )
        } else {
            scheduleDao.updateAttendance(taskId = taskId, attendance = attendance)
        }
    }

    override suspend fun upsertSchedule(
        subject: String,
        scheduleId: Int,
        startTime: LocalTime,
        endTime: LocalTime,
        day: String
    ) {
//        if (scheduleId == 0) {
//            scheduleDao.insertSchedule(
//                ScheduleModel(
//                    subject = subject,
//                    startTime = startTime,
//                    endTime = endTime,
//                    day = day
//                )
//            )
//        } else {
            scheduleDao.upsertSchedule(
                ScheduleModel(
                    subject = subject,
                    scheduleId = scheduleId,
                    startTime = startTime,
                    endTime = endTime,
                    day = day
                )
            )
//        }
    }

    override suspend fun getScheduleById(id: Int): ScheduleModel = scheduleDao.getScheduleById(id)
    override suspend fun getAllSubjects(): List<String> = scheduleDao.getAllSubject()
    override suspend fun getAllScheduleByDay(day: String): List<ScheduleModel> = scheduleDao.getAllScheduleByDay(day)
    override suspend fun deleteScheduleById(id: Int) = scheduleDao.deleteById(id)
}