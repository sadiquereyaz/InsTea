package `in`.instea.instea.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.database.FirebaseDatabase
import `in`.instea.instea.data.repo.CombinedPostRepository
import `in`.instea.instea.data.repo.CombinedUserRepository
import `in`.instea.instea.data.repo.LocalPostRepository
import `in`.instea.instea.data.repo.LocalUserRepository
import `in`.instea.instea.data.repo.NetworkPostRepository
import `in`.instea.instea.data.repo.NetworkUserRepository
import `in`.instea.instea.data.repo.PostRepository
import `in`.instea.instea.data.repo.CurrentUserRepository
import `in`.instea.instea.data.repo.UserPreferenceRepository
import `in`.instea.instea.data.repo.UserRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val postRepository: PostRepository
    val userRepository: UserRepository
    val userPreferenceRepository: UserPreferenceRepository
}

private const val USER_PREFERENCES_NAME = "user_preferences"
// Create an extension property to access the DataStore instance
val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)


class AppDataContainer(private val context: Context) : AppContainer {
    override val postRepository: PostRepository by lazy {
        val localPostRepository = LocalPostRepository(InsteaDatabase.getDatabase(context).postDao())
        val networkPostRepository = NetworkPostRepository(FirebaseDatabase.getInstance())
        CombinedPostRepository(localPostRepository, networkPostRepository)
    }
    override val userRepository: UserRepository by lazy {
        val localUserRepository = LocalUserRepository(InsteaDatabase.getDatabase(context).userDao())
        val networkUserRepository = NetworkUserRepository(FirebaseDatabase.getInstance())
        val currentUserPreferenceRepository =  CurrentUserRepository(context.dataStore)
        CombinedUserRepository(
            localUserRepository = localUserRepository,
            networkUserRepository = networkUserRepository,
            currentUserPreferenceRepository = currentUserPreferenceRepository
        )
    }
    val currentUserPreferenceRepository: UserPreferenceRepository by lazy {
        CurrentUserRepository(context.dataStore)
    }
}