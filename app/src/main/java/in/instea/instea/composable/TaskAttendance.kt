package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel
import `in`.instea.instea.data.datamodel.ScheduleModel

@Composable
fun TaskAttendance(
    openBottomSheet: Boolean,
    scheduleObj: CombinedScheduleTaskModel,
    onAttendanceClick: () -> Unit,
    updateTask: (String) -> Unit,

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
        TaskComposable(
            modifier = Modifier.weight(1f),
            scheduleObj=scheduleObj,
            updateTask = {updateTask(it)}
        )
        //attendance
        Attendance(onAttendanceClick, scheduleObj)
    }
}

