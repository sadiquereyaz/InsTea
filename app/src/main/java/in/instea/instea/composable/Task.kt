package `in`.instea.instea.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.datamodel.ScheduleModel

@Composable
fun Task(
    modifier: Modifier = Modifier,
    openBottomSheet1: Boolean,
    subject: ScheduleModel
) {
    var openBottomSheet11 = openBottomSheet1
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clickable {
//                            showBottomSheet = true
                openBottomSheet11 = true
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

    }
}