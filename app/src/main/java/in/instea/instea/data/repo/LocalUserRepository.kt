package `in`.instea.instea.data.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import `in`.instea.instea.data.datamodel.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LocalUserRepository(
    private val dataStore: DataStore<Preferences>
): UserRepository {
    // Define keys for storing user data
    private companion object {
        val USER_ID = intPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_POINT = intPreferencesKey("user_point")
        val USER_ABOUT = stringPreferencesKey("user_about")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_LINKEDIN = stringPreferencesKey("user_linkedin")
        val USER_INSTAGRAM = stringPreferencesKey("user_instagram")
        val USER_WHATSAPP = stringPreferencesKey("user_whatsapp")
    }

    // Function to get the user details
    override fun getUserById(int: Int): Flow<UserModel> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e("SHARED_STORE", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else
                throw it
        }
        .map { preferences ->
            UserModel(
                userId = preferences[USER_ID] ?: 12345,
                username = preferences[USER_NAME] ?: "data Store",
                about = preferences[USER_ABOUT]
                    ?: "this is default value of user detail in data store",
//                isLoggedIn = preferences[IS_LOGGED_IN] ?: false
            )
        }

    // Function to save the user details
    override suspend fun insertUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.userId
            preferences[USER_NAME] = user.username
            preferences[USER_ABOUT] = user.about
        }
    }

    // Function to clear the user details
    suspend fun clearUserDetails() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}