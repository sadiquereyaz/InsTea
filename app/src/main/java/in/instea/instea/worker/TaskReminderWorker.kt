package `in`.instea.instea.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class TaskReminderWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        //This method is where you put the code for the actual work you want to perform in the background.
        val task = inputData.getString(TASK_KEY)
        makeTaskReminderNotification("Your task is: $task", applicationContext)
        Log.d("WORK_MANAGER", "WorkManager Notification completed")
        return Result.success()
    }

    companion object {
        const val TASK_KEY = "TASK"
    }
}
