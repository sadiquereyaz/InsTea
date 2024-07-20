package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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

@Composable
fun DropDown(
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var readOnly by remember { mutableStateOf(true) }
    val options = listOf("Option 1", "Option 2", "Option 3")
    var subject by remember {
        mutableStateOf("Eng")
    }

    Box {
        OutlinedTextField(
            value = subject, // Initial value
            onValueChange = {
                subject = it
            }, // No value change for this example
            readOnly = readOnly,
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown arrow"
                    )
                }
            },
            supportingText = {if (readOnly) Text(text = "Please select a option")},
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        readOnly = false
                    },
                    text = { Text(text = option) }

                )
            }
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