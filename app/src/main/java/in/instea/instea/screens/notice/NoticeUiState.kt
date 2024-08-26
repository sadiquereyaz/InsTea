package `in`.instea.instea.screens.notice

data class NoticeUiState(
    val isLoading: Boolean = false,
    val notices: List<Pair<String, String>> = emptyList(),
    val error: String? = null
)

data class TabConfig(val tabName: String, val url: String, val selector: String )
