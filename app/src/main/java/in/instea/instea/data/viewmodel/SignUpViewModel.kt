package `in`.instea.instea.data.viewmodel

import android.content.Context
import android.provider.Settings.Global.getString
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.AcademicRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.auth.SignUpUiState
import `in`.instea.instea.utility.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class SignUpViewModel(
    private val userRepository: UserRepository,
    private val academicRepository: AcademicRepository,
    context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()


    private lateinit var googleSignInClient: GoogleSignInClient
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)

//        getAllUniversity()
    }

    fun signInWithGoogle(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val signInIntent = googleSignInClient.signInIntent
            startActivity(context., signInIntent, null)
            _uiState.update {
                it.copy(
                    isSignIngIn = false,
                    errorMessage = signInIntent.toString()
                )
            }
            /*try {
                val signInIntent = googleSignInClient.signInIntent
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(signInIntent)
                    val account = task.getResult(ApiException::class.java)
                    val result = firebaseAuthWithGoogle(account)
                    onResult(result)
                } catch (e: ApiException) {
                    _uiState.update {
                        it.copy(
                            isSignIngIn = false,
                            errorMessage = "Google sign-in failed: ${e.statusCode} - ${e.localizedMessage}\""
                        )
                    }
                    onResult(false)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSignIngIn = false,
                        errorMessage = "Unexpected error: ${e.localizedMessage}"
                    )
                }
                onResult(false)
            }*/
        }
    }

    private suspend fun firebaseAuthWithGoogle(account: GoogleSignInAccount): Boolean {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return try {
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user
            if (user != null) {
                // Update user information in your repository
                /*  userRepository.updateUserWithGoogleInfo(
                      User(
                          username = user.displayName ?: "",
                          email = user.email ?: "",
                          university = _uiState.value.selectedUniversity,
                          dept = _uiState.value.selectedDepartment,
                          sem = _uiState.value.selectedSemester
                      )
                  )*/
                _uiState.update {
                    it.copy(
                        isSignIngIn = false,
                        errorMessage = "Firebase authenticated"
                    )
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isSignIngIn = false,
                    errorMessage = "Firebase authentication failed: ${e.message}"
                )
            }
            false
        }
    }

    private fun getAllUniversity() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isUniversityLoading = true,
                )
            }
            academicRepository.getAllUniversity().collect { result ->
                _uiState.update { currState ->
                    currState.copy(
                        universityList = result.stringList,
                        universityErrorMessage = result.errorMessage,
                        isUniversityLoading = false,
                        universityExpandable = true
                    )
                }
            }
        }
    }

    fun onUniversitySelect(university: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedUniversity = university,
                    selectedDepartment = null,
                    selectedSemester = null,
                    isDepartmentLoading = true,
                    departmentExpandable = false,
                    semesterExpandable = false,
                    departmentList = emptyList(),
                    semesterList = emptyList(),
                    departmentErrorMessage = null,
                    semesterErrorMessage = null,
                    isSemesterLoading = false,
                )
            }
            academicRepository.getAllDepartment(university).collect { result ->
                _uiState.update { currState ->
                    currState.copy(
                        departmentList = result.stringList,
                        isDepartmentLoading = false,
                        departmentErrorMessage = result.errorMessage,
                        departmentExpandable = true
                    )
                }
            }
        }
    }

    fun onDepartmentSelected(department: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedDepartment = department,
                    semesterList = emptyList(),
                    selectedSemester = null,
                    isSemesterLoading = true,
                    semesterExpandable = false,
                    semesterErrorMessage = null
                )
            }
            academicRepository.getAllSemester(_uiState.value.selectedUniversity!!, department)
                .collect { result ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            semesterList = result.stringList,
                            isSemesterLoading = false,
                            semesterErrorMessage = result.errorMessage,
                            semesterExpandable = true
                        )
                    }
                }
        }
    }

    fun onSemesterSelected(sem: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedSemester = sem,
                    semesterErrorMessage = null
                )
            }
        }
    }

    fun signIn(navController: NavController) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSignIngIn = true)
            }
            val values = _uiState.value
            val result: Result<String?> = userRepository.signUp(
                user = User(
                    username = values.username,
                    email = values.email,
                    university = values.selectedUniversity,
                    dept = values.selectedDepartment,
                    sem = values.selectedSemester
                ),
                password = values.password
            )
            result.fold(
                onSuccess = {
                    signInWithGoogle() { result ->
                        _uiState.update {
                            it.copy(isSuccess = result, isSignIngIn = false)
                        }
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isSignIngIn = false,
                            errorMessage = e.message
                        )
                    }
                }
            )
        }
    }

    private fun signInWithGoogle(any: Any) {
        TODO("Not yet implemented")
    }

    fun addItem(semester: String, department: String, university: String) {
        viewModelScope.launch {
            academicRepository.addClassDetail(semester, department, university)
        }
    }

    fun onUserNameChanged(username: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(username = username) }
            var usernameErrorMsg: String? =
                Validator.validateUsername(username)        //format check
            if (usernameErrorMsg == null) {
                val existenceResult: Result<String?> = userRepository.isUserNameAvailable(username)
                existenceResult.onSuccess { usernameErrorMsg = it }
                    .onFailure { usernameErrorMsg = it.message }
            }
            _uiState.update { it.copy(usernameErrorMessage = usernameErrorMsg) }
        }
    }

    fun onEmailChanged(email: String) {
        viewModelScope.launch {
            val errorMsg = Validator.validateEmail(email)
            _uiState.update {
                it.copy(
                    email = email,
                    emailErrorMessage = errorMsg,
                    errorMessage = null
                )
            }
        }
    }

    fun onPasswordChanged(pas: String) {
        _uiState.update {
            it.copy(password = pas, errorMessage = null)
        }
    }

}