package `in`.instea.instea.data.datamodel

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

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

@Entity(tableName = "posts")
@TypeConverters(StringListConverter::class)
data class PostData(
    @PrimaryKey
    var postid: String ,
    val department: String? = null,
    val profileImage: Int? = null,
    val postDescription: String? = null,
    val postImage: Int? = null,
    val postedByUser: String? = null,
    val userLikedCurrentPost: MutableList<String?> = mutableListOf(),
    val userDislikedCurrentPost: MutableList<String?> = mutableListOf(),
    val hasReports: Int = 0
)

@Immutable
data class FeedUiState(
    val posts: List<PostData> = emptyList()
)
