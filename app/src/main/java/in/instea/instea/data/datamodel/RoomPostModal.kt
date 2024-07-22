package `in`.instea.instea.data.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "posts")
@TypeConverters(StringListConverter::class)
class RoomPostModal(
    @PrimaryKey
    var postid: String = "", // Initialize with an empty string
    val department: String? = null,
    val profileImage: Int? = null,
    val postDescription: String? = "",
    val postImage: Int? = null,
    val postedByUser: String? = "",
    val userLikedCurrentPost: MutableList<String?> = mutableListOf(""),
    val userDislikedCurrentPost: MutableList<String?> = mutableListOf("")
)