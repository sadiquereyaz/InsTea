package `in`.instea.instea.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.composable.DropdownMenuBox
import `in`.instea.instea.data.DataSource.departments
import `in`.instea.instea.data.DataSource.platforms
import `in`.instea.instea.data.DataSource.semesters
import `in`.instea.instea.data.DataSource.universities
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.EditProfileViewModel

@Composable
fun EditProfile(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    onDepartmentChanged: (String) -> Unit = {},  // Empty lambda
    onSemesterChanged: (String) -> Unit = {},  // Empty lambda
    onSaveButtonClicked: () -> Unit = {},  // Empty lambda
    onCancelButtonClicked: () -> Unit = {},  // Empty lambda
    selectedDepartment: String = "Computer Science",  // Dummy department
    selectedSemester: String = "Fall 2024",  // Dummy semester
    selectedUniversity: String = "University of Example",  // Dummy university
    onUniversityChanged: (String) -> Unit = {},  // Empty lambda
    viewModel: EditProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier,
    ) {
        EditText(
            modifier = modifier,
            value = uiState.username ?: "",
            onValueChange = {
                viewModel.onUsernameChanged(it)
            },
            label = "Username",
        )
        Spacer(modifier = Modifier.height(16.dp))
        DropdownComposables(
            selectedDepartment,
            onDepartmentChanged,
            selectedSemester,
            onSemesterChanged,
            selectedUniversity,
            onUniversityChanged,
        )
        Spacer(modifier = Modifier.height(16.dp))
        BioText(
            value = "Instea is really a great app", onValueChange = {},
            label = "Bio"
        )
        Spacer(modifier = Modifier.height(16.dp))
        platformComp()
        Spacer(modifier = Modifier.height(16.dp))
        platformComp()
        Spacer(modifier = Modifier.weight(1f))
        Buttons(onCancelButtonClicked, onSaveButtonClicked)

    }
}

@Composable
private fun Buttons(
    onCancelButtonClicked: () -> Unit,
    onSaveButtonClicked: () -> Unit
) {
    Row {
        OutlinedButton(
            onClick = onCancelButtonClicked,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Cancel")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onSaveButtonClicked,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
private fun DropdownComposables(
    selectedDepartment: String,
    onDepartmentChanged: (String) -> Unit,
    selectedSemester: String,
    onSemesterChanged: (String) -> Unit,
    selectedUniversity: String,
    onUniversityChanged: (String) -> Unit,

    ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        val university = rememberSaveable { mutableStateOf<String>(universities[0]) }
        DropdownMenuBox(
            label = "University/College",
            options = universities,
            selectedOption = university.value,
            onOptionSelected = {
                university.value = it
                onUniversityChanged(it)
            },
            modifier = Modifier.weight(1f)
        )

    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val dept = rememberSaveable { mutableStateOf<String>(departments[0]) }
        val sem = rememberSaveable { mutableStateOf<String>(semesters[0]) }
        DropdownMenuBox(
            label = "Department",
            options = departments,
            selectedOption = dept.value,
            onOptionSelected = {
                dept.value = it
                onDepartmentChanged(it)
            },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        DropdownMenuBox(
            label = "Semester",
            options = semesters,
            selectedOption = sem.value,
            onOptionSelected = {
                sem.value = it
                onSemesterChanged(it)
            },
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
fun platformComp(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val platform = rememberSaveable { mutableStateOf<String>(platforms[0]) }
        val sociaLink = rememberSaveable { mutableStateOf("") }

        DropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(70.dp),
            label = "Platform",
            options = platforms,
            selectedOption = platform.value,
            onOptionSelected = {

                platform.value = it
            })
        Spacer(modifier = Modifier.width(10.dp))


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(65.dp),

            value = sociaLink.value,
            onValueChange = {
                sociaLink.value = it
            },
            label = { Text(text = "Social Link") },
            shape = RoundedCornerShape(10.dp)
        )


    }

}

@Composable
fun BioText(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    OutlinedTextField(modifier = Modifier
        .fillMaxWidth(),
                      shape = RoundedCornerShape(15.dp),
                      value = value,
                      minLines = 3,
                      maxLines = 5,
                      label = { Text(text = label) },
                      onValueChange = {})

}

@Composable
fun EditText(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: ImeAction = ImeAction.Next
) {
//    val isEmpty by rememberSavable {mutableStateOf(value.isEmpty())}
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        singleLine = true,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
//            keyboardType = KeyboardType.Text
        ),
//        supportingText = if (isEmpty) "must contains a value" else ""
    )

}


@Preview(showSystemUi = true)
@Composable
fun EditProfilePreview() {
    EditProfile()
}