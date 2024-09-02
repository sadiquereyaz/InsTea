package `in`.instea.instea.data.repo.notice

import `in`.instea.data.repo.notice.WebScrapingService
import `in`.instea.instea.data.datamodel.NoticeModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class NetworkNoticeRepository(
    private val webScrapingService: WebScrapingService
) : NoticeRepository {
    override suspend fun fetchNotices(url: String, selector: String, type: String): List<NoticeModal> = withContext(Dispatchers.IO) {
        webScrapingService.scrapNotices(url, selector, type)
    }

    override suspend fun refreshNotices(url: String, selector: String, type: String) {
        TODO("Not yet implemented")
    }

    override fun getNoticesFlow(url: String, selector: String, type: String): Flow<List<NoticeModal>> {
        TODO("Not yet implemented")
    }
}

