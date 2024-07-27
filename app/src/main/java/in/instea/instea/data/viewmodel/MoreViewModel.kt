package `in`.instea.instea.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.more.MoreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoreViewModel(
    val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoreUiState())
    val uiState: StateFlow<MoreUiState> = _uiState

    init {
        onMonthSelected(_uiState.value.selectedMonth)
    }

    fun onMonthSelected(month: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    selectedMonth = month,
                    attendanceSummaries = scheduleRepository.getSubjectAttendanceSummary()
                )
            }
        }
    }

    val attendanceSummary = liveData {
        val data = scheduleRepository.getSubjectAttendanceSummary()
        emit(data)
    }
}