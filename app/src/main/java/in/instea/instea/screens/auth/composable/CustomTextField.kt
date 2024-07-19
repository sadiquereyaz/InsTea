package `in`.instea.instea.screens.auth.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    textField: String,
    onTextFieldChange: (String) -> Unit,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    textFieldLabel: String = "Enter your text",
    errorText: String = "Invalid input",
    isError: Boolean = false,
    leadingIcon: ImageVector = Icons.Default.Abc
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = textField,
        onValueChange = {
            onTextFieldChange(it)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next,
        ),
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = "icon")
        },
        label = { Text(text = textFieldLabel) },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        isError = isError,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        ),
        supportingText = {
            // Display error text if the input is not valid
            if (isError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

    )
}