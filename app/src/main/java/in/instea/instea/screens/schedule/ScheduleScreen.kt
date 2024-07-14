package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.ScheduleViewModel
import `in`.instea.instea.navigation.InsteaScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = viewModel(factory = AppViewModelProvider.Factory),
    ) {
    val scheduleUiState by viewModel.scheduleUiState.collectAsState()
//    val listState = rememberLazyListState()

    //scroll to the initial index
//    LaunchedEffect(key1 = true) {
//        val initialIndex = 13
//        listState.scrollToItem(initialIndex - 1)
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,

            ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.DateRange, contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = scheduleUiState.selectedMonth,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 2.dp)
            )

        }
        //day date
        LazyRow(
//            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
//            val list = scheduleViewModel.dayDateList
//            items(list.size) { index ->
//                DayDateLayout(
//                    onDateClick = {
//                        scheduleViewModel.selectDateIndex(index)
//                    },
//                    day = list[index].day,
//                    date = list[index].date,
//                    currentDate = index == 15,
//                    isSelected = index == scheduleUiState.selectedDateIndex
//                )
//            }
        }

        //add class
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .clickable {
                    navController.navigate(route = InsteaScreens.EditSchedule.name)
                },
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "add class",
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Add Class",
                color = MaterialTheme.colorScheme.primary,
//                 fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        // subject list
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
        ) {
            itemsIndexed(scheduleUiState.classList) { index, subject ->
                Text(text = "subject.subject")
                TextButton(onClick = {
//                    scheduleViewModel.updateAttendance(index)
                }) {
                    Text(text = subject.attendance)
                }
                SubjectLayout(
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
}

@Preview(showBackground = true)
@Composable
fun ScheduleLayoutPreview() {
    ScheduleScreen(rememberNavController())
}

