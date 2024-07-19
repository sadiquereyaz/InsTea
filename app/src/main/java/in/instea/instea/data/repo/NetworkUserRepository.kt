package `in`.instea.instea.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import `in`.instea.instea.data.datamodel.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class NetworkUserRepository(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) {
    fun getUserById(userId: String): Flow<UserModel> {
        // Implement your logic to fetch user by ID from Firebase
        val databaseReference = firebaseDatabase.reference
            .child("user").child(userId)
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
                            "id unmatched",
                            "unmatchedModel",
                            "user data exists but cannot be mapped to UserModel"
                        )
                    ) // Consider emitting an empty UserModel or error state here
                }
            } else {
                // Handle the case where user data does not exist
                emit(
                    UserModel(
                        "at firebase, id does not exist",
                        "userIdNotAvailable",
                        "given id is not present in firebase"
                    )
                ) // Consider emitting an empty UserModel or error state here
            }
        }
//        return flowOf(UserModel(userId, "Firebase User", "network repository"))
    }

    suspend fun updateUser(user: UserModel) {
        val databaseReference = firebaseDatabase.reference.child("user2").child(user.userId)
        databaseReference.setValue(user).await()
    }

    suspend fun deleteUser(user: UserModel) {

    }

    //sign in
    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "No UID")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // sign up
    suspend fun signUp(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Result.success(result.user?.uid ?: "No UID")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}