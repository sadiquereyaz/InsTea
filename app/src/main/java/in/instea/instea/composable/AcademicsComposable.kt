package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AcademicsComposable(
    modifier: Modifier = Modifier,
    university: String,
    universityList: List<String>,
    onUniversityChanged: (String) -> Unit,
    department: String,
    departmentList: List<String>,
    onDepartmentChanged: (String) -> Unit,
    semester: String,
    semesterList: List<String>,
    onSemesterChanged: (String) -> Unit,
    onAddItemClicked: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // university
        DropdownComposable(
            modifier = modifier,
            label = "University",
            options = universityList,
            leadingIcon = Icons.Default.AccountBalance,
            selectedOption = university,
            onOptionSelected = onUniversityChanged,
            onAddItemClicked = onAddItemClicked,
            isLoadingOption = false
        )
        Spacer(modifier = Modifier.height(8.dp))
        // department and semester
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            //department
            DropdownComposable(
                modifier = Modifier.weight(3f),
                label = "Department",
                selectedOption = department,
                leadingIcon = Icons.Default.School,
                options = departmentList,
                onOptionSelected = onDepartmentChanged,
                onAddItemClicked = onAddItemClicked,
                errorMessage = "Please select university first",
                isLoadingOption = false
            )
            // semester
            DropdownComposable(
                modifier = Modifier.weight(2.5f),
                label = "Sem",
                leadingIcon = Icons.Default.AutoGraph,
                options = semesterList,
                selectedOption = semester,
                onOptionSelected = onSemesterChanged,
                onAddItemClicked = onAddItemClicked,
                isLoadingOption = false,
            )
        }
    }
}