package `in`.instea.instea.domain.repo

import `in`.instea.instea.domain.datamodel.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String
    val currentEmail: String
    fun hasUser(): Boolean
    fun getUserProfile(): User
    suspend fun isUserProfileComplete(): Boolean
    suspend fun updateDisplayName(newDisplayName: String)
    suspend fun signInWithGoogle(idToken: String)
    suspend fun signOut(): Result<String?>
    suspend fun deleteAccount(): Result<String?>
}