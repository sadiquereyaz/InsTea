package `in`.instea.instea.screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import `in`.instea.instea.composable.DropdownMenuBox
import `in`.instea.instea.data.AuthViewModel
import `in`.instea.instea.data.FeedViewModel

import `in`.instea.instea.model.InsteaScreens
import `in`.instea.instea.ui.theme.DarkColors

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
fun MyTextField(
    labelValue: String,
    icon: ImageVector,
    textState: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Text,

    onValueChange: (String) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            value = textState.value,
            onValueChange = {
                textState.value = it
                onValueChange(it)
            },
            label = { Text(text = labelValue) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = "Icon"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Next,
                autoCorrect = false
            ),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),

            )
    }
}


@Composable
fun PasswordTextField(
    password: TextFieldValue,
    onPasswordChange: (TextFieldValue) -> Unit,
    errorColor: Color = MaterialTheme.colorScheme.error,
    textFieldLabel: String = "Enter your password",
    errorText: String = "Password not valid",
) {
    // State variables to manage password visibility and validity
    val localFocusmanager = LocalFocusManager.current
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
        label = { Text(textFieldLabel) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(8.dp),
        singleLine = true

    )
}

@Composable
fun PasswordVisibilityToggleIcon(
    showPassword: Boolean,
    onTogglePasswordVisibility: () -> Unit,
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
    onOptionSelected: (String) -> Unit,
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
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val isDarkMode = isSystemInDarkTheme()
        val textColor = if (isDarkMode) Color.White else Color.Black
        val checkedState = remember {
            mutableStateOf(false)
        }
        Checkbox(checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
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
            ),
            onClick = { offset ->
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

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.DarkGray,
            thickness = 1.dp
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text = "or",
            fontSize = 18.sp,
            color = textColor
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            color = Color.DarkGray,
            thickness = 1.dp
        )
    }
}

@Composable
fun ScreenChangeText(
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    viewModel: AuthViewModel,
    navController: NavController
) {
    val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black
    val initialtxt = "Already have an account ? "
    val logintxt = "Login"
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
        style = TextStyle(
            color = textColor
        ),
        modifier = Modifier.padding(16.dp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.also { span ->
                    if (span.item == logintxt) {
                        navController.navigate("Login")


                    }

                }
        })
}

@Composable
fun ButtonComp(value: String, onButtonClicked: () -> Unit, isEnabled: Boolean = true) {
    val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black
    Button(
        onClick = {
            onButtonClicked()
        },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .padding(15.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ) {
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
fun Signup(viewModel: AuthViewModel, feedViewmodel: FeedViewModel, navController: NavController) {
    val authState = viewModel.authState.collectAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is `in`.instea.instea.data.AuthState.authenticated -> navController.navigate(
                InsteaScreens.Feed.name
            )

            is `in`.instea.instea.data.AuthState.authenticated -> navController.navigate(
                InsteaScreens.Signup.name
            )

            else -> Unit
        }


    }
    val scrollState = rememberScrollState(0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 28.dp, end = 28.dp, bottom = 10.dp)
            .verticalScroll(scrollState)
    ) {

        val optionsUniversity = listOf("Jamia Millia Islamia ", "JNU", "AMU")
        val optionsSemester =
            listOf("SEM I", "SEM II", "SEM III", "SEM IV", "SEM V", "SEM VI", "SEM VII", "SEM VIII")
        val optionsDept =
            listOf("Computer Science", "Electronics & Comm", "Civil", "Mechanical", "Electrical")

        val emailState = rememberSaveable { mutableStateOf<String>("") }
        val name = rememberSaveable { mutableStateOf<String>("") }
        val username = rememberSaveable { mutableStateOf<String>("") }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        val department = rememberSaveable { mutableStateOf<String>("") }
        val semester = rememberSaveable { mutableStateOf<String>("") }
        val university = rememberSaveable { mutableStateOf<String>("") }
        WelcomeText(Modifier, "Hey There")
        HeadingText(Modifier, "Create an Account")
        Spacer(modifier = Modifier.height(10.dp))
        MyTextField(labelValue = "Name",
            icon = Icons.Default.Person,
            textState = name,
            keyboardType = KeyboardType.Text,
            onValueChange = {})
        MyTextField(
            labelValue = "Email Id",
            icon = Icons.Default.Email,
            textState = emailState,
            keyboardType = KeyboardType.Email,
            onValueChange = { emailState.value = it })
        MyTextField(
            labelValue = "Username",
            icon = Icons.Default.Person,
            textState = username,
            keyboardType = KeyboardType.Text,
            onValueChange = {})



        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuBox(
            label = "University",
            options = optionsUniversity,
            selectedOption = university.value,
            onOptionSelected = { university.value = it },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenuBox(
            label = "Department",
            options = optionsDept,
            selectedOption = department.value,
            onOptionSelected = {
                department.value = it
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DropdownMenuBox(
            label = "Semester",
            options = optionsSemester,
            selectedOption = semester.value,
            onOptionSelected = {
                semester.value = it
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(60.dp)
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
                viewModel
                    .SignUpWithEmailAndPassword(
                        emailState.value!!,
                        password.toString()
                    ) { success ->
                        if (success) {
                            FeedViewModel().writeNewUser(
                                name.value,
                                emailState.value,
                                username.value,
                                university.value,
                                department.value,
                                semester.value)
                        }
                    }

            },
            isEnabled = true
        )
        DividerTextComp()
        ScreenChangeText( modifier = Modifier,viewModel = AuthViewModel(),navController=navController)


    }
}



