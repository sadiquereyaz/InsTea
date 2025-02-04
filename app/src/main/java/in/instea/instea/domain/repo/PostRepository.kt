package `in`.instea.instea.domain.repo

import `in`.instea.instea.domain.datamodel.PostData
import `in`.instea.instea.domain.datamodel.User
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getAllSavedPostsStream(): Flow<List<PostData>>
    fun getAllProfilePostsStream(): Flow<List<PostData>>
    suspend fun insertItem(post: PostData)
    suspend fun updateUpAndDownVote(post: PostData)
    suspend fun updateComment(post: PostData)
    fun getPostsByUser(userId: String): Flow<List<PostData>>
    suspend fun Delete(post: PostData)
    suspend fun search(query: String, onResult: (List<User>) -> Unit)
    suspend fun Filter(query: String, onResult: (List<PostData>) -> Unit)

}