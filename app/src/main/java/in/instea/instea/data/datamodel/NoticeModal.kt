package `in`.instea.instea.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class NoticeModal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val url: String,
    val type: String,
    val timestamp: Long = 0L
)