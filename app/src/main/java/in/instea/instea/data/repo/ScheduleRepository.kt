package `in`.instea.instea.data.repo

import `in`.instea.instea.data.dao.ScheduleDao
import `in`.instea.instea.data.datamodel.ScheduleModel
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
    fun getClassList(): Flow<List<ScheduleModel>>
    suspend fun updateAttendance(scheduleId: Int)
    suspend fun updateTask(task: String, scheduleId: Int)
    suspend fun upsert(scheduleDao: ScheduleModel)
}

class LocalScheduleRepository(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {
    override fun getClassList(): Flow<List<ScheduleModel>> = scheduleDao.getAllClasses()
    override suspend fun updateAttendance(scheduleId: Int) {
        scheduleDao.updateAttendance(scheduleId)
    }

    override suspend fun updateTask(task: String, scheduleId: Int) {
        scheduleDao.updateTask(task = task, scheduleId = scheduleId)
    }

    override suspend fun upsert(scheduleDao: ScheduleModel) {
        TODO("Not yet implemented")
    }
}