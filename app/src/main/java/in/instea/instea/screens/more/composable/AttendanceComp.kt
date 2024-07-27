package `in`.instea.instea.screens.more.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.SubjectAttendanceSummaryModel
import `in`.instea.instea.screens.more.MoreUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceComp(
    modifier: Modifier = Modifier, uiState: MoreUiState,
    onMonthSelected: (Int) -> Unit
) {
    val attendanceList = uiState.attendanceSummaries
    val months = listOf(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )
    var expanded by remember { mutableStateOf(false) }

    // Get the current month
    /*  val currentMonth = remember {
          LocalDate.now().month.getDisplayName(TextStyle.FULL, Locale.getDefault())
      }*/

//    var selectedMonth by remember { mutableStateOf(currentMonth) }

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(start = 12.dp, top = 2.dp)
        ) {
            Text(months[uiState.selectedMonth])
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Dropdown Arrow"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            months.forEach { month ->
                DropdownMenuItem(
                    onClick = {
                        onMonthSelected(months.indexOf(month))
                        expanded = false
                    },
                    text = { Text(text = month) }
                )
            }
        }
    }
    LazyColumn {
        itemsIndexed(attendanceList) { index, item ->
            if (index != 0) Divider()
            AttendanceItem(item = item)

        }
    }
}

@Composable
fun AttendanceItem(
    modifier: Modifier = Modifier,
    item: SubjectAttendanceSummaryModel
) {
    val h = 70.dp
    Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
        Row(
            modifier = Modifier,
//            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val absentClasses = item.absentClasses
            val attendedClasses = item.attendedClasses
            val totalClasses = item.totalClasses
            val percentage: Float = ((attendedClasses.toFloat() / totalClasses.toFloat()) * 100)
            Log.d("PERCENT", percentage.toString())
            val color: Color = /*MaterialTheme.colorScheme.primary*/
                if (percentage < 75.0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            // line
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(2.dp)
                    .background(color = color)
                    .height(h)
            )
            // circle
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .size(h)
//                .align(Alignment.Center)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "$percentage%",
                        modifier = Modifier,
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .height(h)
//            horizontalAlignment =
            ) {
                Text(
                    text = item.subject,
                    fontSize = 18.sp,
                    modifier = Modifier,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                AttendanceSubtitle(
                    subtitle = "Marked Attendance: $totalClasses",
                    attendanceType = AttendanceType.MarkAttendance
                )
                Spacer(modifier = Modifier.weight(0.3f))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AttendanceSubtitle(
                        subtitle = "Present: $attendedClasses",
                        attendanceType = AttendanceType.Present,
                    )
                    AttendanceSubtitle(
                        subtitle = "Absent: $absentClasses",
                        attendanceType = AttendanceType.Absent,
                    )
                }
            }
        }
    }
}

@Composable
private fun AttendanceSubtitle(
    subtitle: String,
    attendanceType: AttendanceType,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(18.dp)
                .padding(end = 4.dp),
            imageVector = attendanceType.icon,
            contentDescription = null,
            tint = attendanceType.tint ?: MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = subtitle,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AttendanceItemPreview() {
//    AttendanceItem(item = SubjectAttendanceSummaryModel())
}

@Composable
@Preview(showBackground = true)
fun AttendancePreview() {
//    AttendanceComp()
}