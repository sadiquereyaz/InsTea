package `in`.instea.instea.data

import android.location.Location
import androidx.compose.runtime.Immutable

@Immutable
data class PostData(
    val name: String?=null,
    val location: String?=null,
    val profileImage: Int?=null,
    val postDescription: String?=null,
    val postImage: Int?=null
)

data class FeedUiState(
    val posts: List<PostData> = emptyList()

)