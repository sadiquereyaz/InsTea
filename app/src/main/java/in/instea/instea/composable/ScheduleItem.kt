package `in`.instea.instea.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleItem(
    scheduleModel: CombinedScheduleTaskModel,
    modifier: Modifier = Modifier,
    isBubbleFilled: Boolean = false,
    onReminderClick: () -> Unit = {},
    onEditClick: () -> Unit,
    onAttendanceClick: (AttendanceType) -> Unit,
    repeatReminderSwitchAction: (subject: String, repeat: Boolean) -> Unit,
    reminderOn: Boolean,
    upsertTask: (String)->Unit
) {
//    val attendanceModifier = Modifier.clickable {
//        attendanceType = AttendanceType.Present
//    }

//    var at by rememberSaveable{ mutableStateOf(attendanceType) }

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)

    var showReminderDialog by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        //time
        Column(
            horizontalAlignment = Alignment.End
        ) {
            //time
            Text(text = scheduleModel.startTime.format(DateTimeFormatter.ofPattern("hh:mm a")), fontSize = 14.sp)
            Text(text = scheduleModel.endTime.format(DateTimeFormatter.ofPattern("hh:mm a")), fontSize = 10.sp)
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
                IconButton(onClick = {
                    showReminderDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        tint = if (reminderOn) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                        contentDescription = "reminder",
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = scheduleModel.subject?:"",
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
            TaskAttendance(openBottomSheet, scheduleModel, onAttendanceClick, upsertTask = upsertTask)
        }
    }
    // Reminder Dialog
    if (showReminderDialog) {
        AlertDialog(
            onDismissRequest = { showReminderDialog = false },
            title = { Text("Set Reminder") },
            text = {
                Column {
                    var remindBefore12Hours by rememberSaveable { mutableStateOf(false) }
                    var remindBefore24Hours by rememberSaveable { mutableStateOf(false) }
                    var repeatSwitchOn by rememberSaveable {
                        mutableStateOf(reminderOn)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = repeatSwitchOn,
                            onCheckedChange = {
                                repeatSwitchOn = it
//                                repeatReminderSwitchAction( it)
                            }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = remindBefore12Hours,
                            onCheckedChange = { remindBefore12Hours = it }
                        )
                        Text("Remind before 12 hours")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = remindBefore24Hours,
                            onCheckedChange = { remindBefore24Hours = it }
                        )
                        Text("Remind before 24 hours")
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showReminderDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
    //bottom sheet
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            var isReminderOn by rememberSaveable { mutableStateOf(false) }
            var remindBefore12Hours by rememberSaveable { mutableStateOf(true) }
            var remindBefore24Hours by rememberSaveable { mutableStateOf(false) }

            var task by remember { mutableStateOf(false) }

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

//                    OutlinedTextField(
//                        value = task,
//                        onValueChange = {
//                            task = it
//                            task = it
//                        },
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(
//                                top = 16.dp,
//                                start = 16.dp,
//                                bottom = 16.dp
//                    )
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

