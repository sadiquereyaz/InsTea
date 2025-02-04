package `in`.instea.instea.domain.repo

import `in`.instea.instea.domain.datamodel.CombinedScheduleTaskModel
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