package `in`.instea.instea.presentation.auth


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.di.AppViewModelProvider
import `in`.instea.instea.presentation.auth.composable.AuthenticationViewModel
import `in`.instea.instea.presentation.components.AuthenticationButton
import `in`.instea.instea.presentation.components.LoaderComponent
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    navigateToFeed: () -> Unit,
    viewModel: AuthenticationViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToUserInfo: () -> Unit,
    snackBarHostState: SnackbarHostState
) {

    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

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
                        onClicked = {
//                            viewModel.showSnackBar()
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar("Please wait...")
                            }
                        },
                        authenticationError = { viewModel.onSignUpError(it) },
                        onGetCredentialResponse = { credential ->
                            viewModel.onSignUpWithGoogle(credential)
                        },
                    )
                }
            }

            is AuthUiState.Loading -> {
                // Show loading indicator
                LoaderComponent()
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



