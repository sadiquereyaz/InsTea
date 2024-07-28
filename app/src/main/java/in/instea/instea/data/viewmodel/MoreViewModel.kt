package `in`.instea.instea.data.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.screens.more.MoreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class MoreViewModel(
    val scheduleRepository: ScheduleRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoreUiState())
    val uiState: StateFlow<MoreUiState> = _uiState

    init {
        onTimestampSelected(_uiState.value.selectedTimestamp)
    }

    fun onTimestampSelected(selectedDate: LocalDate) {
        viewModelScope.launch {
            val month = selectedDate.monthValue.toString().padStart(2, '0')
            val year = (selectedDate.year%100).toString()
//            Log.d("loading", selectedDate.year.toString())
            val timestamp = ("$year$month".toInt())*100
            Log.d("time", timestamp.toString())
            _uiState.update {
                it.copy(
                    selectedTimestamp = selectedDate,
                    attendanceSummaries = scheduleRepository.getSubjectAttendanceSummary(timestamp)
                )
            }
        }
    }
}