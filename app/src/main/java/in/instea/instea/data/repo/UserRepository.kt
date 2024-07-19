package `in`.instea.instea.data.repo

import `in`.instea.instea.data.datamodel.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

interface UserRepository {
    fun getCurrentUserId(): Flow<String>
    fun getUserById(userId: String): Flow<User>
    suspend fun upsertUserLocally(user: User)
    suspend fun upsertUserToFirebase(user: User)
    suspend fun signIn(email: String, password: String): Result<String>
    suspend fun signUp(user: User): Result<String>
    suspend fun clearUser()
}

class CombinedUserRepository(
    private val localUserRepository: LocalUserRepository,
    private val networkUserRepository: NetworkUserRepository,
) : UserRepository {
    override fun getCurrentUserId(): Flow<String> = localUserRepository.getUserId()

    override fun getUserById(userId: String): Flow<User> = flow {
        // Try fetching user from local database first
        val localUser = localUserRepository.getCurrentUser().firstOrNull()
        if (localUser?.userId == userId) {
            emit(localUser) // Emit user from local storage if found
        } else {
            // Fetch user from Firebase if not found locally
            val networkUser = networkUserRepository.getUserById(userId).firstOrNull()
            if (networkUser != null) {
                emit(networkUser) // Emit user fetched from Firebase
            } else {
                // Handle the case where the user is not found in Firebase
                emit(User(userId="userIdNotAvailable", email = "Given ID is not present in Firebase"))
            }
        }
    }

    override suspend fun upsertUserLocally(user: User) {
        localUserRepository.upsertUser(user)
        //insert in firebase as well
//        networkUserRepository.updateUser(user)
    }

    override suspend fun clearUser() {
        localUserRepository.clearUser()
    }

    override suspend fun upsertUserToFirebase(user: User) {
//        TODO("check for the non existence of email")
        networkUserRepository.updateUser(user = user)
    }

    override suspend fun signIn(email: String, password: String): Result<String> {
        return networkUserRepository.signIn(email, password)
    }

    override suspend fun signUp(user: User): Result<String> {
        return networkUserRepository.signUp(user)
    }
}

