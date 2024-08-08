//package `in`.instea.instea.screens.profile
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.imePadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Person
//import androidx.compose.material.icons.filled.Whatsapp
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.vectorResource
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import `in`.instea.instea.R
//import `in`.instea.instea.composable.AcademicsComposable
//import `in`.instea.instea.composable.AddAcademicPopup
//import `in`.instea.instea.composable.DropdownComposable
//import `in`.instea.instea.data.DataSource.platforms
//import `in`.instea.instea.data.viewmodel.AppViewModelProvider
//import `in`.instea.instea.data.viewmodel.EditProfileViewModel
//import `in`.instea.instea.screens.auth.composable.CustomTextField
//import kotlinx.coroutines.launch
//
//@Composable
//fun EditProfileScreen(
//    modifier: Modifier = Modifier,
//    navigateToAddAcademics: () -> Unit,  // Empty lambda
//    navigateBack: () -> Unit = {},  // Empty lambda
//    viewModel: EditProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//    val scrollState = rememberScrollState()
//
//    Column(
//        modifier = modifier
//            .verticalScroll(scrollState)
//            .imePadding()
//            .fillMaxSize()
//            .padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(4.dp),
//    ) {
//        //username
//        //username
//        CustomTextField(
//            modifier = Modifier,
//            value = uiState.username,
//            onValueChange = {
//                coroutineScope.launch {
//                    viewModel.onUsernameChanged(it.trim())
//                }
//            },
//            leadingIcon = Icons.Default.Person,
//            label = "Username",
//            errorMessage = uiState.usernameErrorMessage,
//        )
//
//        // about
//        AboutComp(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 16.dp), uiState = uiState, viewModel = viewModel
//        )
//        // academics detail
//        AcademicsComposable(
//            modifier = Modifier.fillMaxWidth(),
//            university = uiState.selectedUniversity,
//            universityList = uiState.universityList,
//            onUniversityChanged = {
//                coroutineScope.launch { viewModel.onUniversityChanged(it) }
//            },
//            department = uiState.selectedDepartment,
//            departmentList = uiState.departmentList,
//            onDepartmentChanged = { viewModel.onDepartmentChanged(it) },
//            semester = uiState.selectedSemester,
//            semesterList = uiState.semesterList,
//            onSemesterChanged = { viewModel.onSemesterChanged(it) },
//            onAddItemClicked = {showPopUp = true}
//        )
//        // whatsapp
//        CustomTextField(
//            value = uiState.whatsappNo.toString() ?: "",
//            onValueChange = {
//                viewModel.onWhatsappNoChanged(it)
//            },
//            label = "Whatsapp Number",
//            leadingIcon = Icons.Default.Whatsapp,
//            keyboardType = KeyboardType.Phone
//        )
//        // instagram
//        CustomTextField(
//            value = uiState.instagram ?: "",
//            onValueChange = {
//                viewModel.onInstagramChanged(it)
//            },
//            label = "Instagram username",
//            leadingIcon = ImageVector.vectorResource(id = R.drawable.insta),
//        )
//        // linkedIn
//        CustomTextField(
//            value = uiState.linkedin ?: "",
//            onValueChange = {
//                viewModel.onLinkedInChanged(it)
//            },
//            label = "Linkedin username",
//            leadingIcon = ImageVector.vectorResource(id = R.drawable.linked),
//        )
//        Spacer(modifier = Modifier.height(24.dp))
//        Buttons(
//            navigateBack,
//            onSaveButtonClicked = {
//                coroutineScope.launch {
//                    navigateBack()
//                    viewModel.saveUserDetails()
//                }
//            },
//            isActive = uiState.usernameErrorMessage.isNullOrBlank()
//        )
//    }
//    AddAcademicPopup(
//        showPopup = showPopUp,
//        onDismiss = { showPopUp = false },
//        onSave = { uni, dept ->
//            viewModel.addNewAcademic(uni, dept)
//            showPopUp = false
//        },
//        uniName = uiState.selectedUniversity,
//    )
//}
//
//@Composable
//private fun Buttons(
//    onCancelButtonClicked: () -> Unit,
//    onSaveButtonClicked: () -> Unit,
//    isActive: Boolean = false
//) {
//    var isEnabled by remember { mutableStateOf(isActive) }
//    LaunchedEffect(isActive) {
//        isEnabled = isActive
//    }
//    Row {
//        OutlinedButton(
//            onClick = onCancelButtonClicked,
//            modifier = Modifier.weight(1f)
//        ) {
//            Text(text = "Cancel")
//        }
//        Spacer(modifier = Modifier.width(16.dp))
//        Button(
//            onClick = onSaveButtonClicked,
//            modifier = Modifier.weight(1f),
//            enabled = isEnabled
//        ) {
//            Text(text = "Save")
//        }
//    }
//}
//
//@Composable
//fun PlatformComp(
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        val platform = rememberSaveable { mutableStateOf<String>(platforms[0]) }
//        val sociaLink = rememberSaveable { mutableStateOf("") }
//
//        DropdownComposable(
//            modifier = Modifier
//                .fillMaxWidth(0.4f)
//                .height(70.dp),
//            label = "Platform",
//            options = platforms,
//            onOptionSelected = {
//
//                platform.value = it
//            },
//            selectedOption = platform.value,
//            isLoadingOption = false
//        )
//        Spacer(modifier = Modifier.width(10.dp))
//
//
//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth(1f)
//                .height(65.dp),
//
//            value = sociaLink.value,
//            onValueChange = {
//                sociaLink.value = it
//            },
//            label = { Text(text = "Social Link") },
//            shape = RoundedCornerShape(10.dp)
//        )
//    }
//}
//
//@Composable
//fun AboutComp(
//    modifier: Modifier = Modifier,
//    uiState: EditProfileUiState,
//    viewModel: EditProfileViewModel
//) {
//    OutlinedTextField(
//        modifier = modifier
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(8.dp),
//        value = uiState.about,
//        minLines = 3,
//        maxLines = 5,
//        label = { Text(text = "About") },
//        onValueChange = {
//            if (it.length <= 100) viewModel.onAboutChanged(it)
//        }
//    )
//
//}
//
//@Composable
//fun EditText(
//    modifier: Modifier,
//    value: String,
//    onValueChange: (String) -> Unit,
//    label: String,
//    imeAction: ImeAction = ImeAction.Next
//) {
////    val isEmpty by rememberSavable {mutableStateOf(value.isEmpty())}
//    OutlinedTextField(
//        modifier = Modifier.fillMaxWidth(),
//        value = value,
//        singleLine = true,
//        onValueChange = onValueChange,
//        label = { Text(text = label) },
//        shape = RoundedCornerShape(8.dp),
//        keyboardOptions = KeyboardOptions.Default.copy(
//            imeAction = imeAction,
////            keyboardType = KeyboardType.Text
//        ),
////        supportingText = if (isEmpty) "must contains a value" else ""
//    )
//}
