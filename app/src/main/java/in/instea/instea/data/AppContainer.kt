package `in`.instea.instea.data

import android.content.Context
import com.google.firebase.database.FirebaseDatabase

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val postRepository: PostRepository
    val userRepository: UserRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    ///lazy delegate to ensure that MyFriendsRepository is instantiated only when needed, using the appropriate Dao obtained from database.
    override val postRepository: PostRepository by lazy {
        val localPostRepository = LocalPostRepository(InsteaDatabase.getDatabase(context).postDao())
        val networkPostRepository = NetworkPostRepository(FirebaseDatabase.getInstance())
        CombinedPostRepository(localPostRepository, networkPostRepository)
    }
    override val userRepository: UserRepository by lazy {
        val localUserRepository = LocalUserRepository(InsteaDatabase.getDatabase(context).postDao())
        val networkUserRepository = NetworkUserRepository(FirebaseDatabase.getInstance())
        CombinedUserRepository(
            localUserRepository = localUserRepository,
            networkUserRepository = networkUserRepository
        )
    }
}