package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.AccountService
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.navigation.InsteaScreens
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val userRepository: UserRepository,
    private val accountService: AccountService
) : InsteaAppViewModel() {

    private val _uiState = MutableStateFlow<SignInUiState>(SignInUiState.Idle)
    val uiState: StateFlow<SignInUiState> = _uiState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = userRepository.signIn(email, password)
            if (result.isSuccess) {
                val userId = result.getOrNull()
                val userObj: Flow<User> = userRepository.getUserById(userId!!)
                userRepository.upsertUserLocally(userObj.firstOrNull()!!)
            }
        }
    }

    fun onSignUpWithGoogle(credential: Credential, openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                accountService.signInWithGoogle(googleIdTokenCredential.idToken)
                openAndPopUp(InsteaScreens.UserInfo.name, InsteaScreens.Authenticate.name)
            } else {
                Log.e(ERROR_TAG, UNEXPECTED_CREDENTIAL)
            }
        }
    }

    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (accountService.hasUser()) {
            launchCatching {
                if (accountService.isUserProfileComplete()) {
                    openAndPopUp(InsteaScreens.Feed.name, InsteaScreens.Authenticate.name)
                } else {
                    openAndPopUp(InsteaScreens.UserInfo.name, InsteaScreens.Authenticate.name)
                }
            }
        }
    }
}

const val ERROR_TAG = "NOTES APP ERROR"
const val UNEXPECTED_CREDENTIAL = "Unexpected type of credential"

open class InsteaAppViewModel : ViewModel() {
    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.d(ERROR_TAG, throwable.message.orEmpty())
            },
            block = block
        )
}

sealed class SignInUiState {
    object Idle : SignInUiState()
    object Loading : SignInUiState()
    data class Success(val user: User) : SignInUiState()
    data class Error(val message: String) : SignInUiState()
}