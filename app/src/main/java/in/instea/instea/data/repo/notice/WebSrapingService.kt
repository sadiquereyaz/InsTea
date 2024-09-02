package `in`.instea.data.repo.notice // Assuming "instea.instea" is a mistake

import android.util.Log
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlAnchor
import com.gargoylesoftware.htmlunit.html.HtmlPage
import `in`.instea.instea.data.datamodel.NoticeModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class WebScrapingService {
    suspend fun scrapNotices(url: String, selector: String, type: String): List<NoticeModal> =
        withContext(Dispatchers.IO) {
            Log.e("WebScrapingService", "URL1 $url")
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
                Log.e("WebScrapingService", "URL $url")
                val page: HtmlPage = webClient.getPage(url)
                Log.e("WebScrapingService", "Page $page")
                webClient.waitForBackgroundJavaScript(3000)

                val notices = page.getByXPath<HtmlAnchor>(selector).map { notice ->
                    Log.e("WebScrapingService", "Notices: $notice")
                    NoticeModal(
                        title = notice.textContent.trim(),
                        url = if (notice.hrefAttribute.startsWith("http")) notice.hrefAttribute.trim()
                        else "https://jmi.ac.in${notice.hrefAttribute.trim()}",
                        type = type,
                        timestamp = System.currentTimeMillis()
                    )
                }
                notices
            } catch (e: Exception) {
                Log.e("NoticeRepository", "Error While fetching notices", e)
                emptyList<NoticeModal>() // Returning empty list with logging
            } finally {
                webClient.close()
            }
        }
}