package `in`.instea.instea.data.repo

import `in`.instea.instea.data.dao.ScheduleDao
import `in`.instea.instea.data.datamodel.ScheduleModel
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {
     fun getClassList(): Flow<List<ScheduleModel>>
}

class LocalScheduleRepository(
    private val scheduleDao: ScheduleDao
) : ScheduleRepository {
    override fun getClassList(): Flow<List<ScheduleModel>> = scheduleDao.getAllClasses()
}