package `in`.instea.instea.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.data.SignupUIEvent
import `in`.instea.instea.data.SignupViewModel
import `in`.instea.instea.ui.theme.DarkColors
import java.util.regex.Pattern


@Composable
fun WelcomeText(modifier: Modifier = Modifier, value: String) {
    val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        color = textColor,
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
    val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black
    Text(text = value, modifier = Modifier
            .fillMaxWidth()
            .heightIn(), style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = textColor, textAlign = TextAlign.Center)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextField(
    labelValue: String,
    icon: ImageVector,
    textState: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Text,

    onValueChange: (String) -> Unit,
    errorStatus: Boolean
) {
    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black
    val backgroundColor = if (isDarkMode) Color.DarkGray else Color.LightGray

    Column(modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                ,
            value = textState.value,
            onValueChange = {
                textState.value = it
                onValueChange(it)
            },
            label = { Text(text = labelValue, color = textColor) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "Icon",
                    tint = textColor
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            textStyle = TextStyle(color = textColor),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            isError =!errorStatus,

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor =  if (!errorStatus) Color.Red else textColor,
                unfocusedBorderColor =  if (!errorStatus) Color.Red else textColor,
                errorCursorColor = Color.Red,
                errorBorderColor = Color.Red
            )
        )
    }
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
    val localFocusmanager= LocalFocusManager.current
    var showPassword by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(true) }
    val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black

    // TextField for entering user password
    OutlinedTextField(

        value = password,
        onValueChange = { it ->
            onPasswordChange(it)
            isPasswordError = it.isValidPassword()
        },
        keyboardOptions = KeyboardOptions(

            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,

            ),
        keyboardActions = KeyboardActions {
            localFocusmanager.clearFocus()
        },
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
        label = { Text(textFieldLabel, color = textColor)  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (!isPasswordError) Color.Red else textColor,
            unfocusedBorderColor = if (!isPasswordError) Color.Red else textColor,
            errorCursorColor = Color.Red,
            errorBorderColor = Color.Red
        )



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
fun CheckboxComp() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(40.dp)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isDarkMode = isSystemInDarkTheme()
        val textColor = if (isDarkMode) Color.White else Color.Black
        val checkedState = remember {
            mutableStateOf(false)
        }
        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value =it
            })
        val initialText = "By continuing you accept our"
        val policytext = "Privacy Policy"
        val TandC = "Terms and Use"

        val annotatedString = buildAnnotatedString {
            append(initialText)
            withStyle(style = SpanStyle(color = DarkColors.primary)) {
                pushStringAnnotation(
                    tag = policytext,
                    annotation = policytext
                )
                append(policytext)
            }
            append(" and ")
            withStyle(style = SpanStyle(color = DarkColors.primary)) {
                pushStringAnnotation(
                    tag = TandC,
                    annotation = TandC
                )
                append(TandC)
            }
        }
        ClickableText(text = annotatedString,
            style = TextStyle(
                color = textColor
            ), onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also {
                    // to open TandC page or policy Page
                }
        })
    }
}

