package `in`.instea.instea.data.repo

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import `in`.instea.instea.data.dao.PostDao
import `in`.instea.instea.data.datamodel.PostData
//import `in`.instea.instea.data.datamodel.RoomPostModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlin.math.log

interface PostRepository {
    fun getAllSavedPostsStream(): Flow<List<PostData>>
    suspend fun insertItem(post: PostData)

}

class CombinedPostRepository(
    private val localPostRepository: LocalPostRepository,
    private val networkPostRepository: NetworkPostRepository
): PostRepository {
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
    firebaseDatabase: FirebaseDatabase
) : PostRepository {
    private val databaseReference = firebaseDatabase.reference.child("posts")

    override fun getAllSavedPostsStream(): Flow<List<PostData>> = callbackFlow {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postList = mutableListOf<PostData>()
                for (postSnapshot in dataSnapshot.children) {
                    val post = postSnapshot.getValue(PostData::class.java)
                    if (post != null) {
                        postList.add(post)
                    }
                    Log.d("NetworkPostRepository", "Fetched post: $post")
                }
                Log.d("NetworkPostRepository", "Total posts fetched: ${postList.size}")
                trySend(postList)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("NetworkPostRepository", "DatabaseError: ${databaseError.message}")
                close(databaseError.toException())
            }
        }
        databaseReference.addValueEventListener(postListener)

        awaitClose {
            Log.d("NetworkPostRepository", "Removing event listener")
            databaseReference.removeEventListener(postListener)
        }
    }

    override suspend fun insertItem(post: PostData) {
        val newPostRef = databaseReference.push()
        post.postid = newPostRef.key.toString()
        newPostRef.setValue(post).await()
        Log.d("NetworkPostRepository", "Inserted post with ID: ${post.postid}")
    }
}
