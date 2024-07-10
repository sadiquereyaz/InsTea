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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import `in`.instea.instea.composable.DropdownMenuBox
import `in`.instea.instea.data.DataSource.departments
import `in`.instea.instea.data.DataSource.graduatingYears
import `in`.instea.instea.data.DataSource.semesters
import `in`.instea.instea.data.DataSource.universities
import java.time.Year

@Composable
fun EditProfile(
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    userName: String,
    onUserNameChanged: (String) -> Unit,
    onDepartmentChanged: (String) -> Unit,
    onSemesterChanged: (String) -> Unit,
    onSaveButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    selectedDepartment: String,
    selectedSemester: String,
    selectedUniversity:String,
    selectedYear:String,
    onUniversityChanged: (String) -> Unit,
    onYearChanged: (String) -> Unit,
    selectedHostel: String,
    instagram: String,
    linkedin: String,
) {
    Column(modifier = modifier,
        ) {
        EditText(
            modifier = modifier,
            value = userName,
            onValueChange = onUserNameChanged,
            label = "User Name",
        )
        Spacer(modifier = Modifier.height(16.dp))
        DropdownComposables(
            selectedDepartment,
            onDepartmentChanged,
            selectedSemester,
            onSemesterChanged,
            selectedUniversity,
            onUniversityChanged ,
            selectedYear ,
            onYearChanged

        )
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
    selectedUniversity:String,
    onUniversityChanged: (String) -> Unit,
    selectedYear: String,
    onYearChanged: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DropdownMenuBox(
            label = "University/College",
            options = universities,
            selectedOption = selectedUniversity,
            onOptionSelected = onUniversityChanged,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        DropdownMenuBox(
            label = "Graduating Year",
            options = graduatingYears,
            selectedOption = selectedYear,
            onOptionSelected = onYearChanged,
            modifier = Modifier.weight(0.5f)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        DropdownMenuBox(
            label = "Department",
            options = departments,
            selectedOption = selectedDepartment,
            onOptionSelected = onDepartmentChanged,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        DropdownMenuBox(
            label = "Semester",
            options = semesters,
            selectedOption = selectedSemester,
            onOptionSelected = onSemesterChanged,
            modifier = Modifier.weight(0.5f)
        )
    }
}

@Composable
fun EditText(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        singleLine = true,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
//            keyboardType = KeyboardType.Text
        )
    )

}

@Composable
fun DropDown() {

}

@Preview(showSystemUi = true)
@Composable
fun EditProfilePreview() {
    EditProfile(
        userName = "John Doe",
        onUserNameChanged = {},
        onSaveButtonClicked = {},
        onCancelButtonClicked = {},
        selectedDepartment = "Computer Science",
        selectedSemester = "5",
        selectedHostel = "A Block",
        instagram = "johndoe_insta",
        linkedin = "johndoe_linkedin",
        onDepartmentChanged = {},
        onSemesterChanged = {},
        onUniversityChanged = {},
        onYearChanged = {},
        selectedUniversity = "Jamia",
        selectedYear = "2026"
    )
}