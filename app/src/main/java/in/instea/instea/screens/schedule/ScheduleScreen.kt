package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.composable.AddClassButton
import `in`.instea.instea.composable.CalendarComposable
import `in`.instea.instea.composable.ScheduleList
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
        AddClassButton(
            modifier = Modifier
                .align(Alignment.End)
                .clickable {
                    navigateToEditSchedule(0, uiState.selectedDay)
                },
        )

        // schedule list
        ScheduleList(
            scheduleUiState = uiState,
            onAttendanceClick = {scheduleId: Int, attendance ->
                coroutineScope.launch {
                    viewModel.upsertAttendance(attendance = attendance, scheduleId = scheduleId)
                }
            },
            upsertTask = {scheduleId: Int, task: String ->
                coroutineScope.launch {
                    viewModel.upsertTask(scheduleId = scheduleId, task = task)
                }
            },
            navigateToEditSchedule = { id: Int ->
                navigateToEditSchedule(id, uiState.selectedDay)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleLayoutPreview() {
//    ScheduleScreen(rememberNavController())
}

