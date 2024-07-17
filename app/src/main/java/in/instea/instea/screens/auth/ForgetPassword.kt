package `in`.instea.instea.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.instea.instea.data.AuthViewModel

@Composable
fun ForgetPass(
    viewModel: AuthViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        var emailState = rememberSaveable {
            mutableStateOf("")
        }
        CustomTextField(
            textField = emailState.value,
            OnTextFieldChange = {emailState.value=it},
            icon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            textFieldLabel = "Email Id",
            errorText = "Invalid Email"
            )


        ButtonComp(value = "Reset Password",
            onButtonClicked = {
                viewModel.resetPassword(emailState.value)

                navController.navigate("Login")


            }
        )

    }

}

