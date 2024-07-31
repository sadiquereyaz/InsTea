package `in`.instea.instea.screens.auth


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import `in`.instea.instea.R
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.SignInUiState
import `in`.instea.instea.data.viewmodel.SignInViewModel
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.screens.auth.composable.ButtonComp
import `in`.instea.instea.screens.auth.composable.CustomTextField
import `in`.instea.instea.screens.auth.composable.HeadingText
import `in`.instea.instea.screens.auth.composable.PasswordTextField
import `in`.instea.instea.screens.auth.composable.UnderlinedTextComp

@Composable
fun SignInScreen(
    navController: NavHostController,

    ) {
    val viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val signInUiState by viewModel.uiState.collectAsState()
    var email by rememberSaveable { mutableStateOf("mah@gmail.com") }
    var password by rememberSaveable { mutableStateOf("ssssss") }
    val isLoading = viewModel.isLoading.value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Spacer(modifier = Modifier.height(50.dp))
        Text(
            text = "sign in",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(),
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal
            ),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(30.dp))
        CustomTextField(
            modifier = Modifier,
            value = email,
            onValueChange = { email = it },
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            label = "Enter Email",
            errorText = "Invalid Email"
        )

        PasswordTextField(
            password = password,
            onPasswordChange = { password = it },
            errorColor = MaterialTheme.colorScheme.error,
            textFieldLabel = "Enter your password",
            errorText = "Password not valid"
        )
        Spacer(modifier = Modifier.height(14.dp))
        UnderlinedTextComp(
            value = "Forgot Password?",
            onTextClicked = {
                navController.navigate(InsteaScreens.Forget.name)
            })
        ButtonComp(
            text = "Login",
            onButtonClicked = {
                viewModel.signIn(email = email, password = password)
                navController.navigate(InsteaScreens.Feed.name)
            },
            isEnabled = true
        )
        when (signInUiState) {
            is SignInUiState.Loading -> {
                // Show loading indicator
            }

            is SignInUiState.Success -> {
                // Navigate to the next screen
//                navController.navigate(InsteaScreens.Feed.name)
            }

            is SignInUiState.Error -> {
                // Show error message
                val errorMessage = (signInUiState as SignInUiState.Error).message
                // Show the error message on the UI
            }

            else -> {}
        }
    }
}



