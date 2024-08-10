package `in`.instea.instea.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.datamodel.SubjectAttendanceSummaryModel
import `in`.instea.instea.data.datamodel.SubjectModel
import `in`.instea.instea.data.datamodel.TaskAttendanceModel
import `in`.instea.instea.screens.more.composable.TaskModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM taskAttendance WHERE scheduleId = :scheduleId AND subjectId = :subjectId AND timestamp = :timestamp")
    suspend fun checkTaskAttendanceRowExistence(
        scheduleId: Int,
        subjectId: Int,
        timestamp: Int
    ): TaskAttendanceModel?

    @Upsert
    suspend fun upsertSchedule(scheduleModel: ScheduleModel)

    // insert a new task/attendance row
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAttendance(taskAttendanceObj: TaskAttendanceModel): Long
    // update task
    @Query("UPDATE taskAttendance SET task = :task  WHERE scheduleId = :scheduleId AND timestamp = :timestamp AND subjectId = :subjectId")
    suspend fun updateTask(
        task: String?,
        scheduleId: Int,
        subjectId: Int,
        timestamp: Int
    )
    // update attendance
    @Query("UPDATE taskAttendance SET attendance = :attendance WHERE scheduleId = :scheduleId AND subjectId = :subjectId AND timestamp = :timestamp")
    suspend fun updateAttendance(attendance: AttendanceType, scheduleId: Int, subjectId: Int, timestamp: Int)

    // for conflict checking
    @Query("SELECT * FROM schedule WHERE day = :day ORDER BY startTime")
    suspend fun getAllScheduleByDay(day: String): List<ScheduleModel>

    @Query(
        """
    SELECT s.scheduleId, s.subjectId, s.startTime, s.endTime, s.day, s.dailyReminder, s.subject, 
           t.timestamp, t.attendance, t.task, t.taskReminder
        FROM schedule s 
        LEFT JOIN taskAttendance t ON s.scheduleId = t.scheduleId AND s.subjectId = t.subjectId AND (t.timestamp = :selectedDate OR t.timestamp IS NULL)
        WHERE s.day = :selectedDay ORDER BY s.startTime
    """
    )
    suspend fun getScheduleAndTaskList(selectedDay: String, selectedDate: Int): List<CombinedScheduleTaskModel>

    @Query("SELECT task, timestamp, scheduleId, subjectId FROM taskAttendance Where task is NOT NULL ORDER BY timestamp")
    suspend fun getAllTask():List<TaskModel>

    @Query("UPDATE taskAttendance SET task = NULL WHERE timestamp = :timestamp AND scheduleId = :scheduleId AND subjectId = :subjectId")
    suspend fun clearTask(scheduleId: Int, subjectId: Int,timestamp: Int)

    @Query("SELECT * FROM schedule WHERE scheduleId = :scheduleId")
    suspend fun getScheduleById(scheduleId: Int): ScheduleModel

    @Query("DELETE FROM schedule WHERE scheduleId = :id AND day =:day")
    suspend fun deleteScheduleById(id: Int, day: String)

   @Query(
       """
    SELECT 
        subj.subject AS subject,
        COUNT(t.attendance) AS totalClasses,
        SUM(CASE WHEN t.attendance = 0 THEN 1 ELSE 0 END) AS attendedClasses,
        SUM(CASE WHEN t.attendance = 1 THEN 1 ELSE 0 END) AS absentClasses
    FROM 
        taskAttendance t
    INNER JOIN 
        subject_table subj ON t.subjectId = subj.subjectId
    WHERE 
        t.timestamp BETWEEN :startOfTimestamp AND :endOfTimestamp
    GROUP BY 
        subj.subject
    """
   )
   suspend fun getSubjectAttendanceSummary(
       startOfTimestamp: Int,
       endOfTimestamp: Int = startOfTimestamp + 32
   ): List<SubjectAttendanceSummaryModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSubject(subject: SubjectModel): Long

    // subject list
    @Query("SELECT * FROM subject_table")
    fun getAllSubjectFlow(): Flow<List<SubjectModel>>

   /* @Query("SELECT DISTINCT subject FROM schedule")
    suspend fun getAllSubject(): List<String>*/
}
