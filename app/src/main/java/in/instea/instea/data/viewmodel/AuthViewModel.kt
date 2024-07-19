package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
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

    fun SignUpWithEmailAndPassword(email: String, password: String, onResul: (Boolean) -> Unit) {
        try {

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
        } catch (e: Exception) {
            _AuthState.value = AuthState.unauthenticated
            Log.e("error", "SignUpWithEmailAndPassword: exception occurred", e)
            onResul(false)
        }
    }

    fun login(email: String, password: String,loggedIn:(Boolean)->Unit) {
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("login", "login: user logged in")
                        loggedIn(true)
                    } else {
                        Log.e("error", "login: failed")
                        loggedIn(false)
                    }

                }
        }
        catch (e:Exception){

        }

    }
    fun resetPassword(email: String){
        try {
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("reset ", "password reset done ")
                    } else {
                        Log.e("error", "password reset failed")
                    }

                }
                .addOnCanceledListener {
                    Log.e("error", "Password reset canceled")
                }
        }
        catch (e:Exception){

        }
    }

    fun signOut() {
        mAuth.signOut()
        _AuthState.value = AuthState.unauthenticated
    }

//    fun writeNewUser(
//        email: String,
//        username: String,
//        university: String,
//        department: String,
//        semester: String,
//    ) {
//        // Basic data validation (you can add more checks as needed)
//        if (email.isBlank() || username.isBlank()) {
//            // Handle invalid input, e.g., show an error message
//            return
//        }
//
//        val user = User(email, username, university, department, semester)
//        val currentUser = mAuth.currentUser ?: return // Handle case where user is not logged in
//
//        // Use try-catch for error handling
//        try {
//            db.child("user").child(currentUser.uid).push().setValue(user)
//            // Data written successfully
//            Log.d("writtenUser", "writeNewUser: User Written")
//        } catch (e: Exception) {
//            // Handle database write error, e.g., logthe error or show an error message
//            println("Error writing user data: ${e.message}")
//        }
//    }

}

sealed class AuthState {
    object authenticated : AuthState()
    object unauthenticated : AuthState()
    object loading : AuthState()
}