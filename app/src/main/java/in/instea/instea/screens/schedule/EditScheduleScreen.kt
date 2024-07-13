package `in`.instea.instea.screens.schedule

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import `in`.instea.instea.data.viewmodel.ScheduleViewModel

@Composable
fun EditScheduleScreen(
    navController: NavController,
    scheduleViewModel: ScheduleViewModel
) {
    Text(text = "Schedule Edit Screen")

}