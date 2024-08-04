package `in`.instea.instea.screens.profile

data class EditProfileUiState(
//    var user: User = User(),
    var username: String = "",
    var usernameErrorMessage: String? = null,

    var selectedUniversity: String = "",
    val universityList: List<String> = emptyList(),

    var selectedDepartment: String = "",
    val departmentList: List<String> = emptyList(),

    var selectedSemester: String = "",
    val semesterList: List<String> = emptyList(),

    var email: String ="",
    var instagram: String = "",
    var linkedin: String = "",
    var whatsappNo: String = "",
    var about: String = "",
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var isSuccess: Boolean = false
)