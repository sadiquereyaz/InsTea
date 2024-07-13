package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import `in`.instea.instea.InsteaApplication

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
                OtherProfileViewModel(
                    postRepository = insteaApplication().container.postRepository,
                    userRepository =insteaApplication().container.userRepository
                )
        }
    }
}

fun CreationExtras.insteaApplication(): InsteaApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as InsteaApplication)
