package `in`.instea.instea.data.repo

import android.util.Log
import com.gargoylesoftware.htmlunit.BrowserVersion
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController
import com.gargoylesoftware.htmlunit.WaitingRefreshHandler
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlAnchor
import com.gargoylesoftware.htmlunit.html.HtmlPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface NoticeRepository{
    suspend fun fetchNotices(url: String, selector: String): List<Pair<String, String>>
}

class NetworkNoticeRepository:NoticeRepository {
    override suspend fun fetchNotices(url: String, selector: String): List<Pair<String, String>> {
        return withContext(Dispatchers.IO) {
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
                webClient.waitForBackgroundJavaScript(3000)

                val notices = page.getByXPath<HtmlAnchor>(selector)
                notices.map { notice ->
                    Pair(
                        notice.textContent.trim(),
                        if (notice.hrefAttribute.startsWith("http")) notice.hrefAttribute.trim()
                        else "https://jmi.ac.in${notice.hrefAttribute.trim()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("NoticeRepository", "Error fetching notices", e)
                emptyList()
            } finally {
                webClient.close()
            }
        }
    }
}