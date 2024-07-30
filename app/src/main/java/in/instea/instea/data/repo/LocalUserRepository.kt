package `in`.instea.instea.data.repo

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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
        val USER_UNIVERSITY = stringPreferencesKey("user_university")
        val USER_DEPARTMENT = stringPreferencesKey("user_department")
        val USER_SEMESTER = stringPreferencesKey("user_semester")
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
                userId = preferences[USER_ID] ?: "",
                username = preferences[USER_NAME] ?: "",
                about = preferences[USER_ABOUT] ?: "Hey there I'm using InsTea!",
                email = preferences[USER_EMAIL] ?: "",
                linkedinId = preferences[USER_LINKEDIN] ?: "",
                instaId = preferences[USER_INSTAGRAM] ?: "",
                whatsappNo = preferences[USER_WHATSAPP] ?: "",
                university = preferences[USER_UNIVERSITY] ?: "",
                dept = preferences[USER_DEPARTMENT] ?: "",
                sem = preferences[USER_SEMESTER] ?: ""
            )
        }

    // Function to save the user details
    suspend fun upsertUser(user: User) {
//        Log.d("datastore username saving", user.username!!)
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.userId ?: ""
            preferences[USER_NAME] = user.username ?: ""
            preferences[USER_ABOUT] = user.about ?: ""
            preferences[USER_UNIVERSITY] = user.university ?: ""
            preferences[USER_DEPARTMENT] = user.dept ?: ""
            preferences[USER_SEMESTER] = user.sem ?: ""
            preferences[USER_EMAIL] = user.email ?: ""
            preferences[USER_WHATSAPP] = user.whatsappNo ?: ""
            preferences[USER_INSTAGRAM] = user.instaId ?: ""
            preferences[USER_LINKEDIN] = user.linkedinId ?: ""
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