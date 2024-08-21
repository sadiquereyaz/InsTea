package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.composable.CalendarComposable
import `in`.instea.instea.composable.ScheduleList
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.ScheduleViewModel
import kotlinx.coroutines.launch

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToEditSchedule: (Int, String) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    //scroll to the initial index
    LaunchedEffect(key1 = true) {
        val initialIndex = 13
        listState.scrollToItem(initialIndex - 1)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        CalendarComposable(
            uiState,
            listState,
            onDateClick = {
                coroutineScope.launch {
                    viewModel.onDateClick(selectedIndex = it)
                }
            }
        )
        //add class
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            IconTextBtn(
                onClick = { navigateToEditSchedule(-1, uiState.selectedDay) },
                btnIcon = Icons.Default.Add,
                btnText = "Add",
            )
        }

        // schedule list
        ScheduleList(
            navigateToEditSchedule = { scheduleId: Int ->
                navigateToEditSchedule(scheduleId, uiState.selectedDay)
            },
            uiState = uiState,
            upsertAttendance = { scheduleId: Int, subjectId: Int, attendance ->
                coroutineScope.launch {
                    viewModel.upsertAttendance(
                        subjectId = subjectId,
                        attendance = attendance,
                        scheduleId = scheduleId
                    )
                }
            },
            upsertTask = { scheduleId: Int, subjectId: Int, task: String?, taskReminder: Int ->
                coroutineScope.launch {
                    viewModel.upsertTask(
                        scheduleId = scheduleId,
                        subjectId = subjectId,
                        task = task,
                        taskReminderBefore = taskReminder
                    )
                }
            },
            onScheduleReminder = { scheduleModel: CombinedScheduleTaskModel, task: String?, hour: Int ->
                viewModel.scheduleReminder(
                    task = task?:"",
                    remindBefore = hour,
                    scheduleObj = scheduleModel
                )
            },
        )
    }
}

@Composable
private fun IconTextBtn(
    onClick: () -> Unit,
    btnIcon: ImageVector,
    btnText: String,
) {
    val coroutineScope = rememberCoroutineScope()
    TextButton(onClick = { coroutineScope.launch { onClick() } }) {
        Icon(imageVector = btnIcon, contentDescription = "Refresh")
        Text(
            text = btnText, modifier = Modifier.padding(start = 4.dp),
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleLayoutPreview() {
//    ScheduleScreen(rememberNavController())
}

