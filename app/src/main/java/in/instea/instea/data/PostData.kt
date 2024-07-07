package `in`.instea.instea.data

import androidx.compose.runtime.Immutable

@Immutable
data class PostData(
    val name: String?=null,
    val department: String?=null,
    val profileImage: Int?=null,
    val postDescription: String?=null,
    val postImage: Int?=null,
    val postUid:String?=null
)

data class FeedUiState(
    val posts: List<PostData> = emptyList()

)