package `in`.instea.instea.data.repo

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import `in`.instea.instea.worker.TaskReminderWorker
import java.util.concurrent.TimeUnit

interface TaskReminderRepository {
    fun scheduleTaskReminder(task: String, duration: Long, unit: TimeUnit, taskKey: String)
    fun cancelReminder(uniqueWorkName: String)
}

class WorkManagerTaskRepository(context: Context) : TaskReminderRepository {
    /*WorkManager: This class actually schedules your WorkRequest and makes it run.
It schedules a WorkRequest in a way that spreads out the load on system resources, while honoring the constraints you specify.*/
    private val workManager = WorkManager.getInstance(context)
    override fun scheduleTaskReminder(task: String, duration: Long, unit: TimeUnit, taskKey: String) {
        val data = Data.Builder()
        data.putString(TaskReminderWorker.TASK_KEY, task)
        // constraint
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // WorkRequest
        val reminderBuilder = OneTimeWorkRequestBuilder<TaskReminderWorker>()
        reminderBuilder.setInputData(data.build())
        reminderBuilder.setConstraints(constraints)
        reminderBuilder.setInitialDelay(duration, unit)
        // Actually start the work
        workManager.enqueueUniqueWork(taskKey,ExistingWorkPolicy.REPLACE, reminderBuilder.build())
    }

    override fun cancelReminder(uniqueWorkName: String) {
        workManager.cancelUniqueWork(uniqueWorkName)
    }
}