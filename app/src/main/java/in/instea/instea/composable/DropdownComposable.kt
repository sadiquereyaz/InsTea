package `in`.instea.instea.composable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DropdownComposable(
    modifier: Modifier = Modifier,
    label: String = "",
    options: List<String> = listOf("opt 1", "opt 2", "opt 3"),
    selectedOption: String = "",
    onOptionSelected: (String) -> Unit,
    leadingIcon: ImageVector = Icons.Default.Abc,
    isError: Boolean = false,
    errorMessage: String = "Error Message",
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onAddItemClicked: () -> Unit = {},
    isEnabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = expanded,
            onExpandedChange = {
                if (isEnabled) expanded = !expanded
            },
            ) {
            OutlinedTextField(
                modifier = modifier.menuAnchor(),
                readOnly = true,
                value = selectedOption,
                onValueChange = {},
                enabled = isEnabled,
                isError = isError,
                label = {
                    Text(
                        text = label,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                leadingIcon = { Icon(imageVector = leadingIcon, contentDescription = null) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(8.dp),
                supportingText = {
                    // Display error text if the input is not valid
                    if (isError) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardActions = keyboardActions,
            )
            DropdownMenu(
                modifier = Modifier,
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(text = option, overflow = TextOverflow.Ellipsis, maxLines = 1)
                        },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
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
                    onClick = { onAddItemClicked() }
                )
            }
        }
    }
}