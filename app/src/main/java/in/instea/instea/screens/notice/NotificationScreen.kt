import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import `in`.instea.instea.data.viewmodel.AppViewModelProvider

@Composable
fun NotificationScreen(
    modifier: Modifier = Modifier, navController: NavHostController,
    viewModel: NoticeViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    /*var count by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit){
        CoroutineScope(Dispatchers.IO).launch{
            try {
                val document = Jsoup.connect("https://books.toscrape.com/").get()
                Log.d("NOTIFICATION_SCREEN", "Title: ${document.title()}")
                val books: Elements = document.select(".product_pod")
//            val description = (document.select("meta[name=description").attr("content"))
                for (book in books) {
                    val title = book.select("h3 > a").text()
                    val price = book.select(".price_color").text()
                    Log.d("NOTIFICATION_BOOK", "Book ${count++}: $title price: $price");
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        CoroutineScope(Dispatchers.IO).launch{
            try {
                val document = Jsoup.connect("https://jmicoe.in/").get()
                Log.d("NOTIFICATION_JMI", "Title: ${document.title()}")
                val notices: Elements = document.select(".news-data")
//            val description = (document.select("meta[name=description").attr("content"))
                Log.d("NOTIFICATION_JMI", "NoticeHTML: ${notices.html()}");
                for (notice in notices) {
//                    val item = notice.select("a").text()
                    Log.d("NOTIFICATION_JMI", "Notice: ${notice.html()}");
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }*/
    var url by remember { mutableStateOf("https://books.toscrape.com/") }
    val uiState by viewModel.uiState.collectAsState()

    Column {
        TextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Enter URL") }
        )
        Button(onClick = { viewModel.getNotice(url) }) {
            Text("Scrape Website")
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(uiState.noticeList) { notice ->
                Text(text = notice)
            }
        }
    }
}