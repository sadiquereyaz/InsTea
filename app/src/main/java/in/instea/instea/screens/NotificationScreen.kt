import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val document = Jsoup.connect("https://medium.com/androiddevelopers/fun-with-shapes-in-compose-8814c439e1a0").get()
            val title = (document.title())
            val description = (document.select("meta[name=description").attr("content"))
            Log.d("NOTIFICATION_SCREEN", "description: $description")
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
}