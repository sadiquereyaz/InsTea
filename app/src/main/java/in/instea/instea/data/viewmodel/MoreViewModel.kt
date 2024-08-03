package `in`.instea.instea.data.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.InsteaDatabase.Companion.clearDatabase
import `in`.instea.instea.data.repo.AccountService
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.data.repo.UserRepository
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
    private val userRepository: UserRepository,
    private val accountService: AccountService,
    private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(MoreUiState())
    val uiState: StateFlow<MoreUiState> = _uiState
    private var argIndex: Int? = savedStateHandle[MoreDestination.INDEX_ARG]

    init {
        viewModelScope.launch {
            if (argIndex == -1) argIndex = null
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

    fun onSignOutClick() {
        viewModelScope.launch {
            try {
                clearDatabase(context)
                userRepository.clearUser()      //deleting user form datastore
                accountService.signOut()
                _uiState.update { it.copy(moveToAuth = true) }
            } catch (e: Exception) {
                // Handle error
                _uiState.update { it.copy(moveToAuth = false) }
            }
        }
    }

    fun onDeleteAccountClick() {
        viewModelScope.launch {
//        clearDatabase(context)
//        userRepository.clearUser()      //deleting user form datastore
//            userRepository.deleteUserDetails(accountService.currentUserId)      //deleting user from realtime db
//            accountService.deleteAccount()

            try {
                clearDatabase(context)
                accountService.deleteAccount()
                _uiState.update { it.copy(moveToAuth = true) }
            } catch (e: Exception) {
                // Handle error
                _uiState.update { it.copy(moveToAuth = false) }
            }
        }
    }
}