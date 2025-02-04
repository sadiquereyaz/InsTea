package `in`.instea.instea.domain.repo

import `in`.instea.instea.domain.datamodel.User
import `in`.instea.instea.presentation.more.classmate
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUserId(): Flow<String>
    fun getUserById(userId: String): Flow<User>
    suspend fun upsertUserLocally(user: User)
    suspend fun upsertUserToFirebase(user: User)
    suspend fun insertUser(user: User): Result<String?>
    suspend fun clearUser()
    suspend fun isUserNameAvailable(username: String): Result<String?>
    suspend fun isUserIdAvailable(uid: String): Result<String?>
    suspend fun deleteUserDetails(currentUserId: String)
    suspend fun getclassmates(): List<classmate>
}