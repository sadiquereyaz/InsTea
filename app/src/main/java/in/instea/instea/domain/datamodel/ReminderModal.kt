package `in`.instea.instea.domain.datamodel

import androidx.annotation.StringRes
import java.util.concurrent.TimeUnit

data class ReminderModal(
    @StringRes val durationRes: Int,
    val duration: Long,
    val unit: TimeUnit,
    val task: String
)
