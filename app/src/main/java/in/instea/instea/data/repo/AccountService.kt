package `in`.instea.instea.data.repo

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import `in`.instea.instea.data.datamodel.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String
    val currentEmail: String
    fun hasUser(): Boolean
    fun getUserProfile(): User
    suspend fun isUserProfileComplete(): Boolean
    suspend fun updateDisplayName(newDisplayName: String)
    suspend fun signInWithGoogle(idToken: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}

class AccountServiceImpl() : AccountService {

    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser.toInsteaUser())
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()
    override val currentEmail: String
        get() = Firebase.auth.currentUser?.email.orEmpty()

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun getUserProfile(): User {
        return Firebase.auth.currentUser.toInsteaUser()
    }
    override suspend fun isUserProfileComplete(): Boolean {
        val user = Firebase.auth.currentUser
        return user != null && !user.displayName.isNullOrEmpty()
    }

    override suspend fun updateDisplayName(newDisplayName: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName
        }
        Firebase.auth.currentUser!!.updateProfile(profileUpdates).await()
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(firebaseCredential).await()
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }

    private fun FirebaseUser?.toInsteaUser(): User {
        return if (this == null) User() else User(
            userId = this.uid,
            email = this.email ?: ""
        )
    }
}