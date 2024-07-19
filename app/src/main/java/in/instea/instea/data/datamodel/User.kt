package `in`.instea.instea.data.datamodel

data class User(
    val userId: String? = null,
    val email: String? = null,
    val username: String? = null,
    var password: String? = null,
    var university: String? = null,
    var dept: String? = null,
    var sem: String? = null,
    var about: String? = null,
    var instaId: String? = null,
    var linkedinId: String? = null,
    val hostel: String? = null,
    val roomNo: String? = null
)