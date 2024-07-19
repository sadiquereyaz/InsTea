package `in`.instea.instea.data.repo

import `in`.instea.instea.data.datamodel.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

interface UserRepository {
    fun getCurrentUserId(): Flow<String>
    fun getUserById(userId: String): Flow<UserModel>
    suspend fun upsertUser(user: UserModel)
    suspend fun saveUserToFirebase(user: UserModel)
    suspend fun signIn(email: String, password: String): Result<String>
    suspend fun signUp(email: String, password: String): Result<String>
    suspend fun clearUser()
}

class CombinedUserRepository(
    private val localUserRepository: LocalUserRepository,
    private val networkUserRepository: NetworkUserRepository,
) : UserRepository {
    override fun getCurrentUserId(): Flow<String> = localUserRepository.getUserId()

    override fun getUserById(userId: String): Flow<UserModel> = flow {
        // Try fetching user from local database first
        val localUser = localUserRepository.getCurrentUser().firstOrNull()
        if (localUser?.userId == userId) {
            emit(localUser) // Emit user from local storage if found
        } else {
            // Fetch user from Firebase if not found locally
            val networkUser = networkUserRepository.getUserById(userId).firstOrNull()
            if (networkUser != null) {
                emit(networkUser) // Emit user fetched from Firebase
                // Insert user into Room database asynchronously
                localUserRepository.upsertUser(networkUser)
            } else {
                // Handle the case where the user is not found in Firebase
                emit(UserModel(userId, "userIdNotAvailable", "Given ID is not present in Firebase"))
            }
        }
    }

    override suspend fun upsertUser(user: UserModel) {
        localUserRepository.upsertUser(user)
        //insert in firebase as well
        networkUserRepository.updateUser(user)
    }

    override suspend fun clearUser() {
        localUserRepository.clearUser()
    }

    override suspend fun saveUserToFirebase(user: UserModel) {
//        TODO("check for the non existence of email")
        networkUserRepository.updateUser(user = user)
    }

    override suspend fun signIn(email: String, password: String): Result<String> {
        return networkUserRepository.signIn(email, password)
    }

    override suspend fun signUp(email: String, password: String): Result<String> {
        return networkUserRepository.signUp(email, password)
    }
}

