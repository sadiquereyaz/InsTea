package `in`.instea.instea.screens.schedule

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.ScheduleViewModel
import kotlinx.coroutines.launch

@Composable
fun EditScheduleScreen(
    navController: NavController,
    viewModel: ScheduleViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
//                viewModel.upsertSchedule()
            }
        }
    ) {
        Text(text = "Save")
    }

}