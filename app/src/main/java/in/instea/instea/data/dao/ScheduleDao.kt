package `in`.instea.instea.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import `in`.instea.instea.data.datamodel.ScheduleModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Upsert
    suspend fun upsert(scheduleModel: ScheduleModel)

    @Query("SELECT * FROM schedule")
    fun getAllClasses(): Flow<List<ScheduleModel>>

    @Query("UPDATE schedule SET attendance = 'present' WHERE scheduleId = :scheduleId")
    suspend fun updateAttendance(scheduleId: Int)

    @Query("UPDATE schedule SET task = :task WHERE scheduleId = :scheduleId")
    suspend fun updateTask(scheduleId: Int, task: String)

}

//@Query("UPDATE schedule SET attendance = 'present', column2 = value2 WHERE id = target_id")
//INSERT INTO class (subject, task, attendance) VALUES ('Mathematics', 'Complete Chapter 1 Homework', 'absent'),('Science', 'Prepare for Lab Experiment', 'Absent'), ('History', 'Read Chapter 3', 'absent'),('Geography', 'Complete Map Assignment', 'absent'),('English', 'Submit Essay', 'absent');