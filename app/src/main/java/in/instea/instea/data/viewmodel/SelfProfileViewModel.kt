package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.repo.PostRepository
import `in`.instea.instea.data.repo.UserPreferenceRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.model.UserModel
import `in`.instea.instea.screens.profile.ProfileUiState
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow


class SelfProfileViewModel(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    private val userId: Int = 12345

companion object{
    private const val TIMEOUT_MILLIS = 5_000L
}
    val profileUiState: StateFlow<ProfileUiState> = combine(
        userRepository.getUserById(userId).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserModel()
        ),
        postRepository.getAllSavedPostsStream().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = emptyList()
        )
    ) { userDetails, savedPosts ->
        // This lambda now returns ProfileUiState directly
        ProfileUiState(savedPosts = savedPosts, userData = userDetails)
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ProfileUiState()
        )
}