@Composable
fun DividerTextComp(modifier: Modifier = Modifier) {
    val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black
    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Divider(modifier= Modifier
            .fillMaxWidth()
            .weight(1f),
            color = Color.DarkGray,
            thickness = 1.dp)
        Text(modifier = Modifier.padding(8.dp),
            text = "or",
            fontSize = 18.sp,
            color = textColor
        )
        Divider(modifier= Modifier
            .fillMaxWidth()
            .weight(1f),
            color = Color.DarkGray,
            thickness = 1.dp)
    }
}
@Composable
fun ScreenChangeText(tryingLogin:Boolean =true,
                     modifier: Modifier = Modifier
                         .fillMaxWidth()
) { val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black
    val initialtxt = if(tryingLogin)"Already have an account ? " else "Don't have an account yet?"
    val logintxt = if(tryingLogin)"Login" else " Register"
    val annotatedString = buildAnnotatedString {
        append(initialtxt)
        withStyle(style = SpanStyle(color = DarkColors.primary)) {
            pushStringAnnotation(
                tag = logintxt,
                annotation = logintxt
            )
            append(logintxt)
        }
    }

    ClickableText(text = annotatedString,
        modifier = Modifier.padding(16.dp),
        style = TextStyle(
            color = textColor
        ),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    if (span.item == logintxt) {


                    }

                }
        })
}
@Composable
fun DropdownMenu(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier,
    errorStatus: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        val isDarkMode = isSystemInDarkTheme()

        // Define colors based on theme
        val backgroundColor = if (isDarkMode) Color.Black else Color.White
        val textColor = if (isDarkMode) Color.White else Color.Black
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown Icon",
                        tint = textColor
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            isError = !errorStatus,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor =  if (!errorStatus) Color.Red else textColor,
                unfocusedBorderColor =  textColor,
                errorCursorColor = Color.Red,
                errorBorderColor = Color.Red
            )
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
fun ButtonComp(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = true) {
    Button(
        onClick = {
            onButtonClicked()
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .padding(15.dp)
            ,
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ) {
        val isDarkMode = isSystemInDarkTheme()

        // Define colors based on theme

        val textColor = if (isDarkMode) Color.White else Color.Black
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .shadow(
                    4.dp,
                    shape = RoundedCornerShape(50.dp)
                )
                .background(
                    color = Color.DarkGray,
                    shape = RoundedCornerShape(50.dp)
                )
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}


@Composable
fun Signup(signupViewModel: SignupViewModel= viewModel(), modifier: Modifier = Modifier) {
    val isDarkMode = isSystemInDarkTheme()

    // Define colors based on theme
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black



    val scrollState = rememberScrollState(0)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .padding(28.dp)

            .verticalScroll(scrollState)
    ) {

        val optionsUniversity = listOf("Jamia Millia Islamia ", "JNU", "AMU")
        val optionsSemester =
            listOf("SEM I", "SEM II", "SEM III", "SEM IV", "SEM V", "SEM VI", "SEM VII", "SEM VIII")
        val optionsDept =
            listOf("Computer Science", "Electronics & Comm", "Civil", "Mechanical", "Electrical")
        var selectedUniversity by remember { mutableStateOf(optionsUniversity[0]) }
        var selectedDepartment by remember { mutableStateOf(optionsDept[0]) }
        var selectedSemester by remember { mutableStateOf(optionsSemester[0]) }

        val emailState = rememberSaveable { mutableStateOf<String>("") }
        val name = rememberSaveable { mutableStateOf<String>("") }
        val username = rememberSaveable { mutableStateOf<String>("") }
        var password by remember { mutableStateOf(TextFieldValue("")) }

        WelcomeText(Modifier, "Hey There")
        HeadingText(Modifier, "Create an Account")
        Spacer(modifier = Modifier.height(10.dp))
        MyTextField(labelValue = "Name",
            icon = Icons.Default.Person,
            textState = name,
            keyboardType = KeyboardType.Text,
            onValueChange = {
                signupViewModel.onEvent(SignupUIEvent.NameChanged(it))
            },
            signupViewModel.signupUiState.value.nameError
        )
        MyTextField(
            labelValue = "Email Id",
            icon = Icons.Default.Email,
            textState = emailState,
            keyboardType = KeyboardType.Email,
            onValueChange = {
                signupViewModel.onEvent(SignupUIEvent.EmailChanged(it))
            },
            signupViewModel.signupUiState.value.emailError)
        MyTextField(
            labelValue = "Username",
            icon = Icons.Default.Person,
            textState = username,
            keyboardType = KeyboardType.Text,
            onValueChange = {
                signupViewModel.onEvent(SignupUIEvent.UsernameChanged(it))
            },
            signupViewModel.signupUiState.value.usernameError)

        var selecteduniv=""

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenu(
            label = "University",
            options = optionsUniversity,
            selectedOption = selectedUniversity,
            onOptionSelected = {selectedOption->
                selectedUniversity=selectedOption
                signupViewModel.onEvent(SignupUIEvent.UnivChanged(selectedOption))},
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(56.dp),
            signupViewModel.signupUiState.value.univError
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(
            label = "Department",
            options = optionsDept,
            selectedOption = selectedDepartment,
            onOptionSelected = {selectedOption->
                selectedDepartment=selectedOption
                signupViewModel.onEvent(SignupUIEvent.DeptChanged(selectedOption))
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(56.dp),
            signupViewModel.signupUiState.value.deptError
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenu(
            label = "Semester",
            options = optionsSemester,
            selectedOption = selectedSemester,
            onOptionSelected = {selectedOption->
                selectedSemester=selectedOption
                signupViewModel.onEvent(SignupUIEvent.SemChanged(selectedOption))
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(56.dp),
            signupViewModel.signupUiState.value.semError
        )
        Spacer(modifier = Modifier.height(4.dp))
        PasswordTextField(
            password = password,
            onPasswordChange = { password = it },
            errorColor = MaterialTheme.colorScheme.error,
            textFieldLabel = "Enter your password",
            errorText = "Password not valid"
        )

        CheckboxComp()
        ButtonComp(
            value = "Signup",
            onButtonClicked = {
                signupViewModel.onEvent(SignupUIEvent.SignupButtonClicked)
            },
            isEnabled = true
        )
        DividerTextComp()
        ScreenChangeText(tryingLogin = true,modifier = Modifier)


    }
}


@Preview(showBackground = true)
@Composable
fun SignupPreview(modifier: Modifier = Modifier) {
    Signup(signupViewModel = SignupViewModel(),Modifier)
}