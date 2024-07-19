package `in`.instea.instea.data.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.play.integrity.internal.i
import `in`.instea.instea.data.datamodel.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LocalUserRepository(
    private val dataStore: DataStore<Preferences>
) {
    // Define keys for storing user data
    private companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_POINT = intPreferencesKey("user_point")
        val USER_ABOUT = stringPreferencesKey("user_about")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_LINKEDIN = stringPreferencesKey("user_linkedin")
        val USER_INSTAGRAM = stringPreferencesKey("user_instagram")
        val USER_WHATSAPP = stringPreferencesKey("user_whatsapp")
    }

    // Function to get the user details
    fun getCurrentUser(): Flow<User> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("SHARED_STORE", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else
                throw it
        }
        .map { preferences ->
            User(
                userId = preferences[USER_ID] ?: "datastore id",
                username = preferences[USER_NAME] ?: "data Store",
                about = preferences[USER_ABOUT]
                    ?: "this is default value of user detail in data store",
//                isLoggedIn = preferences[IS_LOGGED_IN] ?: false
            )
        }

    // Function to save the user details
    suspend fun upsertUser(user: User) {
        Log.d("datastore username saving", user.username!!)
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.userId ?: "datastore id"
            preferences[USER_NAME] = user.username
            preferences[USER_ABOUT] = user.about ?: "Hey there, InsTea is great!"
        }
    }

    fun getUserId(): Flow<String> {
        return dataStore.data
            .map { preferences ->
                preferences[USER_ID] ?: throw Exception("User ID not found")
            }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}