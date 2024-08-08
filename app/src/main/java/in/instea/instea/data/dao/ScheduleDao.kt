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
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM taskAttendance WHERE subjectId = :subjectId AND timestamp = :timestamp")
    suspend fun checkTaskAttendanceRowExistence(
        subjectId: Int,
        timestamp: Int
    ): TaskAttendanceModel?


    @Upsert
    suspend fun upsertSchedule(scheduleModel: ScheduleModel)

    // insert a new task/attendance row
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAttendance(taskAttendanceObj: TaskAttendanceModel): Long

    // update task
    @Query("UPDATE taskAttendance SET task = :task  WHERE subjectId = :scheduleId AND timestamp = :timestamp")
    suspend fun updateTask(task: String, scheduleId: Int, timestamp: Int)

    // update attendance
    @Query("UPDATE taskAttendance SET attendance = :attendance WHERE subjectId = :scheduleId AND timestamp = :timestamp")
    suspend fun updateAttendance(attendance: AttendanceType, scheduleId: Int, timestamp: Int)


    // for conflict checking
    @Query("SELECT * FROM schedule WHERE day = :day ORDER BY startTime")
    suspend fun getAllScheduleByDay(day: String): List<ScheduleModel>

    @Query(
        """
    SELECT s.subjectId, s.startTime, s.endTime, s.day, s.dailyReminder, subj.subject, 
           t.timestamp, t.attendance, t.task, t.taskReminder
        FROM schedule s 
        LEFT JOIN taskAttendance t ON s.subjectId = t.subjectId AND (t.timestamp = :selectedDate OR t.timestamp IS NULL)
    INNER JOIN subject_table subj ON s.subjectId = subj.subjectId
        WHERE s.day = :selectedDay ORDER BY s.startTime
    """
    )
    suspend fun getScheduleAndTaskList(
        selectedDay: String,
        selectedDate: Int
    ): List<CombinedScheduleTaskModel>

    @Query("SELECT * FROM schedule WHERE subjectId = :id")
    suspend fun getScheduleById(id: Int): ScheduleModel

    @Query("DELETE FROM schedule WHERE subjectId = :id")
    suspend fun deleteById(id: Int)

    @Query(
        """
    SELECT 
        subj.subject,
        COUNT(*) AS totalClasses,
        SUM(CASE WHEN t.attendance = 0 THEN 1 ELSE 0 END) AS attendedClasses,
        SUM(CASE WHEN t.attendance = 1 THEN 1 ELSE 0 END) AS absentClasses
    FROM 
        schedule s
    LEFT JOIN 
        taskAttendance t ON s.subjectId = t.subjectId
    INNER JOIN 
        subject_table subj ON s.subjectId = subj.subjectId
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
    suspend fun upsertSchedule(subject: SubjectModel)

    // subject list
    @Query("SELECT * FROM subject_table")
    fun getAllSubjectFlow(): Flow<List<SubjectModel>>

   /* @Query("SELECT DISTINCT subject FROM schedule")
    suspend fun getAllSubject(): List<String>*/
}
