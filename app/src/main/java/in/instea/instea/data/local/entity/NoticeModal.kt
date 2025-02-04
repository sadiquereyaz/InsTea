package `in`.instea.instea.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoticeModal(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val url: String,
    val type: String,
    val timestamp: Long = 0L
)