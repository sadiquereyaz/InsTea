package `in`.instea.instea.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.screens.schedule.ScheduleUiState

@Composable
fun ScheduleList(
    navigateToEditSchedule: (Int) -> Unit = {},
    uiState: ScheduleUiState,
    upsertAttendance: (Int, Int, AttendanceType) -> Unit,
    upsertTask: (Int, Int, String?, Int) -> Unit,
    onScheduleReminder: (CombinedScheduleTaskModel, String?, Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
//        Log.d("LIST", scheduleUiState.scheduleList.toString() ) // correct list
        items(items = uiState.scheduleList) { scheduleModel ->
//            Log.d("CURRENT_TASK", "subject name: ${scheduleModel.subject} \ntask: ${scheduleModel.task}")
            ScheduleItem(
                scheduleObj = scheduleModel,
                onEditClick = { navigateToEditSchedule(scheduleModel.scheduleId) },
                onAttendanceClick = { attendanceType ->
                    upsertAttendance(scheduleModel.scheduleId, scheduleModel.subjectId, attendanceType)
                },
                upsertTask = {task, remindBefore->
                    upsertTask(scheduleModel.scheduleId, scheduleModel.subjectId, task, remindBefore)
                    onScheduleReminder(scheduleModel.copy(timestamp = uiState.timestamp), task, remindBefore)
                },
                repeatReminderSwitchAction = { subName, repeat ->
//                        viewModel.modifySubjectInRepeatReminderList(
//                            subName,
//                            repeat,
//                        )
//                    onScheduleReminder()
                },
                reminderOn = true
            )
        }
    }
}