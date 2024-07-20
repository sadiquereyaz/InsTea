package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropDown(
    modifier: Modifier = Modifier,
    value: String = "value",
    options: List<String> = listOf("opt 1", "opt 2", "opt 3"),
    addButton: Boolean = true,
    label: String = "",
) {
    var expanded by remember { mutableStateOf(false) }
    var readOnly by remember { mutableStateOf(true) }
    var textFieldValue by remember {
        mutableStateOf(value)
    }

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = textFieldValue, // Initial value
                onValueChange = {
                    textFieldValue = it
                }, // No value change for this example
                /*supportingText = {
                    if (!readOnly) {
                        Text(text = "Enter Value")
                    } else if
                                   (textFieldValue.isBlank()) {
                        Text(
                            text = "There must be some value"
                        )
                    }
                },*/
                readOnly = readOnly,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                label = { Text(text = label) },
                )
            DropdownMenu(
                modifier = Modifier,
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            expanded = false
                            textFieldValue = option
                        },
                        text = { Text(text = option) }
                    )
                }
                if (addButton) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Add button"
                            )
                        },
                        text = { Text("Add New") },
                        onClick = {
                            expanded = !expanded
                            readOnly = false
                        }
                    )
                }
            }
        }
    }
}