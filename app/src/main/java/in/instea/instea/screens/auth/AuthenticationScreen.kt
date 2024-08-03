package `in`.instea.instea.screens.auth


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.composable.AuthenticationButton
import `in`.instea.instea.composable.Loader
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.AuthenticationViewModel

@Composable
fun AuthenticationScreen(
    navigateToFeed: () -> Unit,
    viewModel: AuthenticationViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToUserInfo: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var isAuthenticating = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.onAppStart()
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is AuthUiState.Idle -> {
                Column {
                    AuthenticationButton(
                        authenticationError = { viewModel.onSignUpError(it) },
                        onGetCredentialResponse = { credential ->
                            viewModel.onSignUpWithGoogle(credential)
                        },
                    )
                }
            }

            is AuthUiState.Loading -> {
                // Show loading indicator
                Loader()
            }

            is AuthUiState.Success -> {
                val isNewUser = (uiState as AuthUiState.Success).isNewUser
                if (isNewUser) {
                    navigateToUserInfo()
                } else {
                    navigateToFeed()
                }
            }

            is AuthUiState.Error -> {
                // Show error message
                val errorMessage = (uiState as AuthUiState.Error).message

                Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                    TextButton(onClick = { viewModel.onAppStart() }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "refresh")
                        Text(text = "Refresh")
                    }
                }
            }
        }
    }

}



