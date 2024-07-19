package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Idle)
    val uiState: StateFlow<SignInUiState> = _uiState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.signIn(email, password)
            if (result.isSuccess) {
                val userObj: Flow<User> = userRepository.getUserById(result.getOrNull()!!)//getting user obj from firebase
                userRepository.upsertUserLocally(userObj.firstOrNull()!!)
                Log.d("CURRENT", userObj.firstOrNull()!!.username!!)
                Log.d("LOCAL_UID", userRepository.getCurrentUserId().firstOrNull()!!)
                Log.d("USERNAME", userRepository.getUserById(userRepository.getCurrentUserId().firstOrNull()!!).firstOrNull()!!.username!!)
            }
            Log.d("USER ID", result.getOrNull().toString())
        }
    }
}

sealed class SignInUiState {
    object Idle : SignInUiState()
    object Loading : SignInUiState()
    data class Success(val user: User) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
}