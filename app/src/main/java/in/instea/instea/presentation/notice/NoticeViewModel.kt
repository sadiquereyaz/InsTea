package `in`.instea.instea.presentation.notice

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.domain.repo.NoticeRepository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoticeViewModel(private val noticeRepository: NoticeRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState: StateFlow<NoticeUiState> = _uiState.asStateFlow()

    val tabConfigs = mapOf(
        0 to TabConfig("Urgent", "https://jmicoe.in/", "//marquee//a", "Urgent"),
        1 to TabConfig("Admission", "https://jmicoe.in/","//div[@id='leftPanel']/ul/li[(a) or (span/a)]//a", "Admission"),
        2 to TabConfig(
            "New Website Admission",
            "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/1",
            "//span[@id='datatable1']//a", "New Website Admission"
        ),
        3 to TabConfig(
            "Boys Hostels",
            "https://jmi.ac.in/ACADEMICS/Hostels/Hall-Of-Residence-Boys",
            "//*[@id='MainContent']/div/div/div/div/div/div/div/div/div/div/a", "Boys Hostels"
        ),
        4 to TabConfig(
            "Girls Hostels",
            "https://jmi.ac.in/ACADEMICS/Hostels/Hall-Of-Residence-Girls",
            "//*[@id=\"MainContent\"]/div/div/div/div/div/div/div/div/div/div/a", "Girls Hostels"
        ),
        5 to TabConfig(
            "Examinations",
            "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/6",
            "//span[@id='datatable1']//a", "Examinations"
        ),
        6 to TabConfig(
            "Academics",
            "https://jmi.ac.in/BULLETIN-BOARD/Notices/Circulars/Latest/10",
            "//span[@id='datatable1']//a", "Academics"
        ),
        7 to TabConfig(
            "School Hostel",
            "//*[@id=\"MainContent\"]/div/div/div/div/div/div/div/div/div/div/a",
            "//span[@id='datatable1']//a", "School Hostel"
        ),
        8 to TabConfig(
            "Entrance Results",
            "https://jmi.ucanapply.com/univer/public/entrance-result-viewer?app_id=UElZMDAwMDAzNA==",
            "", "Entrance Resulta"
        )
        // Add more tab configurations as needed
    )

    fun fetchNoticesForTab(tabIndex: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(notices = emptyList()) }
            val config = tabConfigs[tabIndex] ?: return@launch
            try {
                Log.d("NOTICE_VM", "Fetching path: $config")
                noticeRepository.getNoticesFlow(config.url, config.selector, config.noticeType).collect { noticeList ->
                    Log.d("NOTICE_VM", "$noticeList")
                    _uiState.update {
                        it.copy(
                            notices = noticeList,
//                            isLoading = false
                        )
                    }
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
    fun refreshNotices() {
        viewModelScope.launch {
            noticeRepository.refreshNotices()
        }
    }
}

