package `in`.instea.instea.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay

@Composable
fun GuidelinesDialog() {
    var showDialog by remember { mutableStateOf(true) }
    var currentSlideIndex by remember { mutableStateOf(0) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    val guidelines = listOf(
        "Treat everyone kindly and avoid offensive language.",
        "Post only college-related and educational content.",
        "Don’t share personal information without permission.",
        "Only authorized personnel should update timetables.",
        "Report inappropriate content or behavior.",
        "Don’t post harmful or explicit content.",
        "Provide Feedback: Share constructive feedback and suggestions."
    )
    LaunchedEffect(currentSlideIndex) {
        isButtonEnabled=false
        delay(2000)
        isButtonEnabled=true
    }
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 8.dp,
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Community Guidelines",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )


                        Text(

                            text = guidelines[currentSlideIndex],
                            fontSize = 16.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = {
                            if (currentSlideIndex<guidelines.size-1) {
                                currentSlideIndex++
                            }else{
                                showDialog=false
                            }

                        },
                            enabled = isButtonEnabled) {
                            Text(if(currentSlideIndex<guidelines.size-1)"Next" else "Close")
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
private fun DialogPrev() {
    GuidelinesDialog()
}