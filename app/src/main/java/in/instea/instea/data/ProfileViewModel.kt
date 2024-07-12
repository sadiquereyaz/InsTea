package `in`.instea.instea.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.model.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(private val authViewModel: AuthViewModel) : ViewModel( ) {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onUserNameChanged(newName: String) {
//        Log.d("user change", "new name is $newName")
        _uiState.update { currentState ->
            currentState.copy(userName = newName)
        }
//        _uiState.value = _uiState.value.copy(userName = newName)
    }

    fun onDepartmentChange(newDepartment: String) {
        _uiState.value = _uiState.value.copy(selectedDepartment = newDepartment)
    }

    fun onSemesterChange(newSemester: String) {
        _uiState.value = _uiState.value.copy(selectedSemester = newSemester)
    }

    fun onUniversityChange(newUniversity: String) {
        _uiState.value = _uiState.value.copy(selectedUniversity = newUniversity)
    }



}