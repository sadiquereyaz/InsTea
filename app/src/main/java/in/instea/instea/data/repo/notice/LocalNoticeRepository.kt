package `in`.instea.instea.data.repo.notice

import `in`.instea.instea.data.dao.NoticeDao
import `in`.instea.instea.data.datamodel.NoticeModal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalNoticeRepository(private val noticeDao: NoticeDao) : NoticeRepository {
    override suspend fun fetchNotices(
        url: String,
        selector: String,
        type: String
    ): List<NoticeModal> = withContext(Dispatchers.IO) {
        noticeDao.getNotices(type = type)
    }

    override suspend fun refreshNotices(url: String, selector: String, type: String) {
        // This method is empty for LocalNoticeRepository as it doesn't fetch new data
    }

    override fun getNoticesFlow(
        url: String,
        selector: String,
        type: String
    ): Flow<List<NoticeModal>> {
        TODO("Not yet implemented")
    }

    suspend fun saveNotices(notices: List<NoticeModal>) =
        withContext(Dispatchers.IO) {
//        noticeDao.deleteAllNotices()
            notices.forEach { noticeDao.insertNotice(it.title, it.url, it.type, it.timestamp) }
        }
}