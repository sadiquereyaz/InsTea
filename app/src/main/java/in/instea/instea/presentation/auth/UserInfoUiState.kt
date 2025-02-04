package `in`.instea.instea.presentation.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

data class UserInfoUiState(
    var dpId: Int = (0..15).random(),
    var username: String = Firebase.auth.currentUser!!.displayName ?: "",
//    var username: String = "",
    var usernameErrorMessage: String? = null,

    var universityList: List<String> = emptyList(),
    var isUniversityLoading: Boolean = true,
    var universityErrorMessage: String? = null,
//    var universityExpandable: Boolean = false,
    var selectedUniversity: String? = null,

    var departmentList: List<String> = emptyList(),
    var isDepartmentLoading: Boolean = false,
    var departmentErrorMessage: String? = null,
//    var departmentExpandable: Boolean = false,
    var selectedDepartment: String? = null,


    var semesterList: List<String> = listOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII"),
    var isSemesterLoading: Boolean = false,
    var semesterErrorMessage: String? = null,
//    var semesterExpandable: Boolean = true,
    var selectedSemester: String? = null,

    var email: String ="",
    var instagram: String = "",
    var linkedin: String = "",
    var whatsappNo: String = "",
    var about: String = "",
    var aboutError:String? = null,
    var whatsappError:String? = null,
    var instagramError:String? = null,
    var linkedInError:String? = null,


    var isLoading: Boolean = false,
    var isSuccess: Boolean = false,
    var errorMessage: String? = null,
    var showSnackBar: Boolean = false,

    var isButtonEnabled: Boolean = username.isNotBlank()
)