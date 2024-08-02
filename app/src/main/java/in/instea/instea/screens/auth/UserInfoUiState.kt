package `in`.instea.instea.screens.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class UserInfoUiState(
//    var username: String = Firebase.auth.currentUser!!.displayName ?: "",
    var username: String = "",
    var usernameErrorMessage: String? = null,

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


    var isSignIngIn: Boolean = false,
    var errorMessage: String? = null,
    var isSuccess: Boolean = false,

    var isButtonEnabled: Boolean = username.isNotBlank()
)