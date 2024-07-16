package `in`.instea.instea.screens.profile

import `in`.instea.instea.data.datamodel.PostData
//import `in`.instea.instea.data.datamodel.RoomPostModel
import `in`.instea.instea.data.datamodel.UserModel

//data class ProfileUiState(
//    val userName: String = "No Name",
//    val selectedDepartment: String = "Computer Science",
//    val selectedSemester: String = "V",
//    val selectedHostel: String = "A Block",
//    val instagram: String = "johndoe_insta",
//    val linkedin: String = "johndoe_linkedin",
//    var savedPosts: List<PostData> = listOf(),
//)

//sealed interface ProfileUiState {
//    data class Success(val savedPosts: List<PostData>) : ProfileUiState
//    object Error : ProfileUiState
//    object Loading : ProfileUiState
//}
data class ProfileUiState(
    var savedPosts: List<PostData>? = listOf(), //savedPost can be null
    var userData: UserModel? = UserModel()      //userDate can be null
)
