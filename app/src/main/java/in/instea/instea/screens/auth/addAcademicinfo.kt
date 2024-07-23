package `in`.instea.instea.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.School
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.SignUpViewModel
import `in`.instea.instea.screens.auth.composable.ButtonComp
import `in`.instea.instea.screens.auth.composable.CustomTextField

@Composable
fun AddInfo(
    navController: NavController,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    val viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
    var university by rememberSaveable { mutableStateOf("") }
    var department by rememberSaveable { mutableStateOf("") }
    var semester by rememberSaveable { mutableStateOf("") }
    Column(modifier = modifier.padding(horizontal = 16.dp)) {

        //University
        CustomTextField(
            value = university,
            onValueChange = {
                university = it
            },
            label = "University",
            leadingIcon = Icons.Default.AccountBalance,
        )
        //Department
        CustomTextField(
            value = department,
            onValueChange = {
                department = it
            },
            label = "Department",
            leadingIcon = Icons.Default.School
        )
        //Semester
        CustomTextField(
            value = semester,
            onValueChange = {
                semester = it
            },
            label = "Semester", leadingIcon = Icons.Default.AutoGraph
        )
        Spacer(modifier = Modifier.weight(1f))
        ButtonComp(
            text = "Add",
                   onButtonClicked = {
                       viewModel.addItem(semester, department, university)

                   })


    }
}

@Preview(showBackground = true)
@Composable
private fun AddInfoPreview() {
    val navController = rememberNavController()
    AddInfo(navController)
}
