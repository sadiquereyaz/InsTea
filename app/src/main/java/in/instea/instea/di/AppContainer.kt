package `in`.instea.instea.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import `in`.instea.instea.data.local.InsteaDatabase
import `in`.instea.instea.data.repo.AccountServiceImpl
import `in`.instea.instea.data.repo.LocalScheduleRepository
import `in`.instea.instea.data.repo.NetworkAcademicRepository
import `in`.instea.instea.data.repo.WorkManagerTaskRepository
import `in`.instea.instea.data.repo.chat.NetworkChatRepository
import `in`.instea.instea.data.repo.notice.CombinedNoticeRepository
import `in`.instea.instea.data.repo.notice.LocalNoticeRepository
import `in`.instea.instea.data.repo.notice.NetworkNoticeRepository
import `in`.instea.instea.data.repo.notice.WebScrapingService
import `in`.instea.instea.data.repo.post.CombinedPostRepository
import `in`.instea.instea.data.repo.post.LocalPostRepository
import `in`.instea.instea.data.repo.post.NetworkPostRepository
import `in`.instea.instea.data.repo.user.CombinedUserRepository
import `in`.instea.instea.data.repo.user.LocalUserRepository
import `in`.instea.instea.data.repo.user.NetworkUserRepository
import `in`.instea.instea.domain.repo.AcademicRepository
import `in`.instea.instea.domain.repo.AccountService
import `in`.instea.instea.domain.repo.NoticeRepository
import `in`.instea.instea.domain.repo.PostRepository
import `in`.instea.instea.domain.repo.ScheduleRepository
import `in`.instea.instea.domain.repo.TaskReminderRepository
import `in`.instea.instea.domain.repo.UserRepository

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val postRepository: PostRepository
    val userRepository: UserRepository
    val scheduleRepository: ScheduleRepository
    val networkRepository: NetworkPostRepository
    val localPostRepository: LocalPostRepository
    val workManagerTaskRepository: TaskReminderRepository
    val academicRepository: AcademicRepository
    val accountService: AccountService
//    val webScrapingService: WebScrapingService
    val networkChatRepository: NetworkChatRepository
    val noticeRepository: NoticeRepository
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
    private val webScrapingService = WebScrapingService()

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
    override val workManagerTaskRepository: TaskReminderRepository by lazy {
        WorkManagerTaskRepository(context)
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
    override val networkChatRepository: NetworkChatRepository by lazy {
        NetworkChatRepository(
            userRepository = NetworkUserRepository(firebaseDatabase, firebaseAuth)
        )
    }
    override val noticeRepository: NoticeRepository by lazy {
        val localNoticeRepository = LocalNoticeRepository(roomDatabase.noticeDao())
        val networkNoticeRepository = NetworkNoticeRepository(webScrapingService)
        CombinedNoticeRepository(
            localNoticeRepository,
            networkNoticeRepository
        )
    }
}