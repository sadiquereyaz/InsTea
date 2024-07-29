package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.AcademicRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.auth.SignUpUiState
import `in`.instea.instea.utility.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository,
    private val academicRepository: AcademicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    init {
        getAllUniversity()
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

    fun signUp() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isSendingOtp = true)
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
                    _uiState.update {
                        it.copy(isSuccess = true, isSendingOtp = false)
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isSendingOtp = false,
                            errorMessage = e.message
                        )
                    }
                }
            )
        }
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