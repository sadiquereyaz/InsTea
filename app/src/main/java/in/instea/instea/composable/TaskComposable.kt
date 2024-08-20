package `in`.instea.instea.composable

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.screens.more.composable.PickerRow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskComposable(
    modifier: Modifier = Modifier,
    scheduleObj: CombinedScheduleTaskModel,
    upsertTask: (String?, Int) -> Unit
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    var task by remember { mutableStateOf(scheduleObj.task) }
    var taskTemp by remember { mutableStateOf("") }
    var isSwitchVisible by remember { mutableStateOf(false) }
    var remindBefore by remember { mutableIntStateOf(scheduleObj.taskReminderBefore) }
    var isReminderOn by remember { mutableStateOf(remindBefore > 0) }
//    Log.d("task composable", "isReminderOn $isReminderOn")
//    Log.d("task composable", "reminderBefore $scheduleObj.taskReminderBefore")
/*
    LaunchedEffect(scheduleObj.task) {
        task = scheduleObj.task ?: ""
    }*/
    LaunchedEffect(task) {
        isSwitchVisible = !task.isNullOrBlank()
    }
    LaunchedEffect(remindBefore) {
        isReminderOn = remindBefore!=0
    }
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .clip(shape = RoundedCornerShape(50))
            .clickable {
                openBottomSheet = true
            }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // task icon and text
        Icon(
            imageVector = Icons.Default.List,
            contentDescription = "task",
            modifier = Modifier.size(16.dp),
        )
            Text(
                text = task ?: "Add Task",
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis, // Truncate text with ellipsis
                modifier = Modifier.padding(start = 4.dp),
            )
    }
    //bottom sheet
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            // Reminder Switch
            Column(
                modifier = Modifier.padding(end = 16.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isSwitchVisible) {
                        Switch(
                            modifier = Modifier
                                .padding(start = 16.dp),
                            checked = isReminderOn,
                            onCheckedChange = {
                                Log.d("SWITCH", it.toString())
                                isReminderOn = it
                                remindBefore = if (!it) 0 else 18;
                                Log.d("SWITCH_HOUR", remindBefore.toString())
                            },
                            thumbContent = {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (isReminderOn) {
                            Row(
                                modifier = Modifier,
//                            horizontalAlignment = Alignment.CenterHorizontally,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Remind before  ")
                                remindBefore.let {
                                    PickerRow(
                                        isMinusEnabled = remindBefore > 0,
                                        displayText = it.toString(),
                                        increase = { remindBefore = (it + 1) },
                                        decrease = {
                                            remindBefore = (it - 1)
                                            if (remindBefore == 0) isReminderOn = false
                                        }
                                    )
                                }
                                Text("  hours")
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    OutlinedTextField(
                        value = task ?: "",
                        onValueChange = {
                            task = it
                            if (it.isBlank()) {
                                isReminderOn = false
                                remindBefore = 0
                                isSwitchVisible = false
                            } else {
                                isSwitchVisible = true
                            }
//                            scheduleObj.task = it
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = 16.dp,
                                start = 16.dp,
                                bottom = 16.dp
                            ),
                        label = { Text("Task") },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        )
                    )
                    Button(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 16.dp, top = 24.dp),
                        shape = RoundedCornerShape(8),
                        onClick = {
                            Log.d("SAVE_BTN", "upsert executed $remindBefore")
                            upsertTask(task, remindBefore)
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