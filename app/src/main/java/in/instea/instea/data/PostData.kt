package `in`.instea.instea.data

import androidx.compose.runtime.Immutable

@Immutable
data class PostData(
    val name: String? = null,
    val department: String? = null,
    val profileImage: Int? = null,
    var postid: String? = null,
    val postDescription: String? = null,
    val postImage: Int? = null,
    val postedByUser: String? = null,
    val upVote: upVotes = upVotes(),
    val downVote: downVotes = downVotes(),
    val hasReports:Int=0
)

@Immutable
data class FeedUiState(
    val posts: List<PostData> = emptyList()
)

@Immutable
data class upVotes(
    val like: Int = 0,
    val userLikedCurrentPost: MutableList<String?> = mutableListOf("")
)

@Immutable
data class downVotes(
    val dislike: Int = 0,
    val userDislikedCurrentPost: MutableList<String?> = mutableListOf("")
)
