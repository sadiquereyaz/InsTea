package `in`.instea.instea.data

data class User(
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val university: String,
    val dept: String,
    val sem: String,
    val instaId: String?,
    val linkedinId: String?,
    val hostel: String?,
    val roomNo: String?
) {

}