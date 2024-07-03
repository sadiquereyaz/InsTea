package `in`.instea.instea.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun Login(){
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 28.dp,
            vertical = 100.dp)){
        WelcomeText(value = "Hey There")
        HeadingText(value="Welcome Back ")
        Spacer(modifier = Modifier
            .height(50.dp))
        MyTextField(labelvalue = "Username", Icons.Default.Person)
        var password by remember { mutableStateOf(TextFieldValue("")) }

        PasswordTextField(
            password = password,
            onPasswordChange = { password = it },
            errorColor = MaterialTheme.colorScheme.error,
            textFieldLabel = "Enter your password",
            errorText = "Password not valid"
        )
        ButtonComp(value = "Login")
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLogin(){
    Login()
}