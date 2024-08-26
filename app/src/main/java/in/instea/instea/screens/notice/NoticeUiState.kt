package `in`.instea.instea.screens.notice

data class NoticeUiState(
    val isLoading: Boolean = true,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val showSnackBar: Boolean = false,
    val scrollingNoticeList: List<Pair<String, String>> = emptyList(),
    val admissionNoticeList: List<Pair<String, String>> = emptyList(),
    val newWebsiteNoticeList: List<Pair<String, String>> = emptyList(),
)
