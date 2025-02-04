package `in`.instea.instea.presentation.auth

sealed class AuthUiState {
    data class Idle(var showSnackBar: Boolean  = false, val snackBarMsg: String = "Please wait...") : AuthUiState()
    data object Loading : AuthUiState()
    data class Success(val isNewUser: Boolean = true) : AuthUiState()
    data class Error(val message: String,val isInternetOn:Boolean = false) : AuthUiState()

}