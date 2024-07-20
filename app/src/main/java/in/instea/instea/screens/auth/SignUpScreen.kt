package `in`.instea.instea.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.instea.instea.composable.DropdownMenuBox
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.SignUpViewModel
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.screens.auth.composable.ButtonComp
import `in`.instea.instea.screens.auth.composable.CustomTextField
import `in`.instea.instea.screens.auth.composable.HeadingText
import `in`.instea.instea.screens.auth.composable.PasswordTextField

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
//    val authState = viewModel.authState.collectAsState()
    val viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val signUpUiState by viewModel.signUpUiState.collectAsState()

    /*   LaunchedEffect(authState.value) {
           when (authState.value) {
               is AuthState.authenticated -> navController.navigate(
                   InsteaScreens.Feed.name
               )

               is AuthState.authenticated -> navController.navigate(
                   InsteaScreens.Signup.name
               )
               else -> Unit
           }
       }*/
    val scrollState = rememberScrollState(0) // Remember the scroll state
    var username by rememberSaveable { mutableStateOf("sad") }
    var email by rememberSaveable { mutableStateOf("sad@gmail.com") }
    var password by rememberSaveable { mutableStateOf("ssssss") }
    var university by rememberSaveable { mutableStateOf("JMI") }
    var department by rememberSaveable { mutableStateOf("CSE") }
    var semester by rememberSaveable { mutableStateOf("V") }
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        HeadingText(value = "Create Account")
        Spacer(modifier = Modifier.height(30.dp))
        // Academic Composable
        Column(
            modifier = Modifier,
//            verticalArrangement =
        ) {
            //username
            CustomTextField(
                modifier = Modifier,
                textField = username,
                onTextFieldChange = {
                    username = it
                },
                icon = Icons.Default.Person,
                textFieldLabel = "Username",
                errorText = "Invalid username",
//                isError = signupUiState.usernameError
            )
            //email
            CustomTextField(
                modifier = Modifier,
                textField = email,
                onTextFieldChange = { email = it },
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                textFieldLabel = "Enter Email",
                errorText = "Invalid Email"
            )

            PasswordTextField(
                modifier = Modifier,
                password = password,
                onPasswordChange = { password = it },
                textFieldLabel = "Enter your password",
                errorText = "Password not valid",
            )
            // university
            DropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                label = "University",
                options = signUpUiState.universityList,
                leadingIcon = Icons.Default.AccountBalance,
                selectedOption = university,
                onOptionSelected = { selectedOption ->
                    university = selectedOption
                    viewModel.getAllDepartment(selectedOption)
                    department = ""
                    semester = ""
                },
            )
            // department and semester
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //department
                DropdownMenuBox(
                    modifier = Modifier.weight(3f),
                    label = "Department",
                    selectedOption = department,
                    leadingIcon = Icons.Default.School,
                    options = signUpUiState.departmentList,
                    onOptionSelected = { selectedOption ->
                        department = selectedOption
                        viewModel.getAllSemester(
                            university = university,
                            department = selectedOption
                        )
                        semester = ""
                    },
                    errorMessage = "Please select university first"
                )
                // semester
                DropdownMenuBox(
                    modifier = Modifier.weight(2.5f),
                    label = "Sem",
                    leadingIcon = Icons.Default.AutoGraph,
                    options = signUpUiState.semesterList,
                    selectedOption = semester,
                    onOptionSelected = {
                        semester = it
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))

        ButtonComp(
            text = "Sign Up",
            onButtonClicked = {
                viewModel.signUp(
                    User(
                        username = username,
                        email = email,
                        university = university,
                        dept = department,
                        sem = semester
                    ),
                    password,
                    moveToSignIn = {
                            navController.navigate(InsteaScreens.SignIn.name)
                    }
                )
            },
            isEnabled = true
        )

    }
}


//@Preview(showBackground = true)
//@Composable
//private fun CustomfieldPreview() {
//    val authViewModel: AuthViewModel = viewModel()
//    val signupviewModel: signupViewModel = viewModel()
//    val feedViewmodel: FeedViewModel = viewModel()
//    val navController = rememberNavController()
//    SignUpScreen(
//        navController = navController,
//    )
//}