package `in`.instea.instea.screens.notice

data class NoticeUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val showSnackBar: Boolean = false,
    val scrollingNoticeList: List<Pair<String, String>> = emptyList()
)
