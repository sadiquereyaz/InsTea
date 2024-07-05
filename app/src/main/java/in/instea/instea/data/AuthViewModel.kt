package `in`.instea.instea.data

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.internal.Objects
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import `in`.instea.instea.data.User
import `in`.instea.instea.screens.Feed.GetUserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val mAuth = Firebase.auth
    private val db = Firebase.database.reference
    private val _AuthState = MutableStateFlow<AuthState>(AuthState.unauthenticated)
    val authState: StateFlow<AuthState> = _AuthState.asStateFlow()


    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (mAuth.currentUser == null) {
            _AuthState.value = AuthState.unauthenticated
        } else {
            _AuthState.value = AuthState.authenticated
        }
    }

    fun SignUpWithEmailAndPassword(email: String, password: String,onResul:(Boolean)->Unit) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _AuthState.value = AuthState.authenticated

                    Log.d("signup", "SignUpWithEmailAndPassword: user Creaeted")
                    onResul(true)

                } else {
                    _AuthState.value = AuthState.unauthenticated
                    Log.e("error", "SignUpWithEmailAndPassword: failed")
                    onResul(false)
                }
            }
    }

    fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("login", "login: user logged in")

                } else {
                    Log.e("error", "login: failed")
                }

            }
    }

    fun signOut() {
        mAuth.signOut()
        _AuthState.value = AuthState.unauthenticated
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

}

sealed class AuthState {
    object authenticated : AuthState()
    object unauthenticated : AuthState()
}