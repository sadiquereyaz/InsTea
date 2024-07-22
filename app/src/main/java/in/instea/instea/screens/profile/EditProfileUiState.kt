package `in`.instea.instea.screens.profile

data class EditProfileUiState(
//    var user: User = User(),
    var username: String? = "",
    var university: String = "",
    val universityList: List<String> = emptyList(),
    var department: String = "",
    val departmentList: List<String> = emptyList(),
    var semester: String = "",
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