package `in`.instea.instea

import InsteaApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import `in`.instea.instea.ui.theme.InsTeaTheme
import kotlinx.coroutines.launch


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

