package `in`.instea.instea.data.repo

import android.util.Log
import `in`.instea.instea.data.datamodel.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine

interface UserRepository {
    fun getCurrentUserId(): Flow<String>
    fun getUserById(userId: String): Flow<User>
    suspend fun upsertUserLocally(user: User)
    suspend fun upsertUserToFirebase(user: User)
    suspend fun signIn(email: String, password: String): Result<String>
    suspend fun signUp(user: User): Result<String?>
    suspend fun clearUser()
    suspend fun isUserNameAvailable(username: String): Result<String?>
    suspend fun isUserIdAvailable(uid: String): Result<String?>
}

class CombinedUserRepository(
    private val localUserRepository: LocalUserRepository,
    private val networkUserRepository: NetworkUserRepository,
) : UserRepository {
    override fun getCurrentUserId(): Flow<String> = localUserRepository.getUserId()

    // override fun getUserById(userId: String): Flow<User> = localUserRepository.getCurrentUser()


    override fun getUserById(userId: String): Flow<User> =
        localUserRepository.getCurrentUser()
            .combine(networkUserRepository.getUserById(userId)) { localUser, networkUser ->
                when {
                    localUser?.userId == userId -> localUser
                    networkUser != null -> networkUser
                    else -> User(userId = "userIdNotAvailable", email = "User not found")
                }
            }
            .catch { throwable ->
                Log.e("UserRepository", "Error fetching user: ${throwable.message}")
                emit(User(userId = "error", email = "Error fetching user data"))
            }


    override suspend fun upsertUserLocally(user: User) {
        localUserRepository.upsertUser(user)
        //insert in firebase as well
//        networkUserRepository.updateUser(user)
    }

    override suspend fun clearUser() {
        localUserRepository.clearUser()
    }

    override suspend fun isUserNameAvailable(username: String): Result<String?> {
        return networkUserRepository.isUserNameAvailable(username)
    }

    override suspend fun isUserIdAvailable(uid: String): Result<String?> {
        return networkUserRepository.isUserIdAvailable(uid)
    }

    override suspend fun upsertUserToFirebase(user: User) {
//        TODO("check for the non existence of email")
        networkUserRepository.updateUser(user = user)
    }

    override suspend fun signIn(email: String, password: String): Result<String> {
        return networkUserRepository.signIn(email, password)
    }

    override suspend fun signUp(user: User): Result<String?> {
        return networkUserRepository.insertUserToFirebase(user)
    }
}

