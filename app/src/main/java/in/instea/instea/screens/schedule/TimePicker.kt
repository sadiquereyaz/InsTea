package `in`.instea.instea.screens.schedule

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.viewmodel.EditScheduleViewModel
import java.time.LocalTime

@Composable
fun TimePicker(
    modifier : Modifier,
    value: String = "",
    label: String = "",
    onTimeSelect: (LocalTime) -> Unit = {}
) {
    var selectedTimeOnPicker by remember { mutableStateOf(LocalTime.now()) }    // state of time picker
//            val startTime = selectedTimeOnPicker.format(DateTimeFormatter.ofPattern("HH:mm a"))
    val context = LocalContext.current
    OutlinedTextField(
        modifier = modifier,
        value = value, // Initial value
        onValueChange = {},
        readOnly = true,
        label = { Text(text = label) },
        trailingIcon = {
            IconButton(onClick = {
                val timePickerDialog = TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val time = LocalTime.of(hour, minute)
                        selectedTimeOnPicker = time
                        onTimeSelect(time)
                    },
                    selectedTimeOnPicker.hour,
                    selectedTimeOnPicker.minute,
                    false // Is 24 hour view
                )
                timePickerDialog.show()
            }) {
                Icon(Icons.Filled.AccessTime, contentDescription = "Select Time")
            }
        },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
    )
}