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

        return flow {
            val userSnapshot = databaseReference.child("user").child(userId).get().await()
            Log.d("chatPartners", "getUserById: $userSnapshot")
            if (userSnapshot != null) {
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

    suspend fun deleteUser(uid: String) {
        try {
            // Delete user data from Firebase Realtime Database
            firebaseDatabase.reference.child("user").child(uid).removeValue().await()
            firebaseAuth.currentUser!!.delete().await()
        } catch (e: Exception) {
            Log.e("NetworkUserRepository", "Error deleting user", e)
            throw e
        }
    }

    // sign up
    suspend fun insertUserToFirebase(user: User): Result<String?> {
        return try {
            firebaseDatabase.reference.child("user").child(user.userId!!).setValue(user).await()
            Result.success(null)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isUserNameAvailable(username: String): Result<String?> {
        return try {
            val snapshot = firebaseDatabase.reference.child("user")
                .orderByChild("username")
                .equalTo(username)
                .get()
                .await()
            if (snapshot.exists()) {
                Result.success("Username taken")
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isUserIdAvailable(uid: String): Result<String?> {
        return try {
            val snapshot = firebaseDatabase.reference.child("user")
//                .orderByChild("username")
                .child(uid)
                .get()
                .await()
            if (snapshot.exists()) {
                Log.d("SNAPSHOT OF UID", snapshot.toString())
                Result.success("User info is available to this uid")
            } else {
                Log.d("UID_NREPO = null", "snapshot not exist of id $uid")
                Result.success(null)
            }
        } catch (e: Exception) {
            Log.d("UID_FETCH ERROR = null", e.localizedMessage ?: "unknown error")
            Result.failure(e)
        }
    }
}