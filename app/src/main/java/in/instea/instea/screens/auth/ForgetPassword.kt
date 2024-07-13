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
        MyTextField(
            labelValue = "Email Id",
            icon = Icons.Default.Email,
            textState = emailState,
            keyboardType = KeyboardType.Email,
            onValueChange = { emailState.value = it })

        ButtonComp(value = "Reset Password",
            onButtonClicked = {
                viewModel.resetPassword(emailState.value)

                navController.navigate("Login")


            }
        )

    }

}

//@Preview
//@Composable
//private fun PreviewForgetPass() {
//    ForgetPass()
//}