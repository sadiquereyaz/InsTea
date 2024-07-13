package `in`.instea.instea.data.repo

import `in`.instea.instea.data.datamodel.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

interface UserRepository {
    fun getUserById(userId: Int): Flow<UserModel>
    suspend fun insertUser(user: UserModel)
}

class CombinedUserRepository(
    private val localUserRepository: LocalUserRepository,
    private val networkUserRepository: NetworkUserRepository,
) : UserRepository {
    override fun getUserById(userId: Int): Flow<UserModel> = flow {
        // Try fetching user from local database first
        val localUser = localUserRepository.getUserById(userId).firstOrNull()
        if (localUser?.userId == userId) {
            emit(localUser) // Emit user from local storage if found
        } else {
            // Fetch user from Firebase if not found locally
            val networkUser = networkUserRepository.getUserById(userId).firstOrNull()
            if (networkUser != null) {
                emit(networkUser) // Emit user fetched from Firebase
                // Insert user into Room database asynchronously
                localUserRepository.insertUser(networkUser)
            } else {
                // Handle the case where the user is not found in Firebase
                emit(UserModel(userId, "userIdNotAvailable", "Given ID is not present in Firebase"))
            }
        }
    }
//        return UserModel(12345, "sadique", about = "Instea is really great app")

    override suspend fun insertUser(user: UserModel) {
        localUserRepository.insertUser(user)
        //insert in firebase as well
        networkUserRepository.insertUser(user)
    }
}

