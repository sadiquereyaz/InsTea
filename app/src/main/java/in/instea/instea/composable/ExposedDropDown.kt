package `in`.instea.instea.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.datamodel.SubjectModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropDown(
    modifier: Modifier = Modifier,
    value: String = "value",
    options: List<Any> = listOf("opt 1", "opt 2", "opt 3"),
    addButton: Boolean = true,
    label: String = "",
    onOptionSelect: (Any) -> Unit,
    errorMessage: String? = null,
//    readOnly: Boolean = true,
    onAddClick: () -> Unit = {},
    isError: Boolean = !errorMessage.isNullOrBlank(),
) {
    var expanded by remember { mutableStateOf(false) }
//    var readOnly by remember { mutableStateOf(true) }

    /*  LaunchedEffect(readOnly) {
          Log.d("EXPOSED_DD_LE", "readonly changed uistate: $readOnly")
          if (!readOnly) {
              editable = true
          }
      }*/

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = value, // Initial value
                onValueChange = {
                    onOptionSelect(it)
                },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text(text = "Enter Value", color = MaterialTheme.colorScheme.error)
                    }
                },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                label = { Text(text = label) },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = false,
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text
                )
            )
            DropdownMenu(
                modifier = Modifier,
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    if (option is String) {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
//                            value = option
                                onOptionSelect(option)
                            },
                            text = { Text(text = option) }
                        )
                    } else if (option is SubjectModel) {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
//                            value = option
                                onOptionSelect(option)
                            },
                            text = { Text(text = option.subject) }
                        )
                    }
                }
                if (addButton) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "Add Manually"
                            )
                        },
                        text = { Text("Add Manually") },
                        onClick = {
                            expanded = !expanded
//                            readOnly = false
                            onAddClick()
                        }
                    )
                }
            }
        }
    }
}