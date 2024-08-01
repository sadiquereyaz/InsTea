package `in`.instea.instea.screens.auth


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import `in`.instea.instea.composable.AuthenticationButton
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.AuthenticationViewModel
import `in`.instea.instea.data.viewmodel.SignInUiState
import kotlinx.coroutines.delay

@Composable
fun AuthenticationScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: AuthenticationViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(true) {
//        delay(1000L)
        viewModel.onAppStart(openAndPopUp)
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    )
    {

        AuthenticationButton { credential ->
            viewModel.onSignUpWithGoogle(credential, openAndPopUp)
        }
        when (uiState) {
            is SignInUiState.Loading -> {
                // Show loading indicator
            }

            is SignInUiState.Success -> {
                // Navigate to the next screen
//                navController.navigate(InsteaScreens.Feed.name)
            }

            is SignInUiState.Error -> {
                // Show error message
                val errorMessage = (uiState as SignInUiState.Error).message
                // Show the error message on the UI
            }

            else -> {}
        }
    }
}



