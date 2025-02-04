package `in`.instea.instea.data.worker

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
        val taskWithDetail = inputData.getString(TASK_KEY)
        val notificationId:Int = inputData.getInt(NOTIFIC_ID,0)
        Log.d("TASK_REMINDER_WORKER", "notificationId: $notificationId")
        makeReminderNotification(
            message = taskWithDetail?:"Task is null",
            context = applicationContext,
            notificationId = notificationId ,
            channelId = TASK_NOTIFICATION_CHANNEL_ID,
            channelName = TASK_NOTIFICATION_CHANNEL_NAME,
            channelDescription = TASK_NOTIFICATION_CHANNEL_DESCRIPTION,
            title = TASK_NOTIFICATION_TITLE
        )
//        Log.d("WORK_MANAGER", "WorkManager Notification completed")
        return Result.success()
    }

    companion object {
        const val TASK_KEY = "TASK"
        const val NOTIFIC_ID = "NOTIFIC_ID"
    }
}
