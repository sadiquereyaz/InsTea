package `in`.instea.instea.data

import GetPostData
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import `in`.instea.instea.screens.Feed.GetUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import kotlin.math.log

class ChatViewModel : ViewModel() {
    private val mAuth = Firebase.auth
    val db = Firebase.database.reference

    //    private val chatUiState = MutableStateFlow(PostData())
//    val _uistate: StateFlow<PostData> = chatUiState.asStateFlow()
    private val _chatUiState = MutableStateFlow(FeedUiState())
    val ChatUiState: StateFlow<FeedUiState> = _chatUiState.asStateFlow()

    init {
        _chatUiState.update { currentState-> currentState.copy(posts = GetPostData())
        }
    }

    fun writeNewUser(
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

        val user = User(email, username, university, department, semester)
        val currentUser = mAuth.currentUser ?: return // Handle case where user is not logged in

        // Use try-catch for error handling
        try {
            db.child("user").child(currentUser.uid).push().setValue(user)
            // Data written successfully
            Log.d("writtenUser", "writeNewUser: User Written")
        } catch (e: Exception) {
            // Handle database write error, e.g., logthe error or show an error message
            println("Error writing user data: ${e.message}")
        }
    }

    fun writingPostDataInDB(
        name: String,
        location: String,
        profileImage: Int,
        postDescription: String,
        postImage: Int,
    ) {
        db.child("posts")
            .child(mAuth.currentUser!!.email!!)
            .push()
            .setValue(PostData(name, location, profileImage, postDescription, postImage))
    }

}