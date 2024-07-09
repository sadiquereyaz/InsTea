package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import `in`.instea.instea.model.schedule.ScheduleViewModel

@Composable
fun EditScheduleScreen(
    navController: NavController,
    scheduleViewModel: ScheduleViewModel
) {
    Text(text = "Schedule Edit Screen")

}