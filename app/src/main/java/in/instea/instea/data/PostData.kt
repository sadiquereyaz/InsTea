package `in`.instea.instea.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostData(
    @PrimaryKey
    val postId: Int,        //timestamp
    val name: String?,
    val location: String?,
    val profileImage: Int?,
    val postDescription: String?,
    @Ignore val postImage: Int?,
)
