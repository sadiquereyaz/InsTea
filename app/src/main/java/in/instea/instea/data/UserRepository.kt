package `in`.instea.instea.data

import com.google.firebase.database.FirebaseDatabase
import `in`.instea.instea.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

interface UserRepository {
    fun getUserById(int: Int): Flow<UserModel>
    suspend fun insertUser(user: UserModel)
}

class CombinedUserRepository(
    private val localUserRepository: LocalUserRepository,
    private val networkUserRepository: NetworkUserRepository
) : UserRepository {
    override fun getUserById(userId: Int): Flow<UserModel> = flow {
        // Try fetching user from local database first
        val localUser = localUserRepository.getUserById(userId).firstOrNull()
        if (localUser != null) {
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

class LocalUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getUserById(userId: Int): Flow<UserModel> = userDao.getUserById(userId)
    override suspend fun insertUser(user: UserModel) {
        userDao.insertUser(user)
    }
}

class NetworkUserRepository(private val firebaseDatabase: FirebaseDatabase) : UserRepository {
    override fun getUserById(userId: Int): Flow<UserModel> {
        // Implement your logic to fetch user by ID from Firebase
        val databaseReference = firebaseDatabase.reference.child("user2").child(userId.toString())
        return flow {
            val userSnapshot = databaseReference.get().await()
            if (userSnapshot.exists()) {
                val userData = userSnapshot.getValue(UserModel::class.java)
                if (userData != null) {
                    emit(userData) // Emit the retrieved UserModel
                } else {
                    // Handle the case where user data exists but cannot be mapped to UserModel
                    emit(
                        UserModel(
                            12345,
                            "unmatchedModel",
                            "user data exists but cannot be mapped to UserModel"
                        )
                    ) // Consider emitting an empty UserModel or error state here
                }
            } else {
                // Handle the case where user data does not exist
                emit(
                    UserModel(
                        12345,
                        "userIdNotAvailable",
                        "given id is not present in firebase"
                    )
                ) // Consider emitting an empty UserModel or error state here
            }
        }
//        return flowOf(UserModel(userId, "Firebase User", "network repository"))
    }

    override suspend fun insertUser(user: UserModel) {
        TODO("Not yet implemented")
    }
}