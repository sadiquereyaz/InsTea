package `in`.instea.instea.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
            onOptionSelected = onUniversityChanged,
            leadingIcon = Icons.Default.AccountBalance,
            selectedOption = university,
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
                options = departmentList,
                onOptionSelected = onDepartmentChanged,
                leadingIcon = Icons.Default.School,
                errorMessage = "Please select university first",
                selectedOption = department,
                onAddItemClicked = onAddItemClicked,
                isLoadingOption = false
            )
            // semester
            DropdownComposable(
                modifier = Modifier.weight(2.5f),
                label = "Sem",
                options = listOf("I", "II", "III","IV", "V", "VI", "VII", "VIII"),
                onOptionSelected = onSemesterChanged,
                leadingIcon = Icons.Default.AutoGraph,
                selectedOption = semester,
                onAddItemClicked = onAddItemClicked,
                isLoadingOption = false,
                showAddBtn = false
            )
        }
    }
}