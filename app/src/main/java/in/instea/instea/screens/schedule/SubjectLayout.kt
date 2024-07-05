package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.model.schedule.AttendanceType
import `in`.instea.instea.model.schedule.SubjectModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectLayout(
    modifier: Modifier = Modifier.fillMaxWidth(),
    isBubbleFilled: Boolean = false,
    subject: SubjectModel,
    onReminderClick: () -> Unit = {},
    onEditClick: () -> Unit,
    onAttendanceClick: () -> Unit
) {
//    val attendanceModifier = Modifier.clickable {
//        subject.attendanceType = AttendanceType.Present
//    }

//    var at by rememberSaveable{ mutableStateOf(subject.attendanceType) }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    Row(
        modifier = modifier
    ) {
        //time
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(text = subject.startTime, fontSize = 20.sp)
            Text(text = subject.endTime, fontSize = 12.sp)
        }
        //bubble and vertical line
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.padding(start = 8.dp, end = 16.dp),
        ) {
            //line (timeline)
            Box(
                modifier = Modifier
//                .padding(start = 8.dp)
                    .width(1.dp)
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
            )
            //bubble
            Box(
                modifier = if (isBubbleFilled) {
                    Modifier
                        .size(12.dp)
                        .clip(RoundedCornerShape(50))
                        .border(
                            2.dp, MaterialTheme.colorScheme.error, RoundedCornerShape(50)
                        )
                        .background(
                            MaterialTheme.colorScheme.error
                        )
                } else {
                    Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(50))
                        .border(
                            2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50)
                        )
                        .background(

                            MaterialTheme.colorScheme.background

                        )
                }
            )
        }
        // subject box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            // subject headline row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                //reminder
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "reminder",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = subject.subjectName,
                    fontWeight = FontWeight.Bold, fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        modifier = Modifier.size(20.dp)
                    )

                }
            }

            //task and attendance
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // task button
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                        .clickable {
//                            showBottomSheet = true
                            openBottomSheet = true
                        }, verticalAlignment = Alignment.CenterVertically

                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "task",
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = subject.task,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis, // Truncate text with ellipsis
                        modifier = Modifier.padding(start = 4.dp),

                        )
                    //bottom sheet
                    if (openBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { openBottomSheet = false },
                            sheetState = bottomSheetState,
                        ) {
                            var isReminderOn by rememberSaveable { mutableStateOf(false) }
                            var remindBefore12Hours by rememberSaveable { mutableStateOf(true) }
                            var remindBefore24Hours by rememberSaveable { mutableStateOf(false) }

                            var task by remember { mutableStateOf(subject.task) }

                            // Reminder Switch
                            Column {
                                Switch(
                                    modifier = Modifier
                                        .align(Alignment.Start)
                                        .padding(start = 16.dp),
                                    checked = isReminderOn,
                                    onCheckedChange = { isReminderOn = it },
                                    thumbContent = {
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                )

                                // Checkboxes (visible only if reminder is on)
                                if (isReminderOn) {
                                    Column(
                                        modifier = Modifier
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = remindBefore12Hours,
                                                onCheckedChange = { remindBefore12Hours = it }
                                            )
                                            Text(
                                                "Remind before 12 hours",
                                            )
                                        }
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Checkbox(
                                                checked = remindBefore24Hours,
                                                onCheckedChange = { remindBefore24Hours = it }
                                            )
                                            Text(
                                                "Remind before 24 hours",
                                            )
                                        }
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.Top,
                                    modifier = Modifier.padding(bottom = 32.dp)
                                ) {

                                    OutlinedTextField(
                                        value = task,
                                        onValueChange = {
                                            task = it
                                            subject.task = it
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(
                                                top = 16.dp,
                                                start = 16.dp,
                                                bottom = 16.dp
                                            ),
                                        label = { Text("Task") }
                                    )
                                    Button(
                                        modifier = Modifier
                                            .padding(start = 8.dp, end = 16.dp, top = 24.dp),
                                        shape = RoundedCornerShape(8),
                                        onClick = {
                                            scope.launch { bottomSheetState.hide() }
                                                .invokeOnCompletion {
                                                    if (!bottomSheetState.isVisible) {
                                                        openBottomSheet = false
                                                    }
                                                }
                                        }) {
                                        Icon(
                                            modifier = Modifier
                                                .size(height = 36.dp, width = 28.dp),
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "check",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                //attendance
                Box() {
                    var expanded by remember { mutableStateOf(false) }
//                    var attendance by remember { mutableStateOf(subject.attendanceType) }
//                    val attendanceModifier = Modifier.clickable { subject.attendanceType = AttendanceType.Present }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                            .padding(start = 4.dp, end = 4.dp)
                    ) {

                        Icon(imageVector = subject.attendanceType.icon,
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
//                                subject.attendanceType = AttendanceType.Present
                            },
                            text = subject.attendanceType.name,
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
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        AttendanceType.values().forEach { option ->
                            DropdownMenuItem(text = { Text(text = option.name) }, onClick = {
                                subject.attendanceType = option
                                expanded = false
                            })
                        }
                    }
                }
            }
        }
    }
}
