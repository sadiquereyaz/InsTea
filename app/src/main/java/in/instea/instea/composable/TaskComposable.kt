package `in`.instea.instea.composable

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskComposable(
    modifier: Modifier = Modifier,
    scheduleObj: CombinedScheduleTaskModel,
    upsertTask:(String)->Unit
) {
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by rememberSaveable { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded)
    var task by rememberSaveable { mutableStateOf(scheduleObj.task) }

    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clickable {
                openBottomSheet = true
            }, verticalAlignment = Alignment.CenterVertically
    ) {
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
            var isReminderOn by rememberSaveable { mutableStateOf(false) }
            var remindBefore12Hours by rememberSaveable { mutableStateOf(true) }
            var remindBefore24Hours by rememberSaveable { mutableStateOf(false) }

//            var task by remember { mutableStateOf(scheduleObj.task ?:"") }

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
                        value = task ?: "",
                        onValueChange = {
                            task = it
//                            scheduleObj.task = it
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
                            task?.let { upsertTask(it) }
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