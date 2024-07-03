package `in`.instea.instea.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern

@Composable
fun WelcomeText(modifier: Modifier = Modifier, value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        textAlign = TextAlign.Center
    )

}

@Composable
fun HeadingText(modifier: Modifier = Modifier, value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        textAlign = TextAlign.Center
    )

}

@Composable
fun MyTextField(labelvalue: String, Icon: ImageVector) {
    val textValue = rememberSaveable {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),

        label = { Text(text = labelvalue) },
        value = textValue.value,
        keyboardOptions = KeyboardOptions(
            imeAction=ImeAction.Next,
            autoCorrect = false),


        onValueChange = {
            textValue.value = it
        },
        leadingIcon = {
            Icon(
                modifier = Modifier
                    .height(20.dp),
                imageVector = Icon,
                contentDescription = ""
            )
        })
}

@Composable
fun PasswordTextField(
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    errorColor: Color = MaterialTheme.colorScheme.error,
    textFieldLabel: String = "Enter your password",
    errorText: String = "Password not valid"
) {
    // State variables to manage password visibility and validity
    var showPassword by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(true) }

    // TextField for entering user password
    OutlinedTextField(
        value = password,
        onValueChange = { it ->
            onPasswordChange(it)
            isPasswordError = it.isValidPassword()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            // Password visibility toggle icon
            PasswordVisibilityToggleIcon(
                showPassword = showPassword,
                onTogglePasswordVisibility = { showPassword = !showPassword })
        },
        isError = !isPasswordError,
        supportingText = {
            // Display error text if the password is not valid
            if (!isPasswordError) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = errorText,
                    color = errorColor
                )
            }
        },
        label = { Text(textFieldLabel) },
        modifier = Modifier
            .fillMaxWidth()

    )
}

@Composable
fun PasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    // Determine the icon based on password visibility
    val image = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    val contentDescription = if (showPassword) "Hide password icon" else "Show password icon"

    // IconButton to toggle password visibility
    IconButton(onClick = onTogglePasswordVisibility) {
        Icon(imageVector = image, contentDescription = contentDescription)
    }
}

/**
 * Extension function to check if the [TextFieldValue] represents a valid password.
 */
fun TextFieldValue.isValidPassword(): Boolean {
    val password = text
    val passwordRegex =
        Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")

    return password.matches((passwordRegex).toRegex())
}

@Composable
fun DropDownMenu(
    modifier: Modifier = Modifier,
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CheckboxComp() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val checkedState = remember {
            mutableStateOf(false)
        }
        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value != checkedState.value
            })
        val initialText = "By continuing you accept our"
        val policytext = "Privacy Policy"
        val TandC = "Terms and Use"

        val annotatedString = buildAnnotatedString {
            append(initialText)
            withStyle(style = SpanStyle(color = Color.Blue)) {
                pushStringAnnotation(
                    tag = policytext,
                    annotation = policytext
                )
                append(policytext)
            }
            append(" and ")
            withStyle(style = SpanStyle(color = Color.Blue)) {
                pushStringAnnotation(
                    tag = TandC,
                    annotation = TandC
                )
                append(TandC)
            }
        }
        ClickableText(text = annotatedString, onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also {
                    // to open TandC page or policy Page
                }
        })
    }
}

@Composable
fun LoginText(
    modifier: Modifier = Modifier
        .fillMaxWidth()
) {
    val initialtxt = "Already have an account ?"
    val logintxt = "Login"
    val annotatedString = buildAnnotatedString {
        append(initialtxt)
        withStyle(style = SpanStyle(color = Color.Blue)) {
            pushStringAnnotation(
                tag = logintxt,
                annotation = logintxt
            )
            append(logintxt)
        }
    }
    ClickableText(text = annotatedString,
        modifier = Modifier.padding(16.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    if (span.item == logintxt) {
                        //Code to shift from signup tologin

                    }

                }
        })
}

@Composable
fun ButtonComp(value: String) {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .padding(20.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(Color.Gray, Color.DarkGray)),
                    shape = RoundedCornerShape(50.dp)
                )
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun Signup(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
            .background(color = Color.White)
    ) {

        val optionsUniversity = listOf("Jamia Millia Islamia ", "JNU", "AMU")
        val optionsSemester =
            listOf("SEM I", "SEM II", "SEM III", "SEM IV", "SEM V", "SEM VI", "SEM VII", "SEM VIII")
        val optionsDept =
            listOf("Computer Science", "Electronics & Comm", "Civil", "Mechanical", "Electrical")

        WelcomeText(Modifier, "Hey There")
        HeadingText(Modifier, "Create an Account")
        Spacer(modifier = Modifier.height(20.dp))
        MyTextField(labelvalue = "Name", Icons.Default.Person)
        MyTextField(labelvalue = "Email Id", Icons.Default.Email)
        MyTextField(labelvalue = "Username", Icons.Default.Person)
        var password by remember { mutableStateOf(TextFieldValue("")) }

        PasswordTextField(
            password = password,
            onPasswordChange = { password = it },
            errorColor = MaterialTheme.colorScheme.error,
            textFieldLabel = "Enter your password",
            errorText = "Password not valid"
        )

        DropDownMenu(
            label = "University",
            options = optionsUniversity,
            selectedOption = " "
        ) {

        }
        DropDownMenu(
            label = "Department",
            options = optionsDept,
            selectedOption = " "
        ) {

        }
        DropDownMenu(
            label = "Semester",
            options = optionsSemester,
            selectedOption = " "
        ) {

        }

        CheckboxComp()
        ButtonComp(value = "Signup")
        LoginText(modifier = Modifier)


    }
}


@Preview(showBackground = true)
@Composable
fun SignupPreview(modifier: Modifier = Modifier) {
    Signup(Modifier)
}
