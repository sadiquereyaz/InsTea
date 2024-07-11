package `in`.instea.instea.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostData(
    @PrimaryKey(autoGenerate  = true)
    val postId: Int=1,        //timestamp
    val name: String,
    val location: String,
    val profileImage: Int,
    val postDescription: String
)

