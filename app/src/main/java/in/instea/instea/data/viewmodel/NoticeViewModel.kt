import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.screens.notice.NoticeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class NoticeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState: StateFlow<NoticeUiState> = _uiState.asStateFlow()

    fun getNotice(url: String) {
        viewModelScope.launch {
            try {
                val noticeList = fetchNotices(url)
                Log.d("NoticeViewModel", "Notices: $noticeList")
                _uiState.update { it.copy(noticeList = noticeList) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = NoticeUiState(noticeList = emptyList())
            }
        }
    }

    private suspend fun fetchNotices(url: String): List<String> {
        Log.d("NoticeViewModel", "Fetching notices from URL: $url")
        return withContext(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).get()
                val books: Elements = document.select(".product_pod")
                val list = mutableListOf<String>()
                var count = 0
                for (book in books) {
                    val title = book.select("h3 > a").text()
                    val price = book.select(".price_color").text()
                    list.add("Book ${count++}: $title price: $price")
                    Log.d("NOTIFICATION_BOOK", "Book $count: $title price: $price")
                }
                list
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
