package `in`.instea.instea.data.repo.post

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.domain.datamodel.PostData
import `in`.instea.instea.domain.datamodel.User
import `in`.instea.instea.domain.repo.PostRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkPostRepository(
    firebaseDatabase: FirebaseDatabase,
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
                }
                Log.d("NetworkPostRepository", "Total posts fetched: ${postList.size}")
                trySend(postList).isSuccess
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("NetworkPostRepository", "DatabaseError: ${databaseError.message}")
                close(databaseError.toException())
            }
        }
        databaseReference.addValueEventListener(postListener)
        awaitClose { databaseReference.removeEventListener(postListener) }
    }

    override fun getAllProfilePostsStream(): Flow<List<PostData>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertItem(post: PostData) {
        val newPostRef = databaseReference.push()
        post.postid = newPostRef.key.toString()
        newPostRef.setValue(post).await()
        Log.d("NetworkPostRepository", "Inserted post with ID: ${post.postid}")
    }

    override suspend fun updateUpAndDownVote(post: PostData) {
        val db = Firebase.database.reference

        val dataSnapshot = db.child("posts").get();
        for (postSnapshot in dataSnapshot.await().children) {
            val currentPost = postSnapshot.getValue(PostData::class.java)
            if (post.postid.equals(currentPost?.postid)) {

                db
                    .child("posts")
                    .child(post.postid!!)
                    .setValue(post)

            }

        }
    }

    override suspend fun updateComment(post: PostData) {

        val db = Firebase
            .database
            .reference

        db.child("posts")
            .child(post.postid)
            .setValue(post)


    }

    override fun getPostsByUser(userId: String): Flow<List<PostData>> {
        TODO("Not yet implemented")
    }


    override suspend fun search(query: String, onResult: (List<User>) -> Unit) {
        val users = mutableListOf<User>()
        val db = Firebase.database.reference
        val dataSnapshot = db.child("user").get().await()
        for (userSnapshot in dataSnapshot.children) {
            val userId = userSnapshot.key ?: continue

            val username = userSnapshot.child("username").getValue(String::class.java) ?: ""
            val university = userSnapshot.child("university").getValue(String::class.java) ?: ""
            val department = userSnapshot.child("dept").getValue(String::class.java) ?: ""
            val sem = userSnapshot.child("sem").getValue(String::class.java) ?: ""
            if (username.contains(query, ignoreCase = true) ||
                university.contains(query, ignoreCase = true) ||
                department.contains(query, ignoreCase = true) ||
                sem.contains(query, ignoreCase = true)
            ) {

                users.add(
                    User(
                        userId = userId,
                        university = university,
                        username = username,
                        dept = department,
                        sem = sem
                    )
                )

            }
        }
        onResult(users)


    }

    override suspend fun Filter(query: String, onResult: (List<PostData>) -> Unit) {
//        val posts = mutableListOf<PostData>()
//        val db = Firebase.database.reference
//        val dataSnapshot = db.child("posts").get().await()
//        for (postSnapshot in dataSnapshot.children) {
//            val post = postSnapshot.getValue(PostData::class.java)
//            if(post)
//
//        }
//        onResult(posts)

    }


    override suspend fun Delete(post: PostData) {
        try {
            val db = Firebase.database.reference
            val dataSnapshot = db.child("posts").get().await()
            for (postSnapshot in dataSnapshot.children) {
                val currentPost = postSnapshot.getValue(PostData::class.java)
                if (post.postid == currentPost?.postid) {
                    postSnapshot.ref.removeValue().await()
                    Log.d("DeletePost", "Post deleted successfully: ${post.postid}")
                    break
                }
            }
        } catch (e: Exception) {
            Log.e("DeletePost", "Error deleting post: ${e.message}")
        }
    }

    fun getProfilePosts(userId: String) {
        // todo: fetch from firebase
    }
}