package `in`.instea.instea

import InsteaApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import `in`.instea.instea.screens.Login
import `in`.instea.instea.screens.Signup
import `in`.instea.instea.screens.getDayDateList
import `in`.instea.instea.ui.theme.InsTeaTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsTeaTheme {
                InsteaApp()
            }
        }
    }
}
