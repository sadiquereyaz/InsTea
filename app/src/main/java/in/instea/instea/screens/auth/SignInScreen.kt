package `in`.instea.instea.screens.auth


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.SignInViewModel
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.screens.auth.composable.ButtonComp
import `in`.instea.instea.screens.auth.composable.CustomTextField
import `in`.instea.instea.screens.auth.composable.HeadingText
import `in`.instea.instea.screens.auth.composable.PasswordTextField
import `in`.instea.instea.screens.auth.composable.UnderlinedTextComp
import `in`.instea.instea.screens.auth.composable.WelcomeText

@Composable
fun SignInScreen(
    navController: NavHostController,

    ) {
    val viewModel: SignInViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val signInUiState by viewModel.uiState.collectAsState()
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 28.dp, vertical = 100.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    )
//    {
//        WelcomeText(value = "Hey There")
//        HeadingText(value = "Welcome Back ")
//        Spacer(
//            modifier = Modifier.height(50.dp)
//        )
//        val email = rememberSaveable { mutableStateOf("") }
//
//        CustomTextField(
//            textField = email.value,
//            OnTextFieldChange = {
//                email.value = it
//            },
//            icon = Icons.Default.Person,
//            keyboardType = KeyboardType.Email,
//            textFieldLabel = "Enter Username",
//            errorText = "Invalid Username"
//        )
//        var password by remember { mutableStateOf(TextFieldValue("")) }
//
//        PasswordTextField(
//            password = password.toString(),
//            onPasswordChange = { password = TextFieldValue(it) },
//            errorColor = MaterialTheme.colorScheme.error,
//            textFieldLabel = "Enter your password",
//            errorText = "Password not valid"
//        )
//        Spacer(modifier = Modifier.height(14.dp))
//        UnderlinedTextComp(
//            value = "Forgot Password?", navController.navigate(InsteaScreens.Forget.name)
//        )
//        ButtonComp(
//            value = "Login", onButtonClicked = {
//                viewModel.login(email.value, password.toString())
//                navController.navigate(InsteaScreens.Feed.name)
//
//            }, isEnabled = true
//        )
//    }
}



