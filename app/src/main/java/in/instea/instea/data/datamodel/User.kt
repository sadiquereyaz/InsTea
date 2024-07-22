package `in`.instea.instea.data.datamodel

data class User(
    val userId: String? = null,
    val email: String? = null,
    val username: String? = null,
    val university: String? = null,
    val dept: String? = null,
    val userPosts: MutableList<String?> = mutableListOf(),
    val sem: String? = null,
    var about: String? = null,
    val instaId: String? = null,
    val linkedinId: String? = null,
    val whatsappNo: Int? = 0,
    val hostel: String? = null,
    val roomNo: String? = null
)