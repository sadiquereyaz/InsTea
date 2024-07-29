package `in`.instea.instea.screens.auth

data class SignUpUiState(
    var username: String = "",
    var usernameErrorMessage: String? = null,

    var password: String = "",
    var passwordErrorMessage: String? = null,

    var email: String = "",
    var emailErrorMessage: String? = null,

    var universityList: List<String> = emptyList(),
    var isUniversityLoading: Boolean = true,
    var universityErrorMessage: String? = null,
    var universityExpandable: Boolean = false,
    var selectedUniversity: String? = null,

    var departmentList: List<String> = emptyList(),
    var isDepartmentLoading: Boolean = false,
    var departmentErrorMessage: String? = null,
    var departmentExpandable: Boolean = false,
    var selectedDepartment: String? = null,


    var semesterList: List<String> = emptyList(),
    var isSemesterLoading: Boolean = false,
    var semesterErrorMessage: String? = null,
    var semesterExpandable: Boolean = false,
    var selectedSemester: String? = null,


    var isSignUpLoading: Boolean = false,
    var errorMessage: String? = null,
    var isSuccess: Boolean = false,

    var isButtonEnabled: Boolean = username.isNotBlank()
)