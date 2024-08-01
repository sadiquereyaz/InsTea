package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Idle)
    val uiState: StateFlow<SignInUiState> = _uiState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = SignInUiState.Loading

            val result = userRepository.signIn(email, password)
            if (result.isSuccess) {
                val userId = result.getOrNull()
                val userObj: Flow<User> = userRepository.getUserById(userId!!)
                userRepository.upsertUserLocally(userObj.firstOrNull()!!)

                _uiState.value = SignInUiState.Success(userObj.firstOrNull()!!)
            } else {
                _uiState.value = SignInUiState.Error(
                    result.exceptionOrNull()?.message ?: "Sign in failed"
                ) // Emit Error state
            }
        }
    }
}

sealed class SignInUiState {
    object Idle : SignInUiState()
    object Loading : SignInUiState()
    data class Success(val user: User) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
}