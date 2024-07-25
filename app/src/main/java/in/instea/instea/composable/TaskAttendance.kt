package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.datamodel.AttendanceType
import `in`.instea.instea.data.datamodel.CombinedScheduleTaskModel

@Composable
fun TaskAttendance(
    openBottomSheet: Boolean,
    scheduleObj: CombinedScheduleTaskModel,
    onAttendanceClick: (AttendanceType) -> Unit,
    upsertTask: (String) -> Unit,

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
            scheduleObj = scheduleObj,
            upsertTask = upsertTask
        )
        //attendance
        AttendanceComposable(onAttendanceClick, scheduleObj)
    }
}

