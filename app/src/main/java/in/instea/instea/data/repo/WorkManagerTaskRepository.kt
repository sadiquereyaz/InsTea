package `in`.instea.instea.data.repo

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.worker.TaskReminderWorker
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

interface TaskReminderRepository {
    fun scheduleTaskReminder(
        task: String,
        remindBefore: Long,
        unit: TimeUnit,
        taskKey: String,
        scheduleObj: CombinedScheduleTaskModel
    )

    fun cancelReminder(uniqueWorkName: String)
}

class WorkManagerTaskRepository(context: Context) : TaskReminderRepository {
    /*WorkManager: This class actually schedules your WorkRequest and makes it run.
It schedules a WorkRequest in a way that spreads out the load on system resources, while honoring the constraints you specify.*/
    private val workManager = WorkManager.getInstance(context)
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
        Log.d("WORK_MANAGER", dateTime.toString())


        val reminderTime = dateTime.minusHours(remindBefore)
        val currentTime = LocalDateTime.now()
        // Calculate the delay between the current time and the reminder time
        val delay = Duration.between(currentTime, reminderTime).toMillis()
//        val delay = 2000L
        Log.d("WORK_MANAGER", "delay: $delay milli sec")
        Log.d("WORK_MANAGER", "delay: ${delay / (3600*1000)} hour")

        // If the delay is negative, the reminder time is in the past, and you should not schedule it
        if (delay <= 0) {
            Log.d("WORK_MANAGER", "Delay is negative")
            return
        }

        val data = Data.Builder()
        data.putString(TaskReminderWorker.TASK_KEY, task)

        // constraint TODO: remove
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // WorkRequest
        val reminderBuilder = OneTimeWorkRequestBuilder<TaskReminderWorker>()
        reminderBuilder.setInputData(data.build())
        reminderBuilder.setConstraints(constraints)
        reminderBuilder.setInitialDelay(delay, TimeUnit.MILLISECONDS)
        Log.d("WORK_MANAGER", "WorkManager task scheduled")
        // Actually start the work
        workManager.enqueueUniqueWork(taskKey, ExistingWorkPolicy.REPLACE, reminderBuilder.build())
    }

    override fun cancelReminder(uniqueWorkName: String) {
        workManager.cancelUniqueWork(uniqueWorkName)
    }
}