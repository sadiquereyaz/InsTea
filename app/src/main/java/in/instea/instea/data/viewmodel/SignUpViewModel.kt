package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.AcademicRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.auth.SignUpUiState
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

    fun getAllDepartment(university: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDepartmentLoading = true,
                    departmentExpandable = false,
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
                        universityErrorMessage = result.errorMessage,
                        departmentExpandable = true
                    )
                }
            }
        }
    }

    fun getAllSemester(university: String, department: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    semesterList = emptyList(),
                    isSemesterLoading = true,
                    semesterExpandable = false,
                    semesterErrorMessage = null
                )
            }
            academicRepository.getAllSemester(university, department).collect { result ->
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

    fun signUp(user: User, password: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = userRepository.signUp(user, password)
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(isSuccess = true, isLoading = false)
                    }
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message ?: "An error occurred"
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
}