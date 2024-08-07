package `in`.instea.instea.screens.auth

import `in`.instea.instea.data.datamodel.User

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data class Success(val isNewUser: Boolean = true) : AuthUiState()
    data class Error(val message: String,val isInternetOn:Boolean = false) : AuthUiState()

}