package `in`.instea.instea.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DailyClassReminderWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        //This method is where you put the code for the actual work you want to perform in the background.
        val message = inputData.getString(DAILY_CLASS_REMINDER)
        val notificationId:Int = inputData.getInt(DAILY_NOTIFIC_ID,1)
        Log.d("DAILY_NOTIFICATION_WORKER", "notificationId: $notificationId")
        Log.d("DAILY_NOTIFICATION_WORKER", "message: $message")
        makeReminderNotification(
            message = message?:"Message is null",
            context = applicationContext,
            notificationId = notificationId,
            channelId = DAILY_NOTIFICATION_CHANNEL_ID,
            channelName = DAILY_NOTIFICATION_CHANNEL_NAME,
            channelDescription = DAILY_NOTIFICATION_CHANNEL_DESCRIPTION,
            title = DAILY_NOTIFICATION_TITLE
        )
        return Result.success()
    }

    companion object {
        const val DAILY_CLASS_REMINDER = "DAILY_CLASS_REMINDER"
        const val DAILY_NOTIFIC_ID = "DAILY_NOTIFIC_ID"
    }
}
