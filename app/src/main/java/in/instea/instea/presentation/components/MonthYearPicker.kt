//package `in`.instea.instea.composable
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material3.Button
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import java.time.LocalDate
//import java.time.format.DateTimeFormatter
//
//@Composable
//fun MonthYearPicker() {
//    var expanded by remember { mutableStateOf(false) }
//    val currentDate = remember { LocalDate.now() }
//    var selectedDate by remember { mutableStateOf(currentDate) }
//
//    Column {
//        Row(
//            modifier = Modifier
//                .clickable { expanded = true }
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = selectedDate.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
//                modifier = Modifier.padding(end = 8.dp)
//            )
//            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Dropdown")
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                // Year selection
//                Column {
//                    Text("Year")
//                    Row {
//                        IconButton(onClick = {
//                            selectedDate = selectedDate.minusYears(1)
//                        }) {
//                            Text("-")
//                        }
//                        Text(selectedDate.year.toString())
//                        IconButton(onClick = {
//                            selectedDate = selectedDate.plusYears(1)
//                        }) {
//                            Text("+")
//                        }
//                    }
//                }
//
//                // Month selection
//                Column {
//                    Text("Month")
//                    Row {
//                        IconButton(onClick = {
//                            selectedDate = selectedDate.minusMonths(1)
//                        }) {
//                            Text("-")
//                        }
//                        Text(selectedDate.month.toString())
//                        IconButton(onClick = {
//                            selectedDate = selectedDate.plusMonths(1)
//                        }) {
//                            Text("+")
//                        }
//                    }
//                }
//            }
//
//            Button(
//                onClick = { expanded = false },
//                modifier = Modifier.align(Alignment.End).padding(end = 16.dp, bottom = 16.dp)
//            ) {
//                Text("OK")
//            }
//        }
//    }
//}