package `in`.instea.instea.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import `in`.instea.instea.data.repo.AcademicRepository
import `in`.instea.instea.data.repo.AccountService
import `in`.instea.instea.data.repo.AccountServiceImpl
import `in`.instea.instea.data.repo.CombinedPostRepository
import `in`.instea.instea.data.repo.CombinedUserRepository
import `in`.instea.instea.data.repo.LocalPostRepository
import `in`.instea.instea.data.repo.LocalScheduleRepository
import `in`.instea.instea.data.repo.LocalUserRepository
import `in`.instea.instea.data.repo.NetworkAcademicRepository
import `in`.instea.instea.data.repo.NetworkPostRepository
import `in`.instea.instea.data.repo.NetworkUserRepository
import `in`.instea.instea.data.repo.PostRepository
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.data.repo.UserRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val postRepository: PostRepository
    val userRepository: UserRepository
    val scheduleRepository: ScheduleRepository
    val networkRepository: NetworkPostRepository
    val localPostRepository:LocalPostRepository
//    val userPreferenceRepository: UserPreferenceRepository
    val academicRepository: AcademicRepository
    val accountService: AccountService
}

private const val CURRENT_USER = "current_user"

// Create an extension property to access the DataStore instance
val Context.dataStore by preferencesDataStore(name = CURRENT_USER)


class AppDataContainer(
    private val context: Context
) : AppContainer {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val roomDatabase = InsteaDatabase.getDatabase(context)

    override val postRepository: PostRepository by lazy {
        val localPostRepository = LocalPostRepository(roomDatabase.postDao())
        val networkPostRepository = NetworkPostRepository(firebaseDatabase)
        CombinedPostRepository(localPostRepository, networkPostRepository)
    }
    override val userRepository: UserRepository by lazy {
        val localUserRepository = LocalUserRepository(context.dataStore)
        val networkUserRepository = NetworkUserRepository(
            firebaseDatabase = firebaseDatabase,
            firebaseAuth = firebaseAuth
        )
        CombinedUserRepository(
            localUserRepository = localUserRepository,
            networkUserRepository = networkUserRepository
        )
    }
    override val scheduleRepository: ScheduleRepository by lazy {
        LocalScheduleRepository(roomDatabase.classDao())
    }
    override val academicRepository: AcademicRepository by lazy {
        NetworkAcademicRepository(firebaseInstance = firebaseDatabase)
    }
    override val networkRepository: NetworkPostRepository by lazy {
        NetworkPostRepository(FirebaseDatabase.getInstance())
    }
    override val localPostRepository: LocalPostRepository by lazy {
       LocalPostRepository(postDao = roomDatabase.postDao())
    }
    override val accountService: AccountService by lazy {
        AccountServiceImpl()
    }
}