package `in`.instea.instea.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.AccountService
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.auth.AuthUiState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val userRepository: UserRepository,
    private val accountService: AccountService,
    private val context: Context
) : InsteaAppViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Loading)
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onSignUpWithGoogle(credential: Credential) {
        launchCatching {
//            Log.d("AUTH_VM", "onSignUpWithGoogle executed")
            _uiState.value = AuthUiState.Loading
            if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                accountService.signInWithGoogle(googleIdTokenCredential.idToken)
                val userId = Firebase.auth.currentUser!!.uid
                val result = userRepository.isUserIdAvailable(userId)
                result.onSuccess { successMsg ->
                    if (successMsg.isNullOrBlank()) {
                        //uid not present in realtime
                        accountService.updateDisplayName("")
                        _uiState.value = AuthUiState.Success(isNewUser = true)

                    } else {
                        // uid is present in realtime db
                        val userObj: Flow<User> = userRepository.getUserById(userId)
                        userRepository.upsertUserLocally(userObj.firstOrNull()!!)
                        _uiState.value = AuthUiState.Success(isNewUser = false)
                    }
                }.onFailure {
                    Log.d(
                        "UID_FETCH ERROR",
                        it.localizedMessage ?: "error while checking user info"
                    )
                    _uiState.value =
                        AuthUiState.Error(it.localizedMessage ?: "Error checking user info")
                    // TODO show toast message
                }
            } else {
                _uiState.value = AuthUiState.Error(UNEXPECTED_CREDENTIAL)
                Log.e(ERROR_TAG, UNEXPECTED_CREDENTIAL)
            }
        }
    }

    fun onAppStart() {
//        _uiState.value = AuthUiState.Loading
        launchCatching {
            if (accountService.isUserProfileComplete()) {
                if (NetworkUtils.isNetworkAvailable(context = context)) {
                    _uiState.value = AuthUiState.Success(isNewUser = false) //navigate to feed
                } else {
                    onSignUpError("Please connect to the internet!")
                }
            }else{
                _uiState.value = AuthUiState.Idle
            }
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    fun onSignUpError(message: String) {
        _uiState.value = AuthUiState.Error(message = message)
    }
}

const val ERROR_TAG = "INSTEA APP ERROR"
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

