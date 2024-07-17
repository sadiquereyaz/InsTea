package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.composable.DropdownMenuBox
import `in`.instea.instea.data.viewmodel.ScheduleViewModel
import `in`.instea.instea.screens.auth.ButtonComp

@Composable
fun EditScheduleScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(28.dp)
            .fillMaxWidth()
    ) {

        val subjects = listOf("ADC", "DSA","Computer Organization")
        val startTime= listOf("9:00","10:00","11:00","12:00","1:00")
        val durations= listOf("50 Minutes","60 Minutes")
        val days= listOf("Monday","Tuesday","Wednesday","Thursday","Friday")
        var subject = remember {
            mutableStateOf("")
        }
        var time= remember {
            mutableStateOf("")
        }
        var duration= remember {
            mutableStateOf("")
        }
        var day= remember {
            mutableStateOf("")
        }
        Text(text = "Edit Schedule",
            modifier = Modifier
                .fillMaxWidth(),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(20.dp))
        DropdownMenuBox(
            label = "SUBJECT",
            options = subjects,
            selectedOption =subject.value,
            onOptionSelected = { subject.value = it },
            modifier =Modifier
        )
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenuBox(
            label = "Start Time",
            options = startTime,
            selectedOption =time.value,
            onOptionSelected = { time.value = it },
            modifier =Modifier
        )
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenuBox(
                label = "Duration",
        options = durations,
        selectedOption =duration.value,
        onOptionSelected = { duration.value = it },
        modifier =Modifier
        )
        Spacer(modifier = Modifier.height(10.dp))
        DropdownMenuBox(
            label = "Day",
            options = days,
            selectedOption =day.value,
            onOptionSelected = { day.value = it },
            modifier =Modifier
        )
        Spacer(modifier = Modifier.weight(1f))
        Row (horizontalArrangement = Arrangement.Center){
            ButtonComp(modifier=Modifier.fillMaxWidth(0.5f),value="Save", onButtonClicked = {})
        }

    }
}



@Preview(showBackground = true)
@Composable
private fun EditschedulePreview() {
    val navController = rememberNavController()
    EditScheduleScreen(modifier = Modifier, navController = navController)
}