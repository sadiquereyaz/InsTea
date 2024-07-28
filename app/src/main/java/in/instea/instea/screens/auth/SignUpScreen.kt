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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import `in`.instea.instea.composable.DropdownButtonComposable
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.SignUpViewModel
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.screens.auth.composable.ButtonComp
import `in`.instea.instea.screens.auth.composable.CustomTextField
import `in`.instea.instea.screens.auth.composable.HeadingText
import `in`.instea.instea.screens.auth.composable.PasswordTextField
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
//    val authState = viewModel.authState.collectAsState()
    val viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState by viewModel.uiState.collectAsState()

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

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate(InsteaScreens.SignIn.name) {
                popUpTo(InsteaScreens.Signup.name) { inclusive = true }
            }
        }
    }
    val scrollState = rememberScrollState(0) // Remember the scroll state
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var university by rememberSaveable {
        mutableStateOf(
            uiState.selectedUniversity ?: "University"
        )
    }
    var department by rememberSaveable {
        mutableStateOf(
            uiState.selectedDepartment ?: "Department"
        )
    }
    var semester by rememberSaveable { mutableStateOf(uiState.selectedSemester ?: "Semester") }
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
                value = username,
                onValueChange = {
                    username = it
                },
                leadingIcon = Icons.Default.Person,
                label = "Username",
                errorText = "Invalid username",
//                isError = signupUiState.usernameError
            )
            //email
            CustomTextField(
                modifier = Modifier,
                value = email,
                onValueChange = { email = it },
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                label = "Enter Email",
                errorText = "Invalid Email"
            )

            PasswordTextField(
                modifier = Modifier,
                password = password,
                onPasswordChange = { password = it },
                textFieldLabel = "Enter your password",
                errorText = "Password not valid",
            )
            // outline button university
            DropdownButtonComposable(
                modifier = Modifier,
                text = university,
                options = uiState.universityList,
                isLoadingOption = uiState.isUniversityLoading,
//                leadingIcon = Icons.Default.AccountBalance,
//                selectedOption = university,
                onOptionSelected = { selectedOption ->
                    university = selectedOption
                    viewModel.getAllDepartment(university = selectedOption)
                    department = ""
                    semester = ""
                },
                onAddItemClicked = { navController.navigate(InsteaScreens.AddAcademicInfo.name) }
            )
            // department and semester
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //department
                DropdownButtonComposable(
                    modifier = Modifier.weight(3f),
                    text = department,
                    options = uiState.departmentList,
                    isLoadingOption = uiState.isDepartmentLoading,
                    onOptionSelected = { selectedOption ->
                        department = selectedOption
                        viewModel.getAllSemester(university = university, department = selectedOption)
                        semester = ""
                    },
                    onAddItemClicked = { navController.navigate(InsteaScreens.AddAcademicInfo.name) }
                )
                // semester
                DropdownButtonComposable(
                    modifier = Modifier.weight(2.5f),
                    text = semester,
                    options = uiState.semesterList,
                    isLoadingOption = uiState.isSemesterLoading,
                    onOptionSelected = { selectedOption ->
                        semester = selectedOption
                    },
                    onAddItemClicked = { navController.navigate(InsteaScreens.AddAcademicInfo.name) }
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))

        ButtonComp(
            text = "Sign Up",
            onButtonClicked = {
                coroutineScope.launch {
                    viewModel.signUp(
                        user = User(
                            username = username,
                            email = email,
                            university = university,
                            dept = department,
                            sem = semester
                        ),
                        password = password,
                    )
                }
            },
            isEnabled = !(email.isBlank() || username.isBlank() || password.isEmpty() || university.isBlank() || department.isBlank() || semester.isBlank())
        )

        TextButton(
            modifier = Modifier.padding(top = 8.dp),
//            enabled = false,
            onClick = {
                coroutineScope.launch {
                    navController.popBackStack()
                    navController.navigate(InsteaScreens.SignIn.name)
                }
            },
        ) {
            Text(text = "Already have account? SignIn")
        }
        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
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