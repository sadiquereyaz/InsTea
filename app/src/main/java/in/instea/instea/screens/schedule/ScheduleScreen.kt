package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.model.InsteaScreens
import `in`.instea.instea.model.schedule.AttendanceType
import `in`.instea.instea.model.schedule.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    scheduleViewModel: ScheduleViewModel = viewModel()
) {
    val scheduleUiState by scheduleViewModel.scheduleUiState.collectAsState()
    val listState = rememberLazyListState()
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
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            val list = scheduleViewModel.dayDateList
            items(list.size) { index ->
                DayDateLayout(
                    onDateClick = {
                        scheduleViewModel.selectDateIndex(index)
                    },
                    day = list[index].day,
                    date = list[index].date,
                    currentDate = index == 15,
                    isSelected = index == scheduleUiState.selectedDateIndex,
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp)
        ) {
            items(scheduleUiState.subjectList) { subject ->
                SubjectLayout(
                    subject = subject,
                    onEditClick = {
                        navController.navigate(route  = "InsteaScreens.EditSchedule")
                    },
                    onAttendanceClick = {
                       scheduleViewModel.updateAttendanceType(subject, AttendanceType.Present)
                    }
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

