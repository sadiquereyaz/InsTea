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
    suspend fun upsertAttendance(attendance: String, taskId: Int)

    @Query("UPDATE taskAttendance SET attendance = 'present' WHERE taskId = :taskId")
    suspend fun upsertAttendance(taskId: Int)

//    @Query("SELECT * FROM tasks WHERE scheduleId IN (SELECT id FROM schedules WHERE day = :selectedDay) AND date = :selectedDate")
//    fun getTasksByDayAndDate(selectedDay: String, selectedDate: Long): Flow<List<TaskModel>>

    @Query(
        """
        SELECT s.scheduleId, s.startTime, s.endTime, s.day, s.dailyReminder, s.subject,
               t.taskId, t.timestamp, t.attendance, t.task, t.taskReminder
        FROM schedule s 
        LEFT JOIN taskAttendance t ON s.scheduleId = t.scheduleId AND (t.timestamp = :selectedDate OR t.timestamp IS NULL)
        WHERE s.day = :selectedDay
    """
    )
    fun getClassesWithTasksByDayAndDate(
        selectedDay: String,
        selectedDate: Long
    ): Flow<List<CombinedScheduleTaskModel>>
}

/*
* INSERT INTO schedule (subject, startTime, endTime, day, dailyReminder)
VALUES ('Urdu', '4', '5', 'Wed', 0),
('Math', '8', '9', 'Mon', 1),
('Science', '9', '10', 'Tue', 0),
('English', '10', '11', 'Wed', 1),
('History', '11', '12', 'Thu', 0),
('Art', '14', '15', 'Mon', 0),
('Music', '15', '16', 'Tue', 1),
('PE', '16', '17', 'Wed', 0),
('Foreign Language', '17', '18', 'Thu', 1),
('Computer Science', '18', '19', 'Fri', 0),
('Elective', '19', '20', 'Fri', 1);
*
*
* INSERT INTO taskAttendance (scheduleId, timestamp, attendance, task, taskReminder)
VALUES
(1, 240716, 'Absent', 'Project Meeting Preparation', 1),
(2, 240717, 'Present', 'Quiz Review', 0),
(3, 240717, 'Present', 'Lab Experiment', 1),
(4, 240724, 'Present', 'Class Discussion', 0),
(5, 240722, 'Present', 'Class Discussion', 0),
(5, 240715, 'Present', 'Class Discussion', 0),
(12, 240716, 'Absent', 'Presentation Practice', 1),
(6, 240716, 'Present', 'Field Trip Permission Slip', 1),  -- New schedule with attendance and task
(21, 240721, 'Present', 'Group Project Meeting', 0),  -- New schedule with attendance and task
(20, 240720, 'Absent', 'Midterm Exam Preparation', 1),  -- Existing schedule with new attendance and task
(19, 240719, 'Present', 'Book Report Due', 0),
(22, 240722, 'Present', 'Movie Screening Permission Slip', 1);
*/