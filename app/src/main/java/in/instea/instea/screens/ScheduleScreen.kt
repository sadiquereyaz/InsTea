package `in`.instea.instea.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.model.DayDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ScheduleScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    //scroll to the initial index
    LaunchedEffect(key1 = true) {
        val initialIndex = 13
        listState.scrollToItem(initialIndex - 1)
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
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
                text = "July",
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
            items(getDayDateList().size) { index ->
                DayDateLayout(
                    day = getDayDateList()[index].day,
                    date = getDayDateList()[index].date,
                    currentDate = index == 15
                )
            }
        }
        LazyColumn(modifier = Modifier.padding(top = 64.dp)) {
            items(30) {
                ScheduleLayout()
            }
        }
    }

}


@Composable
fun DayDateLayout(
    modifier: Modifier = Modifier.padding(top = 8.dp),
    day: String,
    date: String,
    currentDate: Boolean = false,

    ) {
    var isSelected by remember {
        mutableStateOf(false)
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = day, fontWeight = FontWeight.Bold)
        Box(
            modifier = Modifier.clickable { isSelected = !isSelected }
        ) {
            Text(
                text = date,
                modifier = if (currentDate) {
                    modifier
                        .clip(
                            RoundedCornerShape(50)
                        )
                        .background(
                            MaterialTheme.colorScheme.primary
                        )
                        .padding(4.dp)
                } else if (isSelected) {
                    modifier
                        .border(
                            2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(4.dp)
                } else modifier,
                color = if (currentDate) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = if (currentDate) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                }
            )
        }
    }
}

fun getDayDateList(): List<DayDate> {
    val calendar = Calendar.getInstance()
    val dayDateList = mutableListOf<DayDate>()
    // Start from 15 days before the current day
    calendar.add(Calendar.DAY_OF_YEAR, -15)

    repeat(45) {
        val day =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()) ?: ""
        val date = SimpleDateFormat("dd", Locale.getDefault()).format(calendar.time)
        dayDateList.add(DayDate(day = day, date = date))
        calendar.add(Calendar.DAY_OF_WEEK, 1)
    }
    return dayDateList
}

@Composable
fun ScheduleLayout(
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Row(
        modifier = modifier
    ) {
        //time
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(text = "09:00", fontSize = 24.sp)
            Text(text = "10:00", fontSize = 16.sp)
        }
        //bubble and vertical line
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
//                .padding(start = 8.dp)
                    .width(1.dp)
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(RoundedCornerShape(50))
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.onBackground,
                        RoundedCornerShape(50)
                    )
                    .background(MaterialTheme.colorScheme.background),

                )

        }

        // subject box
        Column(
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            // subject headline row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "reminder")
                }
                Text(text = "Comp. Org.", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "edit")

                }
            }

            //task and attendance
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // task button
                Row(
                    modifier = Modifier
                        . padding(start = 16.dp)
                        .clickable { },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "task",
                        modifier = Modifier.size(16.dp),
                    )
                    Text(text = "Add Task", fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                }
                //attendance
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable { }
//                        .padding(top = 12.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                        .padding( start = 4.dp, end =  4.dp)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "attendance",
                         modifier = Modifier.size(16.dp))
                    Text(text = "Attendance" , fontSize = 12.sp/*modifier = Modifier.padding(start = 4.dp)*/)
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .height(32.dp)
                            .width(1.dp)
                            .background(MaterialTheme.colorScheme.onBackground)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "attendance",
//                        modifier = Modifier.size(16.dp)
                    )

                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleLayoutPreview() {
    ScheduleScreen(rememberNavController())
}

