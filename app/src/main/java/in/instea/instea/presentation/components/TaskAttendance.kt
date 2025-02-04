package `in`.instea.instea.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.domain.datamodel.AttendanceType
import `in`.instea.instea.domain.datamodel.CombinedScheduleTaskModel

@Composable
fun TaskAttendance(
    scheduleObj: CombinedScheduleTaskModel,
    onAttendanceClick: (AttendanceType) -> Unit,
    upsertTask: (String?, Int) -> Unit,

    ) {
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
            upsertTask = upsertTask,
            task = scheduleObj.task,
            remindBefore = scheduleObj.taskReminderBefore
        )
        //attendance
        AttendanceComposable(onAttendanceClick, scheduleObj)
    }
}

