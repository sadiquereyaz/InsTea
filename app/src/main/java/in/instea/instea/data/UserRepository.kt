package `in`.instea.instea.data

import com.google.firebase.database.FirebaseDatabase
import `in`.instea.instea.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface UserRepository {
    fun getUserById(int: Int): Flow<UserModel>
//    suspend fun updateUserDetails(user: UserModel)
}

class CombinedUserRepository(
    private val localUserRepository: LocalUserRepository,
    private val networkUserRepository: NetworkUserRepository
) : UserRepository {
    override fun getUserById(userId: Int): Flow<UserModel> {
        // Combine local and network repositories as needed
        return localUserRepository.getUserById(userId)
//        return UserModel(12345, "sadique", about = "Instea is really great app")
    }
}

class LocalUserRepository(private val postDao: PostDao) : UserRepository {
    override fun getUserById(userId: Int): Flow<UserModel> = postDao.getUserById(userId)
}

class NetworkUserRepository(private val firebaseDatabase: FirebaseDatabase) : UserRepository {
    override fun getUserById(userId: Int): Flow<UserModel> {
        // Implement your logic to fetch user by ID from Firebase
        return flowOf(UserModel(userId, "Firebase User"))
    }
}