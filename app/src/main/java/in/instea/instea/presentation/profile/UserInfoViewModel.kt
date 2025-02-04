package `in`.instea.instea.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.domain.datamodel.User
import `in`.instea.instea.domain.repo.AcademicRepository
import `in`.instea.instea.domain.repo.AccountService
import `in`.instea.instea.domain.repo.UserRepository
import `in`.instea.instea.presentation.auth.UserInfoUiState
import `in`.instea.instea.utility.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserInfoViewModel(
    private val userRepository: UserRepository,
    private val academicRepository: AcademicRepository,
    private val accountService: AccountService
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserInfoUiState())
    val uiState: StateFlow<UserInfoUiState> = _uiState.asStateFlow()

    private val currentUserRef = Firebase.auth.currentUser!!
    val isUserInfoScreen = currentUserRef.displayName.isNullOrBlank()

    init {
        if (!isUserInfoScreen) {
            fetchInitialInfo()
        }
        getAllUniversity()
    }

    private fun fetchInitialInfo() {
        viewModelScope.launch {
            _uiState.value.isLoading = true
            userRepository.getUserById(currentUserRef.uid).collect { user ->
                _uiState.update { currState ->
                    currState.copy(
                        dpId = user.dpId,
                        username = user.username ?: "",
                        selectedUniversity = user.university ?: "",
                        selectedDepartment = user.dept ?: "",
                        selectedSemester = user.sem ?: "",
                        email = user.email ?: "",
                        instagram = user.instaId ?: "",
                        linkedin = user.linkedinId ?: "",
                        whatsappNo = user.whatsappNo ?: "",
                        about = user.about ?: "",
                    )
                }
                academicRepository.getAllDepartment(user.university ?: "")
                    .collect { departmentResult ->
                        _uiState.update {
                            it.copy(
                                departmentList = departmentResult.stringList,
                                isLoading = false,
                            )
                        }

                    }
            }
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
//                        universityExpandable = true
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
//                    departmentExpandable = false,
//                    semesterExpandable = false,
                    departmentList = emptyList(),
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
//                        departmentExpandable = true
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
                    selectedSemester = null,
                    isSemesterLoading = false,
//                    semesterExpandable = true,
                    semesterErrorMessage = null
                )
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

    fun save() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            val values = _uiState.value
            val addAcademicResult: Result<String?> = academicRepository.upsertInstitutes(
                values.selectedUniversity!!.trim(),
                values.selectedDepartment!!.trim()
            )
            addAcademicResult.fold(
                onSuccess = {
                    val userInsertResult: Result<String?> = userRepository.insertUser(
                        user = User(
                            dpId = values.dpId,
                            userId = accountService.currentUserId,
                            username = values.username,
                            email = accountService.currentEmail,
                            university = values.selectedUniversity,
                            dept = values.selectedDepartment,
                            sem = values.selectedSemester,
                            instaId = values.instagram,
                            linkedinId = values.linkedin,
                            whatsappNo = values.whatsappNo,
                            about = values.about
                        ),
                    )
                    userInsertResult.fold(
                        onSuccess = {
                            accountService.updateDisplayName(values.username)
                            _uiState.update {
                                it.copy(isSuccess = true, isLoading = true)
                            }
                        },
                        onFailure = { e ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = e.message
                                )
                            }
                            showSnackBar()
                        }
                    )
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = e.message
                        )
                    }
                    showSnackBar()
                }
            )
        }
    }

    private fun showSnackBar() {
        _uiState.value.showSnackBar = !_uiState.value.showSnackBar
    }

    fun onUsernameChanged(username: String) {
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

    fun onAboutChanged(about: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                about = about,
                aboutError = if (about.length > 120) "Maximum 120 characters are allowed!" else null
            )
        }
    }

    fun onWhatsappNoChanged(no: String) {
        _uiState.value = _uiState.value.copy(
            whatsappNo = no.trim(),
            whatsappError = Validator.validateWhatsapp(no)
        )
    }

    fun onInstagramChanged(ig: String) {
        _uiState.value =
            _uiState.value.copy(instagram = ig, instagramError = Validator.validateInstagram(ig))
    }

    fun onLinkedInChanged(it: String) {
        _uiState.value =
            _uiState.value.copy(linkedin = it, linkedInError = Validator.validateInstagram(it))
    }

    fun updateAcademicTF(university: String, dept: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedUniversity = university,
                    selectedDepartment = dept,
                    selectedSemester = ""
                )
            }
        }
    }

    fun setDpId(id: Int) {
        _uiState.update { it.copy(dpId = id) }
    }
}
