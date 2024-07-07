package `in`.instea.instea.data

import GetPostData
import android.util.Log
//import androidx.compose.ui.tooling.data.EmptyGroup.location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import `in`.instea.instea.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FeedViewModel : ViewModel() {
    private val mAuth = Firebase.auth
    val db = Firebase.database.reference
    var currentuser: User = User()


    //    private val chatUiState = MutableStateFlow(PostData())
//    val _uistate: StateFlow<PostData> = chatUiState.asStateFlow()
    private val _feedUiState = MutableStateFlow(FeedUiState())
    val feedUiState: StateFlow<FeedUiState> = _feedUiState.asStateFlow()
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()


    init {
        viewModelScope.launch {

            val fetchedUsers = fetchUserData()

            val currentUser = fetchedUsers.find { it.email == mAuth.currentUser?.email }
//            Log.d("fetchusers", "${fetchedUsers}: ")
            if (currentUser != null) {

                _user.update { currentState ->
                    currentState.copy(
                        name = currentUser.name,
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
//
//    init {
//        viewModelScope.launch {
//
//        }
//
//    }

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

        val user = `in`.instea.instea.data.User(
            name,
            email,
            username,
            null,
            university,
            department,
            semester
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

    fun writingPostDataInDB(
        currentuser: User,

        profileImage: Int?,
        postDescription: String,
        postImage: Int?,
    ) {
        db.child("posts")
            .child("userPosts")
            .push()
            .setValue(
                PostData(
                    currentuser.name,
                    currentuser.dept,
                    profileImage,
                    postDescription,
                    postImage,
                    mAuth.currentUser!!.uid
                )
            )
    }


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


    private val postListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            viewModelScope.launch {
                val updatedPostList = mutableListOf<PostData>()
                for (childSnapshot in snapshot.children) {
                    val post = childSnapshot.getValue(PostData::class.java)
                    if (post != null) {
                        updatedPostList.add(post)
                    }
                }
                _feedUiState.update { currentState-> currentState.copy(posts = updatedPostList) }
            }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle database errors
        }
    }

    init {
        db.child("posts").child("userPosts").addValueEventListener(postListener)
    }
}

