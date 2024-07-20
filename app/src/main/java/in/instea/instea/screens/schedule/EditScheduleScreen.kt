package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.composable.ExposedDropDown
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.EditScheduleViewModel
import `in`.instea.instea.screens.auth.composable.ButtonComp
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@Composable
fun EditScheduleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: EditScheduleViewModel = viewModel(factory = AppViewModelProvider.Factory),
    editScreenType: EditScreenType = EditScreenType.EditScreen,
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutine = rememberCoroutineScope()
    Box(
        modifier = modifier
            .padding(24.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // subject
            ExposedDropDown(
                options = uiState.subjectList,
                value = uiState.selectedSubject,
                label = "Subject",
                onOptionSelect = {
                    viewModel.onSubjectSelected(it)
                }
            )
            // day
            ExposedDropDown(
                options = uiState.dayList,
                value = uiState.selectedDay,
                addButton = false,
                label = "Day",
                onOptionSelect = {
                    viewModel.onDaySelected(it)
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // start time
                TimePicker(
                    modifier = Modifier.weight(1f),
                    value = uiState.startTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    label = "Start Time",
                    onTimeSelect = { localTime ->
                        viewModel.setStartTime(localTime)
                        viewModel.setEndTime(localTime.plusHours(1))
                    }
                )
                // end time
                TimePicker(
                    modifier = Modifier.weight(1f),
                    value = uiState.endTime.format(DateTimeFormatter.ofPattern("hh:mm a")),
                    label = "End Time",
                    onTimeSelect = { localTime ->
                        viewModel.setEndTime(localTime)
                    }
                )
            }
        }
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),

            ) {
            if (editScreenType.isEditable) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ },
//                    colors = ButtonDefaults.outlinedButtonColors(),
                    border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "Delete", color = MaterialTheme.colorScheme.error)
                }
                ButtonComp(
                    modifier = Modifier.weight(1f),
                    text = editScreenType.positiveButtonText,
                    onButtonClicked = {
                        coroutine.launch {
                            viewModel.saveSchedule()
                        }
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditschedulePreview() {
    val navController = rememberNavController()
    EditScheduleScreen(modifier = Modifier, navController = navController)
}

