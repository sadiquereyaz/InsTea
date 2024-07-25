package `in`.instea.instea.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.ScheduleModel
import `in`.instea.instea.data.datamodel.TaskAttendanceModel

@Dao
interface ScheduleDao {
//    @Insert
//    suspend fun insertSchedule(scheduleModel: ScheduleModel): Long

//    @Query("UPDATE schedule SET subject = :subject, startTime = :startTime, endTime = :endTime, day= :day WHERE scheduleId = :scheduleId")
//    suspend fun updateSchedule(
//        subject: String,
//        scheduleId: Int,
//        startTime: LocalTime,
//        endTime: LocalTime,
//        day: String
//    )

    @Query("SELECT * FROM taskAttendance WHERE scheduleId = :scheduleId AND timestamp = :timestamp")
    suspend fun checkTaskAttendanceRowExistence(scheduleId: Int, timestamp: Int): TaskAttendanceModel?


    @Upsert
    suspend fun upsertSchedule(scheduleModel: ScheduleModel)

    // insert a new task/attendance row
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskAttendance(taskAttendanceObj: TaskAttendanceModel): Long
    // update task
    @Query("UPDATE taskAttendance SET task = :task  WHERE scheduleId = :scheduleId AND timestamp = :timestamp")
    suspend fun updateTask(task: String, scheduleId: Int, timestamp: Int)
    // update attendance
    @Query("UPDATE taskAttendance SET attendance = :attendance WHERE scheduleId = :scheduleId AND timestamp = :timestamp")
    suspend fun updateAttendance(attendance: AttendanceType, scheduleId: Int, timestamp: Int)

    // subject list
    @Query("SELECT DISTINCT subject FROM schedule")
    suspend fun getAllSubject(): List<String>

    // for conflict checking
    @Query("SELECT * FROM schedule WHERE day = :day ORDER BY startTime")
    suspend fun getAllScheduleByDay(day: String): List<ScheduleModel>


    @Query(
        """
        SELECT s.scheduleId, s.startTime, s.endTime, s.day, s.dailyReminder, s.subject, t.timestamp, t.attendance, t.task, t.taskReminder
        FROM schedule s 
        LEFT JOIN taskAttendance t ON s.scheduleId = t.scheduleId AND (t.timestamp = :selectedDate OR t.timestamp IS NULL)
        WHERE s.day = :selectedDay ORDER BY s.startTime
    """
    )
    suspend fun getScheduleAndTaskList(selectedDay: String, selectedDate: Int): List<CombinedScheduleTaskModel>

    @Query("SELECT * FROM schedule WHERE scheduleId = :id")
    suspend fun getScheduleById(id: Int): ScheduleModel

    @Query("DELETE FROM schedule WHERE scheduleId = :id")
    suspend fun deleteById(id: Int)
}
