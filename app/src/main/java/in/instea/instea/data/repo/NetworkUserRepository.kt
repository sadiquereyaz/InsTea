package `in`.instea.instea.data.repo

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import `in`.instea.instea.data.datamodel.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class NetworkUserRepository(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) {
    fun getUserById(userId: String): Flow<User> {
        // Implement your logic to fetch user by ID from Firebase
        val databaseReference = firebaseDatabase.reference
            .child("user").child(userId)
        return flow {
            val userSnapshot = databaseReference.get().await()
            if (userSnapshot.exists()) {
                val userData = userSnapshot.getValue(User::class.java)
                if (userData != null) {
                    emit(userData) // Emit the retrieved UserModel
                } else {
                    // Handle the case where user data exists but cannot be mapped to UserModel
                    emit(
                        User(
                            "id unmatched",
                            "unmatchedModel",
                            "user data exists but cannot be mapped to UserModel"
                        )
                    ) // Consider emitting an empty UserModel or error state here
                }
            } else {
                // Handle the case where user data does not exist
                emit(
                    User(
                        "at firebase, id does not exist",
                        "userIdNotAvailable",
                        "given id is not present in firebase"
                    )
                ) // Consider emitting an empty UserModel or error state here
            }
        }
//        return flowOf(UserModel(userId, "Firebase User", "network repository"))
    }

    suspend fun updateUser(user: User) {
        val userId = user.userId
        if (userId != null) {
            val databaseReference = firebaseDatabase.reference.child("user").child(userId)
            databaseReference.setValue(user).await()
        }
    }

    suspend fun deleteUser(user: User) {

    }

    //sign in
    suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: "No UID"
//            Log.d("USER_ID", userId)
            Result.success(userId)
        } catch (e: Exception) {
            Log.e("NetworkUserRepository", "Sign-in error", e)
            Result.failure(e)
        }
    }

    // sign up
    suspend fun signUp(user: User,password: String): Result<String> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(
                user.email ?: "noemailrecieved@networkrepo.com", password ?: "networkPassword"
            ).await()
            val userId = result.user?.uid ?: return Result.failure(Exception("No UID"))
            val newUser = user.copy(userId = userId)
            firebaseDatabase.reference.child("user").child(userId).setValue(newUser).await()
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isUserNameAvailable(username: String): Result<Boolean> {
            return try {
                val snapshot = firebaseDatabase.reference
                    .child("user")
                    .orderByChild("username")
                    .equalTo(username)
                    .get()
                    .await()

                val isAvailable = snapshot.childrenCount == 0L
                Result.success(isAvailable)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}