package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.repo.AcademicRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.profile.EditProfileUiState
import `in`.instea.instea.utility.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    savedStateHandle: SavedStateHandle,
    val userRepository: UserRepository,
    private val academicRepository: AcademicRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState
    private val userId = Firebase.auth.currentUser?.uid.toString()

    init {
        fetchInitialInfo()
    }

    private fun fetchInitialInfo() {
        getUniversities()
        viewModelScope.launch {
            userRepository.getUserById(userId).collect { user ->
                _uiState.update { currState ->
                    currState.copy(
                        username = user.username ?: "",
                        university = user.university ?: "",
                        department = user.dept ?: "",
                        semester = user.sem ?: "",
                        email = user.email ?: "",
                        instagram = user.instaId ?: "",
                        linkedin = user.linkedinId ?: "",
                        whatsappNo = user.whatsappNo ?: "",
                        about = user.about ?: "",
                    )
                }
            }
        }
    }

    private fun getUniversities() {
        viewModelScope.launch {
            academicRepository.getAllUniversity().collect { result ->
                _uiState.value = _uiState.value.copy(
                    universityList = result.stringList,
                    errorMessage = result.errorMessage
                )
            }
        }
    }

    fun onUniversityChanged(university: String) {
        viewModelScope.launch {
            academicRepository.getAllDepartment(university).collect { result ->
                _uiState.value = _uiState.value.copy(
                    university = university,
                    departmentList = result.stringList,
                    department = "",
                    semester = ""
                )
            }
        }
    }

    fun onDepartmentChanged(department: String) {
        viewModelScope.launch {
            academicRepository.getAllSemester(
                department = department, university = _uiState.value.university
            ).collect { result ->
                _uiState.value = _uiState.value.copy(
                    semesterList = result.stringList, department = department, semester = ""
                )
            }
        }
    }

    fun onSemesterChanged(semester: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                semester = semester
            )
        }
    }

    fun onUsernameChanged(username: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(username = username.trim(), usernameErrorMessage = Validator.validateUsername(username))
        }
    }
    fun onAboutChanged(about: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(about = about.trim())
        }
    }

    fun onWhatsappNoChanged(no: String) {
        _uiState.value = _uiState.value.copy(whatsappNo = no.trim())
    }

    fun onInstagramChanged(ig: String) {
        _uiState.value = _uiState.value.copy(instagram = ig.trim())
    }

    fun onLinkedInChanged(it: String) {
        _uiState.value = _uiState.value.copy(linkedin = it.trim())
    }

    fun saveUserDetails() {
        viewModelScope.launch {
            val uiStateValue = uiState.value
            val user = User(
                userId = userId,
                username = uiStateValue.username,
                university = uiStateValue.university,
                dept = uiStateValue.department,
                sem = uiStateValue.semester,
                instaId = uiStateValue.instagram,
                linkedinId = uiStateValue.linkedin,
                whatsappNo = uiStateValue.whatsappNo,
                about = uiStateValue.about
            )
            userRepository.upsertUserLocally(user)
            userRepository.upsertUserToFirebase(user)
        }
    }
}