package `in`.instea.instea.data.viewmodel

import ChatviewModel
import NoticeViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import `in`.instea.instea.InsteaApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ProfileViewModel
        initializer {
                ProfileViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    postRepository = insteaApplication().container.postRepository,
                    userRepository =insteaApplication().container.userRepository,
                    context  = insteaApplication()
                )
        }
        initializer {
                OtherProfileViewModel(
                    postRepository = insteaApplication().container.postRepository,
                    userRepository =insteaApplication().container.userRepository
                )
        }
        initializer {
                ScheduleViewModel(
                    scheduleRepository = insteaApplication().container.scheduleRepository,
                    workManagerTaskRepository = insteaApplication().container.workManagerTaskRepository
                )
        }
        initializer {
                AuthenticationViewModel(
                    userRepository = insteaApplication().container.userRepository,
                    accountService = insteaApplication().container.accountService,
                    context = insteaApplication()
                )
        }
        initializer {
                UserInfoViewModel(
                    userRepository = insteaApplication().container.userRepository,
                    academicRepository = insteaApplication().container.academicRepository,
                    accountService = insteaApplication().container.accountService
                )
        }

        initializer {
            FeedViewModel(
                postRepository = insteaApplication().container.networkRepository,
                localPostRepository = insteaApplication().container.localPostRepository,
                context = insteaApplication()
            )
        }
        initializer {
            EditScheduleViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                scheduleRepository = insteaApplication().container.scheduleRepository,
                workManagerTaskRepository = insteaApplication().container.workManagerTaskRepository
            )
        }
        initializer {
            MoreViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                scheduleRepository = insteaApplication().container.scheduleRepository,
                userRepository = insteaApplication().container.userRepository,
                accountService = insteaApplication().container.accountService,
                context = insteaApplication()
            )
        }
        initializer {
            ChatviewModel(
                netWorkChatRepository = insteaApplication().container.netwrokChatRepository
            )
        }
        initializer {
            NoticeViewModel(noticeRepository = insteaApplication().container.noticeRepository)
        }
    }
}

fun CreationExtras.insteaApplication(): InsteaApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InsteaApplication)
