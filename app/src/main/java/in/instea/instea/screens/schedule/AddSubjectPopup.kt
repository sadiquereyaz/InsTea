package `in`.instea.instea.screens.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddSubjectPopup(
    showPopup: Boolean,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    textFieldCount: Int,
    onChange1: (String) -> Unit = {},
    onChange2: (String) -> Unit = {},
    onChange3: (String) -> Unit = {},
) {
    var subjectName by remember { mutableStateOf("") }

    if (showPopup) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Add New Subject",
                        style = MaterialTheme.typography.titleLarge,
                    )

                        OutlinedTextField(
                            value = subjectName,
                            onValueChange = { subjectName = it },
                            label = { Text("Subject Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                    if (textFieldCount > 1) {
                        OutlinedTextField(
                            value = subjectName,
                            onValueChange = { subjectName = it },
                            label = { Text("Subject Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (textFieldCount > 2) {
                        OutlinedTextField(
                            value = subjectName,
                            onValueChange = { subjectName = it },
                            label = { Text("Subject Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
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
                                onSave(subjectName)
                                onDismiss()
                            },
                            enabled = subjectName.isNotBlank(),
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}