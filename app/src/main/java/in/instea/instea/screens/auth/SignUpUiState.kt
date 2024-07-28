package `in`.instea.instea.screens.auth

data class SignUpUiState(
    var universityList: List<String> = emptyList(),
    var isUniversityLoading: Boolean = true,
    var universityErrorMessage: String? = null,
    var universityExpandable: Boolean = false,
//    var selectedUniversity: String? = null,

    var departmentList: List<String> = emptyList(),
    var isDepartmentLoading: Boolean = false,
    var departmentErrorMessage: String? = null,
    var departmentExpandable: Boolean = false,
//    var selectedDepartment: String? = null,


    var semesterList: List<String> = emptyList(),
    var isSemesterLoading: Boolean = false,
    var semesterErrorMessage: String? = null,
    var semesterExpandable: Boolean = false,
//    var selectedSemester: String? = null,


    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var isSuccess: Boolean = false
)