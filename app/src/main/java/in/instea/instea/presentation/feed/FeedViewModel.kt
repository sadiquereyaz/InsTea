package `in`.instea.instea.presentation.feed

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import `in`.instea.instea.data.repo.post.LocalPostRepository
import `in`.instea.instea.data.repo.post.NetworkPostRepository
import `in`.instea.instea.domain.datamodel.Comments
import `in`.instea.instea.domain.datamodel.PostData
import `in`.instea.instea.domain.datamodel.User
import `in`.instea.instea.utility.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FeedViewModel(
    private val postRepository: NetworkPostRepository,
    private val localPostRepository: LocalPostRepository,
    val context: Context,
) : ViewModel() {
    private val mAuth = Firebase.auth
    val db = Firebase.database.reference
    val currentuser = mAuth.currentUser?.uid
    var fetchedUsers: List<User> = emptyList()

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
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> get() = _userList
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _usersByQuery = MutableStateFlow<List<User>>(emptyList())
    val usersByQuery: StateFlow<List<User>> = _usersByQuery.asStateFlow()

    init {
        viewModelScope.launch {
            fetchPosts()
            fetchUserData()
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
            if (NetworkUtils.isNetworkAvailable(context)) {
                postRepository.getAllSavedPostsStream().collect { posts ->
                    _posts.update { posts }
                    _isLoading.update { false }
                }
            } else {
                localPostRepository.getAllSavedPostsStream().collect { posts ->
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

    fun inserLocal(post: PostData) {
        viewModelScope.launch {
            localPostRepository.insertItem(post)
        }
    }

    fun updateVotes(updatePost: PostData) {
        Log.d("posts", "updateVotes: $updatePost")
        viewModelScope.launch {
            val updatedPosts = _posts.value.map { post ->
                if (post.postid == updatePost.postid) updatePost else post
            }
            _posts.value = updatedPosts
            postRepository.updateUpAndDownVote(updatePost)
        }
    }

    fun updateComment(post: PostData) {
        Log.d("balle", "updateComment: ${post.postid}")
        viewModelScope.launch {
            postRepository.updateComment(post)
        }
    }
    fun deleteComment(comment: Comments){
        viewModelScope.launch {

        }
    }

    fun SearchUser(query:String){
        viewModelScope.launch {
            postRepository.search(query){
                _usersByQuery.value = it
            }
        }
    }
    fun DeletePost(post: PostData) {
        viewModelScope.launch {
            postRepository.Delete(post)
        }
    }

    private suspend fun fetchUserData() {

        try {
            val dataSnapshot =
                db.child("user").get().await() // Fetch all users
            val userList = dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }
            _userList.value = userList

        } catch (e: Exception) {
            // Handle errors appropriately (e.g., log the error)
            _userList.value= emptyList()// Return an empty list in case of error
        }
        Log.d("userlist", "fetchUserData: ${userList}")

    }


}
