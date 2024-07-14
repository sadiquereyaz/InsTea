package `in`.instea.instea.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.screens.schedule.ScheduleUiState

@Composable
fun ScheduleList(scheduleUiState: ScheduleUiState) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        itemsIndexed(scheduleUiState.classList) { index, subject ->
            ScheduleItem(
                subject = subject,
                onEditClick = {
//                        navController.navigate(route = InsteaScreens.EditSchedule.name)
                },
                onAttendanceClick = {
//                        viewModel.updateAttendanceType(AttendanceType.Present)
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