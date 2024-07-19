package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.AcademicRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.auth.SignUpUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val userRepository: UserRepository,
    private val academicRepository: AcademicRepository
) : ViewModel() {

    val signUpUiState: StateFlow<SignUpUiState> =
        academicRepository.getAllUniversity()
            .map {
                SignUpUiState(universityList = it)
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = SignUpUiState()
            )

    fun getAllDepartment(university: String) {
        viewModelScope.launch {
            signUpUiState.value.departmentList = emptyList()
            academicRepository.getAllDepartment(university).collect {
                if (university == "AMU") signUpUiState.value.departmentList = it
            }
        }
    }

    fun getAllSemester(university: String, department: String) {
        viewModelScope.launch {
            signUpUiState.value.semesterList = emptyList()
            academicRepository.getAllSemester(university, department).collect {
                if (university == "AMU") signUpUiState.value.semesterList = it
            }
        }
    }

    fun signUp(user: User, moveToSignIn: () -> Unit) {
        viewModelScope.launch {
            val result = userRepository.signUp(user)
            if(result.isSuccess) {
                moveToSignIn()
            } else { //TODO: show toast message of failure
             }
        }
    }
}