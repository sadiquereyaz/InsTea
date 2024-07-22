package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.profile.EditProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    savedStateHandle: SavedStateHandle,
    val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState
    private val userId = Firebase.auth.currentUser?.uid.toString()

    init {
        fetchInitialInfo()
    }

    fun onUsernameChanged(userName: String) {
        _uiState.value = _uiState.value.copy(username = userName)
    }
    fun onDepartmentChanged(department: String) {
        _uiState.value = _uiState.value.copy(department = department)
    }
    private fun fetchInitialInfo() {
        viewModelScope.launch {
            userRepository.getUserById(userId).collect { user ->
                _uiState.update {
                    it.copy(
                        username = user.username ?: "",
                        university = user.university ?: "",
                        department = user.dept ?: "",
                        semester = user.sem ?: "",
                        email = user.email ?: "",
                        instagram = user.instaId ?: "",
                        linkedin = user.linkedinId ?: "",
                        whatsappNo = user.whatsappNo ?: 0,
                        about = user.about ?: "",
                    )
                }
            }
        }
    }
}