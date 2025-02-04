package `in`.instea.instea.data.repo.notice

import `in`.instea.instea.data.local.entity.NoticeModal
import `in`.instea.instea.domain.repo.NoticeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CombinedNoticeRepository(
    private val localNoticeRepository: LocalNoticeRepository,
    private val networkNoticeRepository: NetworkNoticeRepository
) : NoticeRepository {
    override suspend fun fetchNotices(
        url: String,
        selector: String,
        type: String
    ): List<NoticeModal> {
        val localNotices = localNoticeRepository.fetchNotices("", "", "Admission")
        if (localNotices.isEmpty()) {
            val networkNotices = networkNoticeRepository.fetchNotices(url, selector, type)
//            localNoticeRepository.saveNotices(networkNotices)
            return networkNotices
        } else {
            return localNotices
        }
    }

    override suspend fun refreshNotices(url: String, selector: String, type: String) {
        val networkNotices = networkNoticeRepository.fetchNotices(url, selector, type)
//        localNoticeRepository.saveNotices(networkNotices)
    }

    override fun getNoticesFlow( url: String, selector: String, type: String): Flow<List<NoticeModal>> = flow {
        emit(localNoticeRepository.fetchNotices(type = type))
        val networkNotices = networkNoticeRepository.fetchNotices(url, selector, type)
        localNoticeRepository.saveNotices(notices = networkNotices)
//        emit(networkNotices)
    }
}

