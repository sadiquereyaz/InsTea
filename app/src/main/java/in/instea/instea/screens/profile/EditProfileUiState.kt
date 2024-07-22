package `in`.instea.instea.screens.profile

import `in`.instea.instea.data.datamodel.User
import kotlinx.coroutines.flow.flowOf

data class EditProfileUiState(
//    var user: User = User(),
    var username: String = "",
    var university: String = "",
    var department: String = "",
    var semester: String = "",
    var email: String ="",
    var instagram: String = "",
    var linkedin: String = "",
    var whatsappNo: Int = 0,
    var about: String = "",
    var isLoading: Boolean = false,
    var errorMessage: String? = null,
    var isSuccess: Boolean = false
)