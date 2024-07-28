package `in`.`in`.instea.instea.screens.more.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.screens.more.MoreUiState

data class taskModel(
    val task: String,
    val timeStamp: Int
)

@Composable
fun AllTask(
    uiState: MoreUiState,
    modifier: Modifier = Modifier
) {
//    val task =
//        "This is the content of the task to be displayed in more screen along with the date and time when it was created"
//    val taskList = listOf(task, task, task, task, task)
    val taskList=uiState.taskList
    LazyColumn(
        modifier = modifier/*.padding(vertical = 16.dp)*/
    ) {
        itemsIndexed(taskList) { index, item ->
            TaskItem(task = item)
            if (index != taskList.lastIndex)
                Divider()
        }
    }
}

@Composable
fun TaskItem(modifier: Modifier = Modifier, task: String) {
    Column(
        modifier = modifier/*.padding(40.dp)*/
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "27/02/2024",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /*TODO*/ }) {
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