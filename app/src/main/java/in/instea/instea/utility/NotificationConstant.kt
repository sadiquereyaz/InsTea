object NotificationConstant{
    fun getTaskReminderKey(scheduleId: Int, subjectId:Int, timeStamp:Int): String = "task$scheduleId$subjectId$timeStamp"
    fun getDailyClassReminderKey(scheduleId: Int): String = "dailyClass$scheduleId"
    fun getTaskNotificationId(scheduleId: Int, timeStamp: Int, subjectId: Int): Int =
        "1${timeStamp}${scheduleId}${subjectId}".toInt()
}