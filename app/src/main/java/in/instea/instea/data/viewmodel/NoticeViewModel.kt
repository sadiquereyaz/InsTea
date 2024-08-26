package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler
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
//        getNotice()
        fetchNewWebsiteNotices()
    }

    fun getNotice() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val sNotice = fetchScrollingNotices()   // loader is working properly by initialising here
//                Log.d("NOTICE_VM", "Scrolling Notices: $sNotice")
                _uiState.update { it.copy(scrollingNoticeList = sNotice) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    //httpUnit-static data
    private suspend fun fetchScrollingNotices(url: String = "https://jmicoe.in/"): List<Pair<String, String>> {
        Log.d("NOTICE_VM", "Fetching Scrolling URL: $url")
        return withContext(Dispatchers.IO) {
            val webClient = WebClient().apply {
                options.isJavaScriptEnabled = false     // since this data is static, disabling JS
                options.isCssEnabled = false
            }
            try {
                val page: HtmlPage = webClient.getPage(url)
                // Wait for JS execution
//                webClient.waitForBackgroundJavaScript(3_000) // since this data is static and js is not used, so no need to wait

                // Select all <a> tags within the <marquee> element

                /*NOTE: while using xpath, if elements are dynamically added to the DOM after page load, getByXPath might not return what you're expecting.*/
//                val notices = page.getByXPath<HtmlAnchor>("//marquee//a")
                val marquee = page.getElementsByTagName("marquee").firstOrNull()
                val notices = marquee?.getElementsByTagName("a") ?: emptyList()

                val list = mutableListOf<Pair<String, String>>()
                for (notice in notices) {
                    val noticeText = notice.textContent.trim()
                    val noticeLink = notice.getAttribute("href").trim()
//                    Log.d("NOTICE_VM", "LINK: $noticeText : $noticeLink")
                    list.add(Pair(noticeText, noticeLink))
                }
                _uiState.update { it.copy(isLoading = false) }
                list
            } catch (e: Exception) {
                Log.d("NOTICE_VM", "Exception Execution Completed")
                e.printStackTrace()
                emptyList()
            } finally {
                // always executing
                Log.d("NOTICE_VM", "Finally Execution Completed")
                webClient.close()
            }
        }
    }

    // admissionNotice-Dynamic data
    /*suspend fun fetchAdmissionNotices(url: String = "https://jmicoe.in/"): List<Pair<String, String>> {
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
    }*/

    fun fetchAdmissionNotices(url: String = "https://jmicoe.in/") {
        Log.d("NOTICE_VM", "Fetching notices from URL: $url")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            withContext(Dispatchers.IO) {
                val webClient = WebClient().apply {
                    options.isJavaScriptEnabled = true  // Enable JavaScript
                    options.isCssEnabled = false
                }
                try {
                    val page: HtmlPage = webClient.getPage(url)
                    Log.d("NOTICE_VM", page.titleText)
                    webClient.waitForBackgroundJavaScript(1_000) // Wait for JS execution

                    Log.d("NOTICE_VM", "Execution Completed")

                    // Use XPath to select all <a> tags within the <span class="news-data"> elements
                    val notices = page.getByXPath<HtmlAnchor>("//li//span[@class='news-data']//a")

                    val list = mutableListOf<Pair<String, String>>()
                    for (notice in notices) {
                        val noticeText = notice.textContent.trim()
                        val noticeLink = notice.getAttribute("href").trim()
                        Log.d("NOTICE_VM", "LINK: $noticeText : $noticeLink")
                        list.add(Pair(noticeText, noticeLink))
                    }
                    _uiState.update { it.copy(isLoading = false, admissionNoticeList = list) }
                } catch (e: Exception) {
                    Log.d("NOTICE_VM", "Exception Execution Completed")
                    e.printStackTrace()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            admissionNoticeList = emptyList()
                        )
                    }

                } finally {
                    Log.d("NOTICE_VM", "Finally Execution Completed")
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    webClient.close()
                }

            }
        }
    }

    fun fetchNewWebsiteNotices(url: String = "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/5") {
        Log.d("NOTICE_vm", "Fetching notices from URL: $url")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _uiState.update { it.copy(isLoading = true) }
                val webClient = WebClient(BrowserVersion.CHROME).apply {
                    options.isJavaScriptEnabled = true
                    options.isCssEnabled = false
                    options.isThrowExceptionOnScriptError = false
                    options.isThrowExceptionOnFailingStatusCode = false
                    options.isPrintContentOnFailingStatusCode = false
                    ajaxController = NicelyResynchronizingAjaxController()
                    refreshHandler = WaitingRefreshHandler()
                }
                try {
                    val page: HtmlPage = webClient.getPage(url)

                    // Wait for JavaScript execution
                    webClient.waitForBackgroundJavaScript(3000)

                    // Use XPath to select all <a> tags within the <span id="datatable1"> element
                    val notices = page.getByXPath<HtmlAnchor>("//span[@id='datatable1']//a")

                    val list = mutableListOf<Pair<String, String>>()
                    for (notice in notices) {
                        val noticeText = notice.textContent.trim()
                        val noticeLink = notice.hrefAttribute.trim()
                        val browsableLink = "https://jmi.ac.in$noticeLink"
                        Log.d("NOTICE_VM", "NOTICE $noticeText\nLINK: $browsableLink \n\n ")
                        list.add(Pair(noticeText, browsableLink))
                    }

                    Log.d("NOTICE_VM", "Fetched ${list.size} notices")
                    _uiState.update { it.copy(isLoading = false, newWebsiteNoticeList = list) }

                } catch (e: Exception) {
                    Log.e("NOTICE_VM", "Error fetching notices", e)
                    _uiState.update { it.copy(isLoading = false) }
                } finally {
                    webClient.close()
                }
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
