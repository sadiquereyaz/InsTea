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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import `in`.instea.instea.composable.DropdownComposable
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

    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    val launcher = rememberFirebaseAuthLauncher(

    )

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
//    var username by rememberSaveable { mutableStateOf("") }
//    var email by rememberSaveable { mutableStateOf("") }
//    var password by rememberSaveable { mutableStateOf("") }
//    var university by rememberSaveable { mutableStateOf("") }
//    var department by rememberSaveable { mutableStateOf("") }
//    var semester by rememberSaveable { mutableStateOf("") }
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
                value = uiState.username,
                onValueChange = {
                    coroutineScope.launch {
                        viewModel.onUserNameChanged(it.trim())
                    }
                },
                leadingIcon = Icons.Default.Person,
                label = "Username",
                errorMessage = uiState.usernameErrorMessage,
//                isError = uiState.usernameErrorMessage != null
            )
            //email
            CustomTextField(
                modifier = Modifier,
                value = uiState.email,
                onValueChange = {
                    coroutineScope.launch { viewModel.onEmailChanged(it.trim()) }
                },
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email,
                label = "Enter Email",
                errorMessage = uiState.emailErrorMessage,
                supportingText = "OTP will be sent for the verification"
            )

            PasswordTextField(
                modifier = Modifier,
                password = uiState.password,
                onPasswordChange = {
                    coroutineScope.launch {
                        viewModel.onPasswordChanged(it.trim())
                    }
                },
                textFieldLabel = "Enter your password",
                errorMessage = uiState.passwordErrorMessage,
            )
            // university
            DropdownComposable(
                modifier = Modifier.fillMaxWidth(),
                label = "University",
                options = uiState.universityList,
                onOptionSelected = { selectedOption ->
                    viewModel.onUniversitySelect(university = selectedOption)
                },
                leadingIcon = Icons.Default.AccountBalance,
                isError = uiState.universityErrorMessage != null,
                errorMessage = uiState.universityErrorMessage,
                selectedOption = uiState.selectedUniversity,
                onAddItemClicked = { navController.navigate(InsteaScreens.AddAcademicInfo.name) },
                isLoadingOption = uiState.isUniversityLoading,
//                isEnabled = !uiState.isUniversityLoading,
                isExpandable = uiState.universityExpandable
            )
            // department and semester
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                //department
                DropdownComposable(
                    modifier = Modifier.weight(3f),
                    label = "Department",
                    options = uiState.departmentList,
                    onOptionSelected = { department ->
//                        department = selectedOption
                        viewModel.onDepartmentSelected(
//                            university = university,
                            /*department = */department
                        )
//                        semester = ""
                    },
                    leadingIcon = Icons.Default.School,
                    isError = uiState.departmentErrorMessage != null,
                    errorMessage = uiState.departmentErrorMessage ?: "",
                    selectedOption = uiState.selectedDepartment ?: "",
                    onAddItemClicked = {
                        navController.navigate(InsteaScreens.AddAcademicInfo.name)
                    },
                    isEnabled = uiState.selectedUniversity != null,
                    isLoadingOption = uiState.isDepartmentLoading,
                    isExpandable = uiState.departmentExpandable
                )
                // semester
                DropdownComposable(
                    modifier = Modifier.weight(2.5f),
                    label = "Semester",
                    options = uiState.semesterList,
                    onOptionSelected = {
                        viewModel.onSemesterSelected(it)
                    },
                    leadingIcon = Icons.Default.AutoGraph,
                    selectedOption = uiState.selectedSemester ?: "",
                    onAddItemClicked = {
                        navController.navigate(InsteaScreens.AddAcademicInfo.name)
                    },
                    isEnabled = uiState.selectedDepartment != null,
                    isLoadingOption = uiState.isSemesterLoading,
                    isExpandable = uiState.semesterExpandable
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))

        ButtonComp(
            text = "Send OTP",
            isLoading = uiState.isSignIngIn,
            onButtonClicked = {
                coroutineScope.launch {
//                    viewModel.signIn(navController)
                    viewModel.signInWithGoogle{result ->
                        if (result) {
                            // Sign-in successful, navigate to the next screen
                        } else {
                            // Handle sign-in failure
                        }

                    }
                }
            },
            isEnabled = (uiState.email.isNotBlank() && uiState.username.isNotBlank() &&
                    uiState.password.isNotBlank() && uiState.selectedSemester != null &&
                    uiState.usernameErrorMessage == null && uiState.passwordErrorMessage == null &&
                    uiState.emailErrorMessage == null && uiState.errorMessage == null
                    )
        )

        TextButton(
            modifier = Modifier.padding(top = 8.dp),
//            enabled = false,
            onClick = {
                coroutineScope.launch {
//                    navController.popBackStack()
//                    navController.navigate(InsteaScreens.SignIn.name)
                    viewModel.signInWithGoogle{result ->
                        if (result) {
                            // Sign-in successful, navigate to the next screen
                            navController.navigate(InsteaScreens.SignIn.name)
                        } else {
                            // Handle sign-in failure
                        }

                    }
                }
            },
        ) {
            Text(text = "Already have account? SignIn")
        }

        // Signup screen error
        if (uiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.errorMessage ?: "",
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