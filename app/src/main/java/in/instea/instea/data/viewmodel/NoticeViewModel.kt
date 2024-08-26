import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.repo.NoticeRepository
import `in`.instea.instea.screens.notice.NoticeUiState
import `in`.instea.instea.screens.notice.TabConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoticeViewModel(private val noticeRepository: NoticeRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState: StateFlow<NoticeUiState> = _uiState.asStateFlow()

    val tabConfigs = mapOf(
        0 to TabConfig("Urgent", "https://jmicoe.in/", "//marquee//a"),
        1 to TabConfig("Admission", "https://jmicoe.in/","//div[@id='leftPanel']/ul/li[(a) or (span/a)]//a"),
        2 to TabConfig(
            "New Website Admission",
            "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/1",
            "//span[@id='datatable1']//a"
        ),
        3 to TabConfig(
            "Boys Hostels",
            "https://jmi.ac.in/ACADEMICS/Hostels/Hall-Of-Residence-Boys",
            "///*[@id=\"MainContent\"]/div/div/div/div/div/div/div/div/div/div/a"
        ),
        4 to TabConfig(
            "Examinations",
            "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/6",
            "//span[@id='datatable1']//a"
        ),
        5 to TabConfig(
            "Academics",
            "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/10",
            "//span[@id='datatable1']//a"
        )
        // Add more tab configurations as needed
    )

    fun fetchNoticesForTab(tabIndex: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val config = tabConfigs[tabIndex] ?: return@launch
            try {
                val notices = noticeRepository.fetchNotices(config.url, config.selector)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        notices = notices
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An error occurred"
                    )
                }
            }
        }
    }
}

