package `in`.instea.instea.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

interface PostRepository {
    fun getAllSavedPostsStream(): Flow<List<PostData>>
    suspend fun insertItem(post: PostData)
}

class CombinedPostRepository(
    private val localPostRepository: LocalPostRepository,
    private val networkPostRepository: NetworkPostRepository
):PostRepository{
    override fun getAllSavedPostsStream(): Flow<List<PostData>> = flow {
        // Emit local data immediately
        emitAll(localPostRepository.getAllSavedPostsStream())
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
}

class LocalPostRepository(
    private val postDao: PostDao
) : PostRepository {
    override fun getAllSavedPostsStream(): Flow<List<PostData>> = postDao.getAllSavedPosts()
    override suspend fun insertItem(post: PostData) = postDao.insertPost(post)
}

class NetworkPostRepository(
    private val firebaseDatabase: FirebaseDatabase
) : PostRepository {
    private val databaseReference = firebaseDatabase.getReference("posts")

    override fun getAllSavedPostsStream(): Flow<List<PostData>> = callbackFlow {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postList = mutableListOf<PostData>()
                dataSnapshot.children.forEach { snapshot ->
                    val post = snapshot.getValue(PostData::class.java)
                    post?.let { postList.add(it) }
                }
                trySend(postList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                close(databaseError.toException())
            }
        }
        databaseReference.addValueEventListener(postListener)

        awaitClose {
            databaseReference.removeEventListener(postListener)
        }
    }

    override suspend fun insertItem(post: PostData) {
        val newPostRef = databaseReference.push()
        newPostRef.setValue(post).await()
    }
}

