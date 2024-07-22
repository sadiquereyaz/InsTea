package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import `in`.instea.instea.InsteaApplication
import `in`.instea.instea.data.FeedViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ProfileViewModel
        initializer {
                SelfProfileViewModel(
                    postRepository = insteaApplication().container.postRepository,
                    userRepository =insteaApplication().container.userRepository
                )
        }
        initializer {
                EditProfileViewModel(
                    savedStateHandle = this.createSavedStateHandle(),
                    userRepository =insteaApplication().container.userRepository,
                    academicRepository = insteaApplication().container.academicRepository
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
                    scheduleRepository = insteaApplication().container.scheduleRepository
                )
        }
        initializer {
                SignInViewModel(
                    userRepository = insteaApplication().container.userRepository
                )
        }
        initializer {
                SignUpViewModel(
                    userRepository = insteaApplication().container.userRepository,
                    academicRepository = insteaApplication().container.academicRepository
                )
        }

        initializer {
            FeedViewModel(
                postRepository = insteaApplication().container.networkRepository
            )
        }
        initializer {
            EditScheduleViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                scheduleRepository = insteaApplication().container.scheduleRepository
            )
        }
    }
}

fun CreationExtras.insteaApplication(): InsteaApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InsteaApplication)
