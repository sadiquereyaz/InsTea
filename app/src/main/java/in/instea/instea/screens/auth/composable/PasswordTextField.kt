package `in`.instea.instea.screens.auth.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorColor: Color = MaterialTheme.colorScheme.error,
    textFieldLabel: String = "Enter your password",
    errorText: String = "Password not valid",
    isError: Boolean = false
) {
    // State variables to manage password visibility and validity
    val localFocusmanager = LocalFocusManager.current
    var showPassword by remember { mutableStateOf(false) }
//    var isPasswordError by remember { mutableStateOf(true) }

    // TextField for entering user password
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = password,
        onValueChange = { it ->
            onPasswordChange(it)

//            isPasswordError = it.isValidPassword()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
        ),
        keyboardActions = KeyboardActions(onDone = { localFocusmanager.clearFocus() }),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "password")},
        trailingIcon = {
            // Password visibility toggle icon
            PasswordVisibilityToggleIcon(
                showPassword = showPassword,
                onTogglePasswordVisibility = {
                    showPassword = !showPassword
                }
            )
        },
        isError = isError,
//        leadingIcon = Icons.Default.Lock,
        supportingText = {
            // Display error text if the password is not valid
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = errorColor
                )
            }
        },
        label = { Text(textFieldLabel) },
        shape = RoundedCornerShape(8.dp),
        singleLine = true

    )
}