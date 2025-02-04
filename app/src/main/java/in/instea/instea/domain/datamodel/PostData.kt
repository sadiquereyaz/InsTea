package `in`.instea.instea.domain.datamodel

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "posts")
@TypeConverters(StringListConverter::class, DateAndHourConverter::class)
data class PostData(
    @PrimaryKey
    var postid: String = "", // Initialize with an empty string
    var profileImage: Int? = null,
    var postDescription: String? = "",
    @Ignore
    var comments: MutableList<Comments> = mutableListOf(),
    var postImage: Int? = null,
    var postedByUser: String? = "",
    var timestamp: DateAndHour = DateAndHour(),
    var userLikedCurrentPost: MutableList<String?> = mutableListOf(""),
    var userDislikedCurrentPost: MutableList<String?> = mutableListOf(""),
    var saved: Boolean = false,
    @Ignore
    var reports: Reports = Reports(),
    var isAnonymous: Boolean = false,
    var edited: Boolean = false
) {
    constructor() : this("", null, "", mutableListOf(), null, "", DateAndHour(), mutableListOf(), mutableListOf(), false, Reports())
}

data class Reports(var hasReport:Int=0, val reportByUser:MutableList<String> = mutableListOf())

data class Comments(
    var comment:String ="",
    val commentByUser:String="",
    val userLikedComment : MutableList<String> = mutableListOf(),
    val userDislikeComment:MutableList<String> = mutableListOf(),
    val replies:MutableList<Replies> = mutableListOf()

    )

data class Replies(
    val reply:String="",
    val replyByUser:String="",
    val userLikedReply : MutableList<String> = mutableListOf(),
    val userDislikeReply:MutableList<String> = mutableListOf(),
)

@Immutable
data class FeedUiState(
    val posts:List<PostData> = emptyList()
)



class DateAndHourConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): DateAndHour? {
        return value?.let { DateAndHour(Date(it)) }
    }

    @TypeConverter
    fun dateAndHourToTimestamp(dateAndHour: DateAndHour?): Long? {
        return dateAndHour?.toTimestamp()
    }
}
data class DateAndHour(
    var date: Date = Date() // No-argument constructor required by Firebase
) {
    fun format(): String {
        val dateFormat = SimpleDateFormat("dd MMM yy", Locale.getDefault())
        return dateFormat.format(this.date)
    }
    fun formateForMessage(): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(this.date)
    }
    fun toTimestamp(): Long {
        return this.date.time
    }
}
class StringListConverter {
    @TypeConverter
    fun fromString(value: String?): MutableList<String?> {
        return value?.split(",")?.map { it.trim() }?.toMutableList() ?: mutableListOf()
    }

    @TypeConverter
    fun toString(list: MutableList<String?>): String {
        return list.filterNotNull().joinToString(",")
    }
}