package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.screens.schedule.ScheduleUiState

@Composable
fun Calendar(
    scheduleUiState: ScheduleUiState,
    listState: LazyListState
) {
    // month
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.DateRange, contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = scheduleUiState.month,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
    //day date
    DayDate(listState, scheduleUiState)
}

@Composable
private fun DayDate(
    listState: LazyListState,
    scheduleUiState: ScheduleUiState
) {
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        val list = scheduleUiState.dayDateList
        items(list.size) { index ->
            DayDateLayout(
                onDateClick = {
//                        scheduleViewModel.selectDateIndex(index)
                },
                day = list[index].day,
                date = list[index].date,
                currentDate = index == 15,
                isSelected = index == scheduleUiState.selectedDateIndex
            )
        }
    }
}