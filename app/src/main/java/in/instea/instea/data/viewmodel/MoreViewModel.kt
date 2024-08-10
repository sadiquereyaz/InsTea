package `in`.instea.instea.data.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.instea.instea.data.dao.InsteaDatabase.Companion.clearDatabase
import `in`.instea.instea.data.repo.AccountService
import `in`.instea.instea.data.repo.ScheduleRepository
import `in`.instea.instea.data.repo.UserRepository
import `in`.instea.instea.screens.more.MoreDestination
import `in`.instea.instea.screens.more.MoreUiState
import `in`.instea.instea.screens.more.composable.TaskModel
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
        getAllTask()
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
    fun getAllTask(){
        viewModelScope.launch {
            val tasks=scheduleRepository.getAllTasks()
            _uiState.update {
                it.copy(
                    taskList = tasks
                )
            }
        }

    }
    fun onDeleteTaskClicked(taskModel: TaskModel){
        viewModelScope.launch {
            scheduleRepository.deleteTaskById(scheduleId = taskModel.scheduleId, timeStamp =  taskModel.timestamp, subjectId = taskModel.subjectId)
            Log.d("View Model", "VIew Model delete task clicked ${taskModel.scheduleId} ${taskModel.subjectId} ")
        }
        getAllTask()
    }

    fun onSignOutClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                clearDatabase(context)
                userRepository.clearUser()      //deleting user form datastore
                accountService.signOut()
                _uiState.update { it.copy(moveToAuth = true) }
            } catch (e: Exception) {
                // Handle error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage,
                        moveToAuth = false
                    )
                }
                showSnackBar()
            }
        }
    }

    fun onDeleteAccountClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val deleteResult = accountService.deleteAccount()
                deleteResult.onSuccess {
                    clearDatabase(context)
                    _uiState.update {
                        it.copy(
                            moveToAuth = true,
//                            isLoading = false,
//                            errorMessage = "Account deleted successfully"
                        )
                    }
//                    showSnackBar()
                }.onFailure { failureMsg ->
                    Log.d("MORE_VM", "failed result of deleting account: $failureMsg")
                    _uiState.update {
                        it.copy(
                            moveToAuth = false,
                            isLoading = false,
                            errorMessage = failureMsg.message
                                ?: "Error while deleting your account"
                        )
                    }
                    showSnackBar()
                }
            } catch (e: Exception) {
                // Handle error
                Log.d("MORE_VM", "outer exception executed with error ${e.localizedMessage}")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage,
                        moveToAuth = false
                    )
                }
                showSnackBar()
            }
        }
    }

    private fun showSnackBar() {
        _uiState.value.showSnackBar = !_uiState.value.showSnackBar
    }
}