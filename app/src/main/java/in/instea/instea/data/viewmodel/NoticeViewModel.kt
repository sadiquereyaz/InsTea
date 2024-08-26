package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlAnchor
import com.gargoylesoftware.htmlunit.html.HtmlPage
import `in`.instea.instea.screens.notice.NoticeUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoticeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState: StateFlow<NoticeUiState> = _uiState.asStateFlow()

    init {
        getNotice()
    }

    private fun getNotice() {
        viewModelScope.launch {
            try {
                val sNotice = fetchScrollingNotices()   // loader is working properly by initialising here
                _uiState.update {
                    it.copy(
                        scrollingNoticeList = sNotice,
//                        admissionNoticeList = fetchAdmissionNotices(),
//                        newWebsiteNoticeList = fetchNewWebsiteNotices()
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {it.copy(isLoading = false)}
            }
        }
    }

    //httpUnit-static data
    suspend fun fetchScrollingNotices(url: String = "https://jmicoe.in/"): List<Pair<String, String>> {
        Log.d("NOTICE_VM", "Fetching notices from URL: $url")
        return withContext(Dispatchers.IO) {
            val webClient = WebClient().apply {
                options.isJavaScriptEnabled = false     // since this data is static, disabling JS
                options.isCssEnabled = false
            }
            try {
                val page: HtmlPage = webClient.getPage(url)
                // Wait for JS execution
                //webClient.waitForBackgroundJavaScript(10_000) // since this data is static and js is not used, so no need to wait

                // Select all <a> tags within the <marquee> element

                /*NOTE: while using xpath, if elements are dynamically added to the DOM after page load, getByXPath might not return what you're expecting.*/
//                val notices = page.getByXPath<HtmlAnchor>("//marquee//a")
                val marquee = page.getElementsByTagName("marquee").firstOrNull()
                val notices = marquee?.getElementsByTagName("a") ?: emptyList()

                val list = mutableListOf<Pair<String, String>>()
                for (notice in notices) {
                    val noticeText = notice.textContent.trim()
                    val noticeLink = notice.getAttribute("href").trim()
                    list.add(Pair(noticeText, noticeLink))
                }
                _uiState.update {it.copy(isLoading = false)}
                list
            } catch (e: Exception) {
                _uiState.update {it.copy(isLoading = false)}
                e.printStackTrace()
                emptyList()
            } finally {
                _uiState.update {it.copy(isLoading = false)}
                webClient.close()
            }
        }
    }

    // admissionNotice-Dynamic data
    suspend fun fetchAdmissionNotices(url: String = "https://jmicoe.in/"): List<Pair<String, String>> {
        Log.d("`in`.instea.instea.data.viewmodel.NoticeViewModel", "Fetching notices from URL: $url")
        return withContext(Dispatchers.IO) {
            val webClient = WebClient().apply {
                options.isJavaScriptEnabled = true  // Enable JavaScript
                options.isCssEnabled = false
            }
            try {
                val page: HtmlPage = webClient.getPage(url)
                Log.d("NOTICE_VM", page.titleText)
                webClient.waitForBackgroundJavaScript(10000) // Wait for JS execution

                // Use XPath to select all <a> tags within the <span class="news-data"> elements
                val notices = page.getByXPath<HtmlAnchor>("//li//span[@class='news-data']//a")

                val list = mutableListOf<Pair<String, String>>()
                for (notice in notices) {
                    val noticeText = notice.textContent.trim()
                    val noticeLink = notice.getAttribute("href").trim()
                    list.add(Pair(noticeText, noticeLink))
                }
                _uiState.update {it.copy(isLoading = false)}
                list
            } catch (e: Exception) {
                _uiState.update {it.copy(isLoading = false)}
                e.printStackTrace()
                emptyList()
            } finally {
                _uiState.update {it.copy(isLoading = false)}
                webClient.close()
            }
        }
    }

    suspend fun fetchNewWebsiteNotices(url: String = "https://www.youtube.com/watch?v=1z70h-v5Wz8/"): List<Pair<String, String>> {
        Log.d("`in`.instea.instea.data.viewmodel.NoticeViewModel", "Fetching notices from URL: $url")
        return withContext(Dispatchers.IO) {
            val webClient = WebClient().apply {
                options.isJavaScriptEnabled = true
                options.isCssEnabled = false
            }
            try {
                val page: HtmlPage = webClient.getPage(url)
                webClient.waitForBackgroundJavaScript(10000) // Wait for JS execution
                Log.d("NOTICE_VM", page.titleText)

                // Use XPath to select all <a> tags within the <span class="news-data"> elements
//                val notices = page.getByXPath<HtmlAnchor>("//span[@id='datatable1']//a")

                val list = mutableListOf<Pair<String, String>>()
                /* for (notice in notices) {
                     val noticeText = notice.textContent.trim()
                     val noticeLink = notice.getAttribute("href").trim()
                     list.add(Pair(noticeText, noticeLink))
                 }*/
                _uiState.update {it.copy(isLoading = false)}
                list
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {it.copy(isLoading = false)}
                emptyList()
            } finally {
                _uiState.update {it.copy(isLoading = false)}
                webClient.close()
            }
        }
    }
    // jsoup static data
    /* private suspend fun fetchScrollingNotices(url: String = "https://jmicoe.in/"): List<Pair<String, String>> {
         Log.d("`in`.instea.instea.data.viewmodel.NoticeViewModel", "Fetching notices from URL: $url")
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
     }*/
}
