package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddAcademicPopup(
    showPopup: Boolean,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit,
    uniName: String
) {
    var uni by remember { mutableStateOf(uniName) }
    var department by remember { mutableStateOf("") }
    LaunchedEffect(key1 = uniName) {
        uni = uniName
    }

    if (showPopup) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .padding(36.dp, 24.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        text = "Add New Institute",
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        text = "Please add correct spelled names.",
//                        style = MaterialTheme.typography.labelSmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Column () {
                        OutlinedTextField(
                            value = uni,
                            onValueChange = { /*if (it.length <= 30)*/ uni = it },
                            label = { Text("University Name") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 0.dp),
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)

                        )
                        if (uni.length > 30) Text(
                            modifier = Modifier.padding(top = 6.dp, start = 8.dp),
                            text = "Maximum 30 characters allowed",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                    Column () {
                        OutlinedTextField(
                            value = department,
                            onValueChange = { /*if (it.length <= 30)*/ department = it },
                            label = { Text("Department Name") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 0.dp),
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                        )
                        if (department.length > 30) Text(
                            modifier = Modifier.padding(top = 6.dp, start = 8.dp),
                            text = "Maximum 30 characters allowed",
                            color = MaterialTheme.colorScheme.error,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            modifier = Modifier,
                            onClick = {
                                onSave(uni, department)
                                onDismiss()
                            },
                            enabled = uni.isNotBlank() && department.isNotBlank()
                                    && uni.length <= 30 && department.length <= 30,
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddAcademicPopupPreview() {
    var showPopup by remember { mutableStateOf(true) }
    AddAcademicPopup(
        showPopup = showPopup,
        onDismiss = { showPopup = false },
        onSave = { uni, dept -> /* Handle save action here */ },
        uniName = "Test University"
    )
}