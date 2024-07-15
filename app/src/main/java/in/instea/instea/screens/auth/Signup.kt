package `in`.instea.instea.screens.auth

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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import `in`.instea.instea.composable.DropdownMenuBox
import `in`.instea.instea.data.AuthViewModel
import `in`.instea.instea.data.DataSource.departments
import `in`.instea.instea.data.DataSource.semesters
import `in`.instea.instea.data.DataSource.universities
import `in`.instea.instea.data.FeedViewModel
import `in`.instea.instea.data.viewmodel.signupViewModel
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.ui.theme.DarkColors

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
fun PasswordTextField(
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
        value = password,
        onValueChange = { it ->
            onPasswordChange(it)
//            isPasswordError = it.isValidPassword()
        },
        keyboardOptions = KeyboardOptions(

            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done,

            ),
        keyboardActions = KeyboardActions(
            onDone = { localFocusmanager.clearFocus() }
        ),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            // Password visibility toggle icon
            PasswordVisibilityToggleIcon(
                showPassword = showPassword,
                onTogglePasswordVisibility = { showPassword = !showPassword })
        },
        isError = isError,
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

@Composable
fun CheckboxComp(navController: NavController) {
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
                if (checkedState.value) {
                    navController.navigate("Guidelines")
                }
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
                        navController.navigate("Guidelines")
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    textField: String,
    OnTextFieldChange: (String) -> Unit,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    textFieldLabel: String = "Enter your text",
    errorText: String = "Invalid input",
    isError: Boolean = false
) {
    // State variables to manage password visibility and validity
    val localFocusmanager = LocalFocusManager.current
//    var isInputError by remember { mutableStateOf<Boolean>(false) }

    OutlinedTextField(
        value = textField,
        onValueChange = {
            OnTextFieldChange(it)
//            isInputError = !TextFieldValue(it).isValidInput()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next,
        ),
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = "icon")
        },
        keyboardActions = KeyboardActions(
            onNext = {
                localFocusmanager.clearFocus()
            }
        ),
        label = { Text(text = textFieldLabel) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
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


@Composable
fun Signup(
    viewModel: AuthViewModel,
    feedViewmodel: FeedViewModel,
    navController: NavController,
    signupviewModel: signupViewModel
) {
    val authState = viewModel.authState.collectAsState()
    val signupUiState by signupviewModel.uiState.collectAsState()

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
        val btnEnabled by remember { derivedStateOf { !signupUiState.nameError && !signupUiState.usernameError && !signupUiState.emailError && !signupUiState.passError && signupUiState.departmenr.isNotEmpty() && signupUiState.university.isNotEmpty() && signupUiState.semester.isNotEmpty() } }

        HeadingText(Modifier, "Create an Account")
        Spacer(modifier = Modifier.height(10.dp))

        CustomTextField(
            textField = signupUiState.name,
            OnTextFieldChange = {
                signupviewModel.updateName(it)
                signupviewModel.nameValid(it)
            },
            icon = Icons.Default.Person,
            textFieldLabel = "Name",
            errorText = "Name is invalid ",
            isError = signupUiState.nameError
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomTextField(
            textField = signupUiState.emailid,
            OnTextFieldChange = {
                signupviewModel.updateEmail(it)
                signupviewModel.mailValid(it)
            },
            icon = Icons.Default.Email,
            keyboardType = KeyboardType.Email,
            textFieldLabel = "Email",
            errorText = "Invalid Email",
            isError = signupUiState.emailError
        )
        Spacer(modifier = Modifier.height(5.dp))
        CustomTextField(
            textField = signupUiState.username,
            OnTextFieldChange = {
                signupviewModel.updateUsername(it)
                signupviewModel.usernameValid(it)
            },
            icon = Icons.Default.Person,
            textFieldLabel = "Username",
            errorText = "Invalid username",
            isError = signupUiState.usernameError
        )
        Spacer(modifier = Modifier.height(5.dp))
        DropdownMenuBox(
            label = "University",
            options = universities,
            selectedOption = signupUiState.university,
            onOptionSelected = {
                signupviewModel.updateUniversity(it)
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        DropdownMenuBox(
            label = "Department",
            options = departments,
            selectedOption = signupUiState.departmenr,
            onOptionSelected = {
                signupviewModel.updateDepartment(it)
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        DropdownMenuBox(
            label = "Semester",
            options = semesters,
            selectedOption = signupUiState.semester,
            onOptionSelected = {
                signupviewModel.updateSemester(it)
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(60.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        PasswordTextField(
            password = signupUiState.password,
            onPasswordChange = {
                signupviewModel.updatePassword(it)
                signupviewModel.passValid(it)
            },
            errorColor = MaterialTheme.colorScheme.error,
            textFieldLabel = "Enter your password",
            errorText = "Password not valid",
            isError = signupUiState.passError
        )

        CheckboxComp(navController = navController)
        ButtonComp(
            value = "Signup",
            onButtonClicked = {
                viewModel
                    .SignUpWithEmailAndPassword(
                        signupUiState.emailid!!,
                        signupUiState.password
                    ) { success ->
                        if (success) {
                            FeedViewModel().writeNewUser(
                                signupUiState.name,
                                signupUiState.emailid,
                                signupUiState.username,
                                signupUiState.university,
                                signupUiState.departmenr,
                                signupUiState.semester
                            )
                        }
                    }

            },
            isEnabled = btnEnabled
        )
        DividerTextComp()
        ScreenChangeText(modifier = Modifier, navController = navController)


    }
}

//@Preview(showBackground = true)
//@Composable
//private fun CustomfieldPreview() {
//    val authViewModel: AuthViewModel = viewModel()
//    val signupviewModel: signupViewModel = viewModel()
//    val feedViewmodel: FeedViewModel = viewModel()
//    val navController = rememberNavController()
//    Signup(
//        viewModel = authViewModel,
//        feedViewmodel = feedViewmodel,
//        navController = navController,
//        signupviewModel = signupviewModel
//    )
//}