package `in`.instea.instea.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.datamodel.TaskAttendanceModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Upsert
    suspend fun upsert(scheduleModel: ScheduleModel)

    @Query("SELECT * FROM schedule")
    fun getAllClasses(): Flow<List<ScheduleModel>>

    @Query("SELECT * FROM schedule where day = :selectedDay")
    fun getClassByDay(selectedDay: String = "Tue"): Flow<List<ScheduleModel>>



    @Query("SELECT * FROM taskAttendance WHERE timestamp = :timestamp")
    fun getTaskAttendanceByTimestamp(timestamp: Long): Flow<List<TaskAttendanceModel>>

    @Query("Update taskAttendance SET task = :task WHERE taskId = :taskId")
    suspend fun addOrUpdateTask(task: String, taskId: Int)

    @Query("Update taskAttendance SET attendance = :attendance WHERE taskId = :taskId")
    suspend fun updateAttendance(attendance: String, taskId: Int)

    @Query("UPDATE taskAttendance SET attendance = 'present' WHERE scheduleId = :scheduleId")
    suspend fun updateAttendance(scheduleId: Int)

//    @Query("SELECT * FROM tasks WHERE scheduleId IN (SELECT id FROM schedules WHERE day = :selectedDay) AND date = :selectedDate")
//    fun getTasksByDayAndDate(selectedDay: String, selectedDate: Long): Flow<List<TaskModel>>

    @Query(
        """
        SELECT s.scheduleId, s.startTime, s.endTime, s.day, s.dailyReminder, s.subject,
               t.taskId, t.timestamp, t.attendance, t.task, t.taskReminder
        FROM schedule s
        LEFT JOIN taskAttendance t ON s.scheduleId = t.scheduleId
        WHERE s.day = :selectedDay AND t.timestamp = :selectedDate
    """
    )
    fun getClassesWithTasksByDayAndDate(
        selectedDay: String,
        selectedDate: Long
    ): Flow<List<CombinedScheduleTaskModel>>
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
