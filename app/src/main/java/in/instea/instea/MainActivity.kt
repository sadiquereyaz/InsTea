package `in`.instea.instea

import InsteaApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.model.schedule.ScheduleViewModel
import `in`.instea.instea.ui.theme.InsTeaTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsTeaTheme {
//                val scheduleViewModel: ScheduleViewModel = viewModel()
                InsteaApp(/*scheduleViewModel = scheduleViewModel*/)
            }
        }
    }
}
