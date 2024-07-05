package `in`.instea.instea.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UnderlinedTextComp(value:String,modifier: Modifier = Modifier) {
    Text(text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(40.dp),
        style = TextStyle(
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ), color = Color.DarkGray,
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )
}

@Composable
fun Login() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 28.dp,
                vertical = 100.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
        
    ) {
        WelcomeText(value = "Hey There")
        HeadingText(value = "Welcome Back ")
        Spacer(
            modifier = Modifier
                .height(50.dp)
        )
        val username = rememberSaveable { mutableStateOf<String>("") }
        MyTextField(
            labelValue = "Username",
            icon = Icons.Default.Person,
            textState = username,
            keyboardType = KeyboardType.Text,
            onValueChange = {})
        var password by remember { mutableStateOf(TextFieldValue("")) }

        PasswordTextField(
            password = password,
            onPasswordChange = { password = it },
            errorColor = MaterialTheme.colorScheme.error,
            textFieldLabel = "Enter your password",
            errorText = "Password not valid"
        )
        Spacer(modifier = Modifier.height(14.dp))
        UnderlinedTextComp(value = "Forgot Password?")
        ButtonComp(value = "Login", onButtonClicked = {})
        DividerTextComp()
        ScreenChangeText(tryingLogin = false, modifier = Modifier
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    Login()
}