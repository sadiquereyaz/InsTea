package `in`.instea.instea.screens.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.R
import `in`.instea.instea.composable.AddAcademicPopup
import `in`.instea.instea.composable.DropdownComposable
import `in`.instea.instea.composable.Loader
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.UserInfoViewModel
import `in`.instea.instea.screens.auth.composable.ButtonComp
import `in`.instea.instea.screens.auth.composable.CustomTextField
import kotlinx.coroutines.launch

@Composable
fun UserInfoScreen(
    modifier: Modifier = Modifier,
    openAndPopUp: () -> Unit,
    viewModel: UserInfoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    snackBarHostState: SnackbarHostState
) {
    val uiState by viewModel.uiState.collectAsState()
    val isBackButtonEnabled = remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var showPopUp by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.showSnackBar) {
        coroutineScope.launch {
            val message = uiState.errorMessage
            if (message != null) {
                snackBarHostState.showSnackbar(message = message)
            }
        }
    }
    BackHandler(enabled = isBackButtonEnabled.value) {
        // Your logic when back button is pressed
        openAndPopUp()
    }
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            openAndPopUp()
        }
    }
    val scrollState = rememberScrollState(0) // Remember the scroll state

    if (!uiState.isLoading) {
        Column(
            modifier = modifier
                .verticalScroll(scrollState)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier,
            ) {
                //username
                CustomTextField(
                    modifier = Modifier,
                    value = uiState.username,
                    onValueChange = {
                        coroutineScope.launch {
                            viewModel.onUsernameChanged(it.trim())
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
                    onAddItemClicked = {
                        showPopUp = true
                    },
                    isLoadingOption = uiState.isUniversityLoading,
                    //                isEnabled = !uiState.isUniversityLoading,
                    //                isExpandable = uiState.universityExpandable
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
                            showPopUp = true
                        },
                        isEnabled = uiState.selectedUniversity != null,
                        isLoadingOption = uiState.isDepartmentLoading,
                        //                    isExpandable = uiState.departmentExpandable
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
                        isEnabled = uiState.selectedDepartment != null,
                        isLoadingOption = uiState.isSemesterLoading,
                        //                    isExpandable = uiState.semesterExpandable,
                        showAddBtn = false
                    )
                }
            }

            if (!viewModel.isUserInfoScreen) {
                // about
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    value = uiState.about,
                    label = { Text(text = "About") },
                    minLines = 3,
                    onValueChange = { it ->
                        viewModel.onAboutChanged(about = it)
                    },
                    supportingText = {
                        val error = uiState.aboutError
                        if (!error.isNullOrBlank()) {
                            Text(
                                text = error
                            )
                        }
                    },
                )

                // whatsapp
                CustomTextField(
                    value = uiState.whatsappNo,
                    onValueChange = {
                        viewModel.onWhatsappNoChanged(it)
                    },
                    label = "Whatsapp (+91)",
                    leadingIcon = Icons.Default.Whatsapp,
                    keyboardType = KeyboardType.Phone,
                    errorMessage = uiState.whatsappError,
                )
                // instagram
                CustomTextField(
                    value = uiState.instagram,
                    onValueChange = {
                        viewModel.onInstagramChanged(it)
                    },
                    label = "Instagram username",
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.insta),
                    errorMessage = uiState.instagramError,

                    )
                // linkedIn
                CustomTextField(
                    value = uiState.linkedin,
                    onValueChange = {
                        viewModel.onLinkedInChanged(it)
                    },
                    label = "Linkedin username",
                    leadingIcon = ImageVector.vectorResource(id = R.drawable.linked),
                    errorMessage = uiState.linkedInError,
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
            Spacer(modifier = Modifier.weight(1f))
            ButtonComp(
                text = "Save",
                isLoading = uiState.isLoading,
                onButtonClicked = {
                    coroutineScope.launch {
                        viewModel.save()
                    }
                },
                isEnabled = (uiState.username.isNotBlank() &&
                        uiState.selectedSemester != null &&
                        uiState.usernameErrorMessage == null &&
                        uiState.errorMessage == null && uiState.linkedInError == null &&
                        uiState.whatsappError == null && uiState.aboutError == null
                        //                    && uiState. == null && uiState.Error == null
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
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Loader()
        }
    }
    AddAcademicPopup(
        showPopup = showPopUp,
        onDismiss = { showPopUp = false },
        onSave = { uni, dept ->
            viewModel.updateAcademicTF(uni.trim(), dept.trim())
            showPopUp = false
        },
        uniName = uiState.selectedUniversity ?: "",
    )
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