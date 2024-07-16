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

    @Query("SELECT * FROM schedule where day = :selectedDay")
    fun getClassByDay(selectedDay: String = "Tue"): Flow<List<ScheduleModel>>

    @Query("UPDATE schedule SET attendance = 'present' WHERE scheduleId = :scheduleId")
    suspend fun updateAttendance(scheduleId: Int)

    @Query("UPDATE schedule SET task = :task WHERE scheduleId = :scheduleId")
    suspend fun updateTask(scheduleId: Int, task: String)

}

//@Query("UPDATE schedule SET attendance = 'present', column2 = value2 WHERE id = target_id")
/*
INSERT INTO schedule (subject, task, attendance, startTime, endTime, day)
VALUES ('Math', 'Solve practice problems', 'Absent', '09:00', '10:00', 'Mon'),
('Science', 'Review for experiment', 'Absent', '10:00', '11:00', 'Tue'),
('English', 'Read assigned chapters', 'Absent', '11:00', '12:00', 'Wed'),
('History', 'Complete research paper', 'Absent', '12:00', '01:00', 'Thu'),
('Art', 'Work on creative project', 'Absent', '01:00', '02:00', 'Fri');
*/
