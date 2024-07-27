package `in`.instea.instea.screens.more.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.datamodel.AttendanceType

data class MoreAttendance(
    val subject: String = "Subject Name",
    val percentage: Float = 89f,
    val attendanceMarked: Int = 30,
    val present: Int = 25,
    val absent: Int = 25,
    val cancelled: Int = 25
)

@Composable
fun AttendanceComp(modifier: Modifier = Modifier) {
    val attendanceList = listOf(
        MoreAttendance(),
        MoreAttendance(percentage = 63.3f),
        MoreAttendance(),
        MoreAttendance()
    )
    LazyColumn {
        itemsIndexed(attendanceList) { index, item ->
            AttendanceItem(item = item)
            if (index != attendanceList.lastIndex)
                Divider()
        }
    }
}

@Composable
fun AttendanceItem(
    modifier: Modifier = Modifier,
    item: MoreAttendance
) {
    val h = 70.dp
    Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
        Row(
            modifier = Modifier,
//            .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color: Color =
                if (item.percentage < 75) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            // line
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(2.dp)
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
                        text = "${item.percentage}%",
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
                AttendanceSubtitle(item = item, attendanceType = AttendanceType.MarkAttendance, subtitle1 = "Marked Attendance")
                Spacer(modifier = Modifier.weight(0.3f))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    AttendanceSubtitle(item = item, attendanceType = AttendanceType.Present,)
                    AttendanceSubtitle(item = item, attendanceType = AttendanceType.Absent,)
                }
            }
        }
    }
}

@Composable
private fun AttendanceSubtitle(
    item: MoreAttendance,
    attendanceType: AttendanceType,
    modifier: Modifier = Modifier,
    subtitle1: String = attendanceType.title
) {
    Row(
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
            text = "${subtitle1}: ${item.attendanceMarked}",
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AttendanceItemPreview() {
    AttendanceItem(item = MoreAttendance())
}

@Composable
@Preview(showBackground = true)
fun AttendancePreview() {
    AttendanceComp()
}