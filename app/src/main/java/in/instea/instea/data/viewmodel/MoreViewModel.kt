package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.repo.AccountService
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.more.MoreDestination
import `in`.instea.instea.screens.more.MoreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class MoreViewModel(
    savedStateHandle: SavedStateHandle,
    private val scheduleRepository: ScheduleRepository,
    private val accountService: AccountService
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoreUiState())
    val uiState: StateFlow<MoreUiState> = _uiState
    private var argIndex: Int? = savedStateHandle[MoreDestination.INDEX_ARG]

    init {
        viewModelScope.launch {
            if (argIndex==-1 ) argIndex = null
            _uiState.update { it.copy(expandedIndex = argIndex) }
        }
            onTimestampSelected(_uiState.value.selectedTimestamp)
    }

    fun onTimestampSelected(selectedDate: LocalDate) {
        viewModelScope.launch {
            val month = selectedDate.monthValue.toString().padStart(2, '0')
            val year = (selectedDate.year % 100).toString()
//            Log.d("loading", selectedDate.year.toString())
            val timestamp = ("$year$month".toInt()) * 100
//            Log.d("time", timestamp.toString())
            _uiState.update {
                it.copy(
                    selectedTimestamp = selectedDate,
                    attendanceSummaries = scheduleRepository.getSubjectAttendanceSummary(timestamp)
                )
            }
        }
    }
    suspend fun onSignOutClick(){
        accountService.signOut()
    }
    suspend fun onDeleteAccountClick(){
        accountService.deleteAccount()
    }
}