package `in`.instea.instea.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.screens.schedule.ScheduleUiState

@Composable
fun ScheduleList(
    scheduleUiState: ScheduleUiState,
    onAttendanceClick: (Int, Int, AttendanceType) -> Unit,
    upsertTask: (Int, Int, String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        itemsIndexed(scheduleUiState.classList) { index, scheduleObj ->
            ScheduleItem(
                scheduleObj = scheduleObj,
                onEditClick = {
//                        navController.navigate(route = InsteaScreens.EditSchedule.name)
                },
                onAttendanceClick = { attendanceType->
                    onAttendanceClick(scheduleObj.taskId, scheduleObj.scheduleId, attendanceType) },
                upsertTask = {
//                    Log.d("ATT", scheduleObj.taskId.toString())
                    upsertTask(scheduleObj.taskId, scheduleObj.scheduleId, scheduleObj.task ?:"")
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