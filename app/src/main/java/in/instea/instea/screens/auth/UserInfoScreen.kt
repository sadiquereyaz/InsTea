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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.instea.instea.composable.DropdownComposable
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.UserInfoViewModel
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.screens.auth.composable.ButtonComp
import `in`.instea.instea.screens.auth.composable.CustomTextField
import `in`.instea.instea.screens.auth.composable.HeadingText
import kotlinx.coroutines.launch

@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
//    val authState = viewModel.authState.collectAsState()
    val viewModel: UserInfoViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
            navController.navigate(InsteaScreens.Feed.name) {
                popUpTo(InsteaScreens.Authenticate.name) { inclusive = true }
            }
        }
    }
    val scrollState = rememberScrollState(0) // Remember the scroll state
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        HeadingText(value = "A little more")
        Spacer(modifier = Modifier.height(30.dp))
        // Academic Composable
        Column(
            modifier = Modifier,
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
            text = "Let's Go",
            isLoading = uiState.isSignIngIn,
            onButtonClicked = {
                coroutineScope.launch {
                    viewModel.signIn()
                }
            },
            isEnabled = (uiState.username.isNotBlank() &&
                     uiState.selectedSemester != null &&
                    uiState.usernameErrorMessage == null &&
                     uiState.errorMessage == null
                    )
        )

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