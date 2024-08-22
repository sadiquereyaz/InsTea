package `in`.instea.instea.data.repo

import NotificationConstant
import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.worker.DailyClassReminderWorker
import `in`.instea.instea.worker.TaskReminderWorker
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

interface TaskReminderRepository {
    fun scheduleDailyClassReminder(
        scheduleObj: CombinedScheduleTaskModel, time: LocalTime
    )

    fun scheduleTaskReminder(
        task: String,
        remindBefore: Long,
        unit: TimeUnit,
        taskKey: String,
        scheduleObj: CombinedScheduleTaskModel
    )

    fun cancelScheduledWork(uniqueWorkName: String)
}

class WorkManagerTaskRepository(context: Context) : TaskReminderRepository {
    /*WorkManager: This class actually schedules your WorkRequest and makes it run.
It schedules a WorkRequest in a way that spreads out the load on system resources, while honoring the constraints you specify.*/
    private val workManager = WorkManager.getInstance(context)

    override fun scheduleDailyClassReminder(
        scheduleObj: CombinedScheduleTaskModel,
        time: LocalTime
    ) {
        val dayString = scheduleObj.day
        val dayOfWeek = when (dayString) {
            "Mon" -> DayOfWeek.MONDAY
            "Tue" -> DayOfWeek.TUESDAY
            "Wed" -> DayOfWeek.WEDNESDAY
            "Thu" -> DayOfWeek.THURSDAY
            "Fri" -> DayOfWeek.FRIDAY
            "Sat" -> DayOfWeek.SATURDAY
            "Sun" -> DayOfWeek.SUNDAY
            else -> throw IllegalArgumentException("error in dayof weeek")
        }
        val reminderDay = dayOfWeek.minus(1) // A day before the scheduled day

        val now = LocalDateTime.now()
        val targetTime = now.with(reminderDay).with(time)

        val initialDelay = if (targetTime.isAfter(now)) {
            Duration.between(now, targetTime).toMillis()
        } else {
            Duration.between(now, targetTime.plusWeeks(1)).toMillis()
        }
        val data = Data.Builder()
        data.putString(
            DailyClassReminderWorker.DAILY_CLASS_REMINDER,
            "Tomorrow is ${scheduleObj.subject} class. Good Luck :-)"
        )
        data.putInt(DailyClassReminderWorker.DAILY_NOTIFIC_ID, scheduleObj.scheduleId)
        val weeklyWorkRequest =
            PeriodicWorkRequestBuilder<DailyClassReminderWorker>(7, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .setInputData(data.build())
                .build()
        workManager.enqueueUniquePeriodicWork(
            NotificationConstant.getDailyClassReminderKey(scheduleId = scheduleObj.scheduleId),
            ExistingPeriodicWorkPolicy.UPDATE,
            weeklyWorkRequest
        )
    }

    override fun scheduleTaskReminder(
        task: String,
        remindBefore: Long,
        unit: TimeUnit,
        taskKey: String,
        scheduleObj: CombinedScheduleTaskModel
    ) {
        // duration calculation
        val dateInt: Int = scheduleObj.timestamp ?: 210101
        val year = (dateInt) / 10000 + 2000
        val month = (dateInt) / 100 % 100
        val day = (dateInt % 100)
        val time: LocalTime = scheduleObj.startTime
        val date = LocalDate.of(year, month, day)
        val dateTime = LocalDateTime.of(date, time)

        val reminderTime = dateTime.minusHours(remindBefore)
        val currentTime = LocalDateTime.now()
        // Calculate the delay between the current time and the reminder time
        val delay = Duration.between(currentTime, reminderTime).toMillis()

        // If the delay is negative, the reminder time is in the past, and you should not schedule it
        if (delay <= 0) {
            return
        }

        val data = Data.Builder()
        data.putString(
            TaskReminderWorker.TASK_KEY,
            "$task due date: $day/$month/$year of ${scheduleObj.subject}"
        )
        data.putInt(
            TaskReminderWorker.NOTIFIC_ID,
            NotificationConstant.getTaskNotificationId(
                scheduleId = scheduleObj.scheduleId,
                timeStamp = scheduleObj.timestamp ?: 0,
                subjectId = scheduleObj.subjectId
            )
        )
        // constraint TODO: useful
        /*  val constraints = Constraints.Builder()
              .setRequiredNetworkType(NetworkType.CONNECTED)
              .build()*/

        // WorkRequest
        val reminderBuilder = OneTimeWorkRequestBuilder<TaskReminderWorker>()
        reminderBuilder.setInitialDelay(delay, TimeUnit.MILLISECONDS)
        reminderBuilder.setInputData(data.build())
//        reminderBuilder.setConstraints(constraints)       //TODO: useful
        Log.d("WORK_MANAGER", "WorkManager task scheduled")
        // Actually start the work
        workManager.enqueueUniqueWork(taskKey, ExistingWorkPolicy.REPLACE, reminderBuilder.build())
    }


    override fun cancelScheduledWork(uniqueWorkName: String) {
        workManager.cancelUniqueWork(uniqueWorkName)
    }
}

fun convertTimeStampToLocalDateTime(dateInt: Int, time: LocalTime): LocalDateTime {
    val year = (dateInt) / 10000 + 2000
    val month = (dateInt) / 100 % 100
    val day = (dateInt % 100)
    val date = LocalDate.of(year, month, day)
    return LocalDateTime.of(date, time)
}