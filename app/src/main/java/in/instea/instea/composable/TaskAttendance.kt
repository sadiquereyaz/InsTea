package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.datamodel.ScheduleModel

@Composable
fun TaskAttendance(
    openBottomSheet: Boolean,
    subject: ScheduleModel,
    onAttendanceClick: () -> Unit
) {
    var openBottomSheet1 = openBottomSheet
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // task button
        Task(
            modifier = Modifier
                .weight(1f), openBottomSheet1, subject
        )
        //attendance
        Attendance(onAttendanceClick, subject)
    }
}

