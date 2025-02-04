package `in`.instea.instea.presentation.notice

import `in`.instea.instea.data.local.entity.NoticeModal

data class NoticeUiState(
    var isLoading: Boolean = true,
    val notices: List<NoticeModal> = emptyList(),
    val error: String? = null
)

data class TabConfig(val tabName: String, val url: String, val selector: String, val noticeType:String)
