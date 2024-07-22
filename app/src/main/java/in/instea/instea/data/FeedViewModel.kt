package `in`.instea.instea.data


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.data.EmptyGroup.location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FederatedAuthProvider
import com.google.firebase.auth.auth

import com.google.firebase.database.database
import `in`.instea.instea.data.datamodel.FeedUiState
import `in`.instea.instea.data.datamodel.PostData

import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.LocalPostRepository
import `in`.instea.instea.data.repo.NetworkPostRepository
import `in`.instea.instea.data.repo.PostRepository
import `in`.instea.instea.data.viewmodel.NetworkUtils
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FeedViewModel(
    private val postRepository: NetworkPostRepository,
    private val localPostRepository: LocalPostRepository,
    val context: Context
) : ViewModel() {
    private val mAuth = Firebase.auth
    val db = Firebase.database.reference
    val currentuser = mAuth.currentUser?.uid
    lateinit var fetchedUsers : List<User>
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    // private val chatUiState = MutableStateFlow(PostData())
//    val _uistate: StateFlow<PostData> = chatUiState.asStateFlow()
//    private val _feedUiState = MutableStateFlow(FeedUiState())
//    val feeduiState:StateFlow<FeedUiState> =
    private val _posts = MutableStateFlow<List<PostData>>(emptyList())
    val posts: StateFlow<List<PostData>> get() = _posts

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading


//    var feedUiState: StateFlow<FeedUiState> = postRepository.getAllSavedPostsStream().map { posts ->
//        FeedUiState(posts = (posts))
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//        initialValue = FeedUiState()
//    )



    init {
        viewModelScope.launch {
           fetchPosts()
             fetchedUsers = fetchUserData()

            val currentUser = fetchedUsers.find { it.email == mAuth.currentUser?.email }

            if (currentUser != null) {

                _user.update { currentState ->
                    currentState.copy(
                        email = currentUser.email,
                        username = currentUser.username,
                        university = currentUser.university,
                        dept = currentUser.dept,
                        sem = currentUser.sem
                    )
                }
            } else {
                Log.d("1234", "margaye")
            }


        }


    }

    private fun fetchPosts() {
        viewModelScope.launch {

            if(NetworkUtils.isNetworkAvailable(context)){
                postRepository.getAllSavedPostsStream().collect { posts ->
                    _posts.update { posts }
                    _isLoading.update { false }
                }
            }
            else{
                localPostRepository.getAllSavedPostsStream().collect{ posts ->
                    _posts.update { posts }
                    _isLoading.update { false }
                }
            }

        }
    }

    fun insertPostsInDatabase(post: PostData) {
        viewModelScope.launch {
            postRepository.insertItem(post);

        }

//        _feedUiState.update { currentState -> currentState.copy(posts = postRepository.getAllSavedPostsStream()) }
    }
    fun inserLocal(post:PostData){
        viewModelScope.launch {
            localPostRepository.insertItem(post)
        }
    }
    fun getlocalPosts(){
        viewModelScope.launch{
            localPostRepository.getAllSavedPostsStream()
        }
    }

    fun updateVotes(post: PostData){
        Log.d("posts", "updateVotes: $post")
        viewModelScope.launch {
            postRepository.updateUpAndDownVote(post)
        }
    }


    fun writeNewUser(
        name: String,
        email: String,
        username: String,
        university: String,
        department: String,
        semester: String,
    ) {
        // Basic data validation (you can add more checks as needed)
        if (email.isBlank() || username.isBlank()) {
            // Handle invalid input, e.g., show an error message
            return
        }

        val user = User(
            email= email,username= username,university= university, dept = department, sem=semester
        )
        val currentUser = mAuth.currentUser ?: return // Handle case where user is not logged in

        // Use try-catch for error handling
        try {
            db.child("user").child(currentUser.uid).push().setValue(user)
            // Data written successfully
//            Log.d("writtenUser", "writeNewUser: User Written")
        } catch (e: Exception) {
            // Handle database write error, e.g., logthe error or show an error message
            println("Error writing user data: ${e.message}")
        }
    }

//    fun writingPostDataInDB(
//        currentuser: User,
//
//        profileImage: Int?,
//        postDescription: String,
//        postImage: Int?,
//        postType: String,
//    ) {
//        val newPostRef = db.child("posts").child("userPosts").push()
//        val postId = newPostRef.key!!
//
//        newPostRef.setValue(
//            PostData(
////                if (postType.equals("visible")) currentuser.name else "Anonymous",
////                if (postType.equals("visible")) currentuser.dept else " ",
////                profileImage,
////                postId,
////                postDescription,
////                postImage,
////                mAuth.currentUser!!.uid,
////                upVotes(0, mutableListOf("")),
////                downVotes(0, mutableListOf(""))
//            )
//        )
//    }
//
//    suspend fun updateUpVote(
//        post: PostData,
//        upVotes: upVotes,
//
//        ) {
//        val dataSnapshot = db.child("posts").child("userPosts").get();
//        for (postSnapshot in dataSnapshot.await().children) {
//            val currentPost = postSnapshot.getValue(PostData::class.java)
//            if (post.postid.equals(currentPost?.postid)) {
//                if (post.downVote.userDislikedCurrentPost.contains(currentuser)) {
//                    post.downVote.userDislikedCurrentPost.remove(currentuser)
//
//                    var dislikes = post.downVote.dislike
//                    db
//                        .child("posts")
//                        .child("userPosts")
//                        .child(post.postid!!)
//                        .child("downVote")
//                        .setValue(downVotes(--dislikes, post.downVote.userDislikedCurrentPost))
//
//                }
////                if(post.upVote.userLikedCurrentPost.contains(currentuser)){
////                    var likes = post.upVote.like
////                    post.upVote.userLikedCurrentPost.remove(currentuser)
////                    db.child("posts")
////                        .child("userPosts")
////                        .child(post.postid!!)
////                        .child("downVote")
////                        .setValue(downVotes(--likes,post.upVote.userLikedCurrentPost))
////                }
//
//                    db
//                        .child("posts")
//                        .child("userPosts")
//                        .child(post.postid!!)
//                        .child("upVote")
//                        .setValue(upVotes)
//
//            }
//
//        }
//    }
//
//    suspend fun updateDownVote(
//        post: PostData,
//        downVotes: downVotes,
//
//        ) {
//        val dataSnapshot = db.child("posts").child("userPosts").get();
//        for (postSnapshot in dataSnapshot.await().children) {
//            val currentPost = postSnapshot.getValue(PostData::class.java)
//            if (post.postid.equals(currentPost?.postid)) {
//                if (post.upVote.userLikedCurrentPost.contains(currentuser)) {
//                    post.upVote.userLikedCurrentPost.remove(currentuser)
//
//                    var likes = post.upVote.like
//                    db
//                        .child("posts")
//                        .child("userPosts")
//                        .child(post.postid!!)
//                        .child("upVote")
//                        .setValue(upVotes(--likes, post.upVote.userLikedCurrentPost))
//
//                }
////                if(post.downVote.userDislikedCurrentPost.contains(currentuser)){
////                    var dislikes = post.downVote.dislike
////                    post.downVote.userDislikedCurrentPost.remove(currentuser)
////                    db.child("posts")
////                        .child("userPosts")
////                        .child(post.postid!!)
////                        .child("downVote")
////                        .setValue(downVotes(--dislikes,post.downVote.userDislikedCurrentPost))
////                }
//
//                    db
//                        .child("posts")
//                        .child("userPosts")
//                        .child(post.postid!!)
//                        .child("downVote")
//                        .setValue(downVotes)
//
//            }
//
//        }
//    }

    private suspend fun fetchUserData(): List<User> {
        val userList = mutableListOf<User>()
        try {
            val dataSnapshot =
                db.child("user").child(mAuth.currentUser!!.uid).get().await() // Fetch all users

            for (userSnapshot in dataSnapshot.children) {
                val user = userSnapshot.getValue(User::class.java)
                if (user != null) {
                    userList.add(user)
                }

            }
            userList

        } catch (e: Exception) {
            // Handle errors appropriately (e.g., log the error)
            emptyList()// Return an empty list in case of error
        }
        Log.d("userlist", "fetchUserData: ${userList}")
        return userList
    }


//    private val postListener = object : ValueEventListener {
//        override fun onDataChange(snapshot: DataSnapshot) {
//            viewModelScope.launch {
//                val updatedPostList = mutableListOf<PostData>()
//                for (childSnapshot in snapshot.children) {
//                    val post = childSnapshot.getValue(PostData::class.java)
//                    if (post != null) {
//                        updatedPostList.add(post)
//                    }
//                }
//                _feedUiState.update { currentState -> currentState.copy(posts = updatedPostList) }
//            }
//        }
//
//        override fun onCancelled(error: DatabaseError) {
//            Log.e("error", "onCancelled: ${error}")
//        }
//    }
//
//    init {
//        db.child("posts").child("userPosts").addValueEventListener(postListener)
//    }
}

