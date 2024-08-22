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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.SubjectAttendanceSummaryModel
import `in`.instea.instea.screens.more.MoreUiState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceComp(
    modifier: Modifier = Modifier,
    uiState: MoreUiState,
    onDateSelected: (LocalDate) -> Unit = {}
) {
    val attendanceList = uiState.attendanceSummaries
    var expanded by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val coroutineScope = rememberCoroutineScope()
    // Calculate totals
    var totalCommenced = 0
    var present = 0
    var absent = 0

    attendanceList.forEach { item ->
        totalCommenced += item.totalClasses
        present += item.attendedClasses
        absent += item.absentClasses
    }

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                OutlinedButton(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp, bottom = 2.dp),
                ) {
                    Text(selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")))
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown Arrow"
                    )
                }
                Text(
                    text = "Showing summary of marked classes only.",
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            Box(modifier = Modifier.padding(start = 16.dp)) {
                DropdownMenu(
//            modifier = Modifier.align(Alignment.TopEnd),
                    expanded = expanded,
                    onDismissRequest = {
                        coroutineScope.launch {
                            expanded = false
                            onDateSelected(selectedDate)
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Month selection
                        PlusMinusBtn(
                            displayText = selectedDate.month.getDisplayName(
                                TextStyle.SHORT,
                                Locale.getDefault()
                            ),
                            increase = { selectedDate = selectedDate.plusMonths(1) },
                            decrease = { selectedDate = selectedDate.minusMonths(1) },
                            isPlusEnabled = true
                        )

                        // Year selection
                        PlusMinusBtn(
                            displayText = selectedDate.year.toString(),
                            increase = { selectedDate = selectedDate.plusYears(1) },
                            decrease = {
                                selectedDate = selectedDate.minusYears(1)
                                Log.d("click", selectedDate.year.toString())
                            },
                            isPlusEnabled = true
                        )
                    }
                }
            }
        }
    }
    LazyColumn {
        item {
            Divider()
            if (totalCommenced > 0) AttendanceItem(
                item = SubjectAttendanceSummaryModel(
                    subject = "Overall",
                    totalClasses = totalCommenced,
                    attendedClasses = present,
                    absentClasses = absent
                )
            ) else {
                Text(
                    text = "Oops! No Attendance Marked in this Month",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 32.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        itemsIndexed(attendanceList) { index, item ->
            /* if (index != 0) */Divider()
            AttendanceItem(item = item)
        }
    }
}

@Composable
fun PlusMinusBtn(
    displayText: String,
    increase: () -> Unit,
    decrease: () -> Unit,
    isMinusEnabled: Boolean = true,
    isPlusEnabled: Boolean = true
) {
    val iconBtnColor = IconButtonColors(
        contentColor = MaterialTheme.colorScheme.onPrimary,
        containerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(
            colors = iconBtnColor,
            enabled = isMinusEnabled,
            onClick = decrease,
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Remove, contentDescription = "minus",
//                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(displayText)
        IconButton(
            colors = iconBtnColor,
            enabled = isPlusEnabled,
            onClick = increase,
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "add",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun AttendanceItem(
    modifier: Modifier = Modifier,
    item: SubjectAttendanceSummaryModel
) {
    val h = 70.dp
    Box(modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
        Row(
            modifier = Modifier,
//            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val absentClasses = item.absentClasses
            val attendedClasses = item.attendedClasses
            val totalClasses = item.totalClasses
            val percentage: Float =
                ((attendedClasses.toFloat() / totalClasses.toFloat()) * 1000).roundToInt()
                    .toFloat() / 10
            val color: Color =
                if (percentage < 75.0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            // line
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(4.dp)
                    .clip(
                        shape = RoundedCornerShape(
                            topStartPercent = 100,
                            topEndPercent = 0,
                            bottomStartPercent = 100,
                            bottomEndPercent = 0
                        )
                    )
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

/*@Composable
@Preview(showBackground = true)
fun AttendanceItemPreview() {
//    AttendanceItem(item = SubjectAttendanceSummaryModel())
}*/

@Composable
@Preview
fun AttendanceCompPreview() {
    AttendanceItem(
        item = SubjectAttendanceSummaryModel(
            subject = "Maths",
            attendedClasses = 25,
            absentClasses = 5,
            totalClasses = 30
        )
    )
}