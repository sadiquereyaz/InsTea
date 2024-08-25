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

    init {
        getNotice()
    }

    fun getNotice() {
        viewModelScope.launch {
            try {
                val noticeList = fetchScrollingNotices()
                _uiState.update { it.copy(scrollingNoticeList = noticeList) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = NoticeUiState(scrollingNoticeList = emptyList())
            }
        }
    }

    private suspend fun fetchScrollingNotices(url: String = "https://jmicoe.in/"): List<Pair<String, String>> {
        Log.d("NoticeViewModel", "Fetching notices from URL: $url")
        return withContext(Dispatchers.IO) {
            try {
                val document = Jsoup.connect(url).get()
                Log.d("NOTICE_VM", document.html())
                // Select all <a> tags within the <marquee> element
                val notices: Elements = document.select("marquee a")
                val list = mutableListOf<Pair<String, String>>()
                for (notice in notices) {
                    val noticeText = notice.text()
                    val noticeLink = notice.attr("href")
                    list.add(Pair(noticeText, noticeLink))
                }
                list
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
