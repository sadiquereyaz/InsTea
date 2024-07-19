package `in`.instea.instea.screens.auth

data class SignUpUiState(
//        val username: String = "defaultuser",
//        val email: String = "defaultemail@mail.com",
//        val password: String = "",
    var universityList: List<String> = emptyList(),
    var departmentList: List<String> = emptyList(),
    var semesterList: List<String> = emptyList(),
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var isSuccess: Boolean = false
)