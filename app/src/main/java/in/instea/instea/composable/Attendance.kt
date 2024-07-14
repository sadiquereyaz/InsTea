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
import androidx.compose.material.icons.filled.Check
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.ScheduleModel

@Composable
 fun Attendance(
    onAttendanceClick: () -> Unit,
    subject: ScheduleModel
) {
    Box() {
        var expanded by remember { mutableStateOf(false) }
//                    var attendance by remember { mutableStateOf(attendanceType) }
//                    val attendanceModifier = Modifier.clickable { attendanceType = AttendanceType.Present }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(end = 16.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                .padding(start = 4.dp, end = 4.dp)
        ) {

            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "attendance",
                modifier = Modifier
                    .clickable {
                        onAttendanceClick()
//                                attendanceType = AttendanceType.Present
                    }

                    .size(16.dp))
            Text(
                modifier = Modifier.clickable {
                    onAttendanceClick()
//                                at = AttendanceType.Present
//                                attendanceType = AttendanceType.Present
                },
                text = subject.attendance,
                fontSize = 12.sp/*modifier = Modifier.padding(start = 4.dp)*/
            )
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
                    text = { Text(text = option.name) },
                    onClick = {
//                                    attendanceType = option
                        expanded = false
                    }
                )
            }
        }
    }
}