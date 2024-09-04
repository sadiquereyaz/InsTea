package `in`.instea.instea.screens.notice

import `in`.instea.instea.data.datamodel.NoticeModal

data class NoticeUiState(
    var isLoading: Boolean = false,
    val notices: List<NoticeModal> = emptyList(),
    val error: String? = null
)

data class TabConfig(val tabName: String, val url: String, val selector: String, val noticeType:String)
