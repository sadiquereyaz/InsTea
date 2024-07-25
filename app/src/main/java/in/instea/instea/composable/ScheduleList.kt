package `in`.instea.instea.composable

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.screens.schedule.ScheduleUiState

@Composable
fun ScheduleList(
    navigateToEditSchedule: (Int) -> Unit = {},
    scheduleUiState: ScheduleUiState,
    onAttendanceClick: (Int, AttendanceType) -> Unit,
    upsertTask: (Int, String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
//        Log.d("LIST", scheduleUiState.scheduleList.toString() ) // correct list
        items(items = scheduleUiState.scheduleList) { scheduleModel ->
            Log.d("CURRENT_TASK", "${scheduleModel.subject} ${scheduleModel.task}")
            ScheduleItem(
                scheduleModel = scheduleModel,
                onEditClick = { navigateToEditSchedule(scheduleModel.scheduleId) },
                onAttendanceClick = { attendanceType ->
                    onAttendanceClick(scheduleModel.scheduleId, attendanceType)
                },
                upsertTask = {task->
//                    Log.d("ATT", scheduleModel.taskId.toString())
                    upsertTask(
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