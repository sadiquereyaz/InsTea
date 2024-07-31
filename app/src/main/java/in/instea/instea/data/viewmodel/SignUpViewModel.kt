package `in`.instea.instea.data.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.DataSource.departments
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    init {
        getAllUniversity()
    }

    private fun getAllUniversity() {
        viewModelScope.launch {
            academicRepository.getAllUniversity().collect { universities ->
                _uiState.update { currState->
                    currState.copy(
                        universityList = universities
                    )
                }
            }
        }
    }

    fun getAllDepartment(university: String) {
        viewModelScope.launch {
            uiState.value.departmentList = emptyList()
            academicRepository.getAllDepartment(university).collect {departments->
                _uiState.update { currState->
                    currState.copy(
                        departmentList = departments
                    )
                }
            }
        }
    }

    fun getAllSemester(university: String, department: String) {
        viewModelScope.launch {
            uiState.value.semesterList = emptyList()
            academicRepository.getAllSemester(university, department).collect {semester->
                _uiState.update {currentState->
                    currentState.copy(
                        semesterList = semester
                    )
                }
            }
        }
    }

    fun signUp(user: User, password: String, moveToSignIn: () -> Unit) {
        viewModelScope.launch {
            val result = userRepository.signUp(user, password)
            if (result.isSuccess) {
                moveToSignIn()
            } else { //TODO: show toast message of failure
            }
        }
    }

    fun addItem(semester: String, department: String, university: String) {
        viewModelScope.launch {
            academicRepository.addClassDetail(semester, department, university)
            _isLoading.value = false
        }
    }
}