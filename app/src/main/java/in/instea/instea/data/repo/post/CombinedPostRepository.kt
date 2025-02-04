package `in`.instea.instea.data.repo.post

import `in`.instea.instea.data.local.dao.PostDao
import `in`.instea.instea.domain.datamodel.PostData
import `in`.instea.instea.domain.datamodel.User
import `in`.instea.instea.domain.repo.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class CombinedPostRepository(
    private val localPostRepository: LocalPostRepository,
    private val networkPostRepository: NetworkPostRepository,
) : PostRepository {
    override fun getAllSavedPostsStream(): Flow<List<PostData>> = flow {
        // Emit local data immediately
        emitAll(localPostRepository.getAllSavedPostsStream())

        // Then emit network data and update local database
        networkPostRepository.getAllSavedPostsStream().collect { networkPosts ->
            emit(networkPosts)
            networkPosts.forEach { localPostRepository.insertItem(it) }
        }
    }

    override fun getAllProfilePostsStream(): Flow<List<PostData>> = flow {
        // Then emit network data and update local database
        networkPostRepository.getAllSavedPostsStream().collect { networkPosts ->
            emit(networkPosts)
            networkPosts.forEach { localPostRepository.insertItem(it) }
        }
    }

    override suspend fun insertItem(post: PostData) {
        localPostRepository.insertItem(post)
        try {
            networkPostRepository.insertItem(post)
        } catch (e: Exception) {
            // Handle network insertion failure
        }
    }

    override suspend fun updateUpAndDownVote(post: PostData) {

    }

    override suspend fun updateComment(post: PostData) {
        TODO("Not yet implemented")
    }


    override suspend fun search(query: String, onResult: (List<User>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun Filter(query: String, onResult: (List<PostData>) -> Unit) {
        TODO("Not yet implemented")
    }


    override suspend fun Delete(post: PostData) {
        TODO("Not yet implemented")
    }

    override fun getPostsByUser(userId: String): Flow<List<PostData>> = flow {
        networkPostRepository.getProfilePosts(userId)
    }


}

class LocalPostRepository(
    private val postDao: PostDao,
) : PostRepository {

    override fun getAllSavedPostsStream(): Flow<List<PostData>> = postDao.getAllSavedPosts()
    override fun getAllProfilePostsStream(): Flow<List<PostData>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertItem(post: PostData) = postDao.insertPost(post)

    override suspend fun updateUpAndDownVote(post: PostData) {
        TODO("Not yet implemented")
    }

    override suspend fun updateComment(post: PostData) {

    }

    override fun getPostsByUser(userId: String): Flow<List<PostData>> {
        TODO("Not yet implemented")
    }


    override suspend fun search(query: String, onResult: (List<User>) -> Unit) {
        TODO("Not yet implemented")
    }

    override suspend fun Filter(query: String, onResult: (List<PostData>) -> Unit) {
        TODO("Not yet implemented")
    }


    override suspend fun Delete(post: PostData) {
        TODO("Not yet implemented")
    }


}



