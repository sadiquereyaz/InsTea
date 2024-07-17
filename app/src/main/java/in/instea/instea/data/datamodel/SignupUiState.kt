package `in`.instea.instea.data.datamodel

data class SignupUiState(
    var name:String="",
    var emailid:String="",
    var username:String="",
    var university:String="",
    var department:String="",
    var semester:String="",
    var password:String="",
    var nameError:Boolean=false,
    var emailError:Boolean=false,
    var usernameError:Boolean=false,
    var passError:Boolean=false,
    var isDeptEnabled:Boolean=false,
    var isSemEnabled: Boolean=false
)
data class university(
    val name: String
)