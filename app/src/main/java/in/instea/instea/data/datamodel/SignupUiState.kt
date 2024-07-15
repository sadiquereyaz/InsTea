package `in`.instea.instea.data.datamodel

import androidx.compose.ui.text.input.TextFieldValue

data class SignupUiState(
    var name:String="",
    var emailid:String="",
    var username:String="",
    var university:String="",
    var departmenr:String="",
    var semester:String="",
    var password:String="",
    var nameError:Boolean=false,
    var emailError:Boolean=false,
    var usernameError:Boolean=false,
    var passError:Boolean=false,
    var isBtnEnabled:Boolean=false
)