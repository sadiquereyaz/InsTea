package `in`.instea.instea.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import kotlinx.coroutines.launch

@Composable
fun AttendanceComposable(
    onAttendanceClick: (AttendanceType) -> Unit,
    scheduleObj: CombinedScheduleTaskModel
) {
    Box {
        var expanded by remember { mutableStateOf(false) }
        var coroutineScope = rememberCoroutineScope()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(end = 16.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                .padding(start = 4.dp, end = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch {
                            onAttendanceClick(AttendanceType.Present)
                        }
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = scheduleObj.attendance?.icon ?: Icons.Default.AddTask,
                    contentDescription = "attendance",
                    modifier = Modifier
                        .size(16.dp))
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = scheduleObj.attendance?.title ?: AttendanceType.MarkAttendance.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
            }

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
                modifier = Modifier.clickable {
                    expanded = !expanded
                },
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AttendanceType.values().forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.title) },
                    onClick = {
                        onAttendanceClick(option)
                        expanded = false
                    }
                )
            }
        }
    }
}