package `in`.instea.instea.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.domain.datamodel.User
import `in`.instea.instea.domain.repo.PostRepository
import `in`.instea.instea.domain.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface OtherProfileUiState {
    data class Success(
        val profilePosts: Flow<List<User>>?,
        val userData: Flow<User>?
    ) : OtherProfileUiState

    object Error : OtherProfileUiState
    object Loading : OtherProfileUiState
}

class OtherProfileViewModel(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val userId: String = "11"

    private val _otherProfileUiState = MutableStateFlow<OtherProfileUiState>(OtherProfileUiState.Loading)
    val otherProfileUiState: StateFlow<OtherProfileUiState> = _otherProfileUiState

    init {
        getUserDetail(userId)
//        getProfilePosts(userId)
    }

    private fun getUserDetail(userId: String) {
        viewModelScope.launch {
            _otherProfileUiState.value = OtherProfileUiState.Loading
            _otherProfileUiState.value = try {
                OtherProfileUiState.Success(
                    userData = userRepository.getUserById(userId),
                    profilePosts = null
                )
            } catch (e: IOException) {
                OtherProfileUiState.Error
            }
        }
    }
}