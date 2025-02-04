package `in`.instea.instea.domain.repo

import `in`.instea.instea.data.local.entity.NoticeModal
import kotlinx.coroutines.flow.Flow

interface NoticeRepository {
    suspend fun fetchNotices(url: String="", selector: String = "", type: String="Admission"): List<NoticeModal>
    suspend fun refreshNotices(url: String="", selector: String = "", type: String="Admission")
    fun getNoticesFlow(url: String, selector: String, type: String): Flow<List<NoticeModal>>
}