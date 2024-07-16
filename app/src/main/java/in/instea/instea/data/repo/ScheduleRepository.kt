package `in`.instea.instea.data.repo

import `in`.instea.instea.data.dao.ScheduleDao
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getClassListByDayAndTaskByDate(day: String, timeStamp: Long): Flow<List<CombinedScheduleTaskModel>>
    suspend fun upsertAttendance(taskId: Int)
    suspend fun updateTask(task: String, scheduleId: Int)
    suspend fun upsert(scheduleDao: ScheduleModel)
}

class LocalScheduleRepository(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {
    override fun getClassListByDayAndTaskByDate(day: String, timeStamp: Long): Flow<List<CombinedScheduleTaskModel>> =
        scheduleDao.getClassesWithTasksByDayAndDate(selectedDay = day, selectedDate = timeStamp)

    override suspend fun upsertAttendance(taskId: Int) {
        scheduleDao.upsertAttendance(taskId)
    }
    override suspend fun updateTask(task: String, scheduleId: Int) {
//        scheduleDao.updateTask(task = task, scheduleId = scheduleId)
    }

    override suspend fun upsert(scheduleDao: ScheduleModel) {
        TODO("Not yet implemented")
    }
}