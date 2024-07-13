package `in`.instea.instea.data.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

class CurrentUserRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val currentUserId = stringPreferencesKey("current_user_id")
        val currentUserName = stringPreferencesKey("current_user_username")
        val currentUserUniversity = stringPreferencesKey("current_user_university")
        val currentUserDepartment = stringPreferencesKey("current_user_department")
        val currentUserSemester = stringPreferencesKey("current_user_department")
    }
}