package `in`.instea.instea.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.screens.schedule.ScheduleUiState
import kotlinx.coroutines.coroutineScope

@Composable
fun ScheduleList(
    navigateToEditSchedule: (Int) -> Unit = {},
    scheduleUiState: ScheduleUiState,
    onAttendanceClick: (Int, Int, AttendanceType) -> Unit,
    upsertTask: (Int, Int, String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        items(items = scheduleUiState.scheduleList, key = { it.scheduleId }) { scheduleModel ->
            ScheduleItem(
                scheduleModel = scheduleModel,
                onEditClick = { navigateToEditSchedule(scheduleModel.scheduleId) },
                onAttendanceClick = { attendanceType ->
                    onAttendanceClick(
                        scheduleModel.taskId,
                        scheduleModel.scheduleId,
                        attendanceType
                    )
                },
                upsertTask = {task->
//                    Log.d("ATT", scheduleModel.taskId.toString())
                    upsertTask(
                        scheduleModel.taskId,
                        scheduleModel.scheduleId,
                        task
                    )
                },
                repeatReminderSwitchAction = { subName, repeat ->
//                        viewModel.modifySubjectInRepeatReminderList(
//                            subName,
//                            repeat,
//                        )
                },
                reminderOn = true
            )
        }
    }
}