package `in`.instea.instea.screens.auth

data class SignUpUiState(
    var universityList: List<String> = emptyList(),
    var isUniversityLoading: Boolean = false,
    var universityErrorMessage: String? = null,
    var selectedUniversity: String? = null,

    var departmentList: List<String> = emptyList(),
    var isDepartmentLoading: Boolean = false,
    var departmentErrorMessage: String? = null,
    var selectedDepartment: String? = null,


    var semesterList: List<String> = emptyList(),
    var isSemesterLoading: Boolean = false,
    var semesterErrorMessage: String? = null,
    var selectedSemester: String? = null,


    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var isSuccess: Boolean = false
)