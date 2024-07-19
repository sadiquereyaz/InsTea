package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Idle)
    val uiState: StateFlow<SignInUiState> = _uiState

    suspend fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val user = userRepository.signIn(email, password)
            _uiState.value = when (user) {
                null -> SignInUiState.Error("Sign In Failed")
                else -> SignInUiState.Success(User())
            }
        }
    }
}

sealed class SignInUiState {
    object Idle : SignInUiState()
    data class Success(val user: User) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
}
