package `in`.instea.instea.presentation.more.composable

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.presentation.more.MoreUiState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class TaskModel(
    val task: String,
    val timestamp: Int,
    val scheduleId: Int,
    val subjectId: Int
)

@Composable
fun AllTask(
    uiState: MoreUiState,
    modifier: Modifier = Modifier,
    onDeleteTask:(TaskModel)->Unit={}
) {
//    val task =
//        "This is the content of the task to be displayed in more screen along with the date and time when it was created"
//    val taskList = listOf(task, task, task, task, task)

    val taskList = uiState.taskList
    LazyColumn(
        modifier = modifier/*.padding(vertical = 16.dp)*/
    ) {
        itemsIndexed(taskList) { index, item ->
            if(item.task.isNotBlank())
            TaskItem(
                task = item.task,
                date = localDateFromTimestamp(item.timestamp),
                ondeleteButtonClicked = {
                    onDeleteTask(item) })
            if (index != taskList.lastIndex)
                HorizontalDivider()
        }
        item {
//            Divider()
            if (taskList.isEmpty())
                Text(
                    text = "Oh! you've never saved any task!",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 32.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )

        }
    }
}

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: String,
    date: String,
    ondeleteButtonClicked: () -> Unit
) {
    Column(
        modifier = modifier/*.padding(40.dp)*/
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                ondeleteButtonClicked()
                Log.d("deleteButton", "TaskItem: delete task clicked")
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = null
                )
            }
        }
        Text(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp), text = task)
    }

}

private fun localDateFromTimestamp(timestamp: Int): String {
    // Extract the year, month, and day from the timestamp
    val yearMonthDay = timestamp.toString().padStart(6, '0') // Ensure 6 digits

    // Parse year, month, and day from the string
    val year = 2000 + yearMonthDay.substring(0, 2).toInt() // Assumes 2000s; adjust if needed
    val month = yearMonthDay.substring(2, 4).toInt()
    val day = yearMonthDay.substring(4, 6).toInt()

    // Create a LocalDate with the given year, month, and day
    val date = LocalDate.of(year, month, day)

    // Define the format you want the date to be returned in
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")

    // Format the LocalDate to a string
    return date.format(formatter)
}