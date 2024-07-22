import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.`in`.instea.instea.screens.more.composable.Developers
import `in`.`in`.instea.instea.screens.more.composable.allTask
import `in`.`in`.instea.instea.screens.more.composable.attendance
import `in`.`in`.instea.instea.screens.more.composable.classmates
import `in`.instea.instea.screens.more.composable.account
import `in`.instea.instea.screens.more.composable.report

@Composable
fun More(modifier: Modifier = Modifier) {
    val items =
        listOf("Developers", "All Task", "Account", "Attendance Record", "Classmates", "Report")
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(items.size) { index ->
            ExpandableItem(title = items[index],
                isExpanded = (index == expandedIndex),
                onExpandChange = { expandedIndex = if (expandedIndex == index) null else index })
        }
    }

}

@Composable
fun ExpandableItem(title: String, isExpanded: Boolean, onExpandChange: () -> Unit) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
//        elevation = CardElevation(2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onExpandChange() },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = title, fontSize = 18.sp)
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand"
                )
            }
            if (isExpanded) {
                // Add the content to display when expanded
                Column(
                    modifier = Modifier
                        .padding(6.dp)
                        .heightIn(max = 500.dp)
                ) {
                    when (title) {
                        "Developers" -> {
                            Developers()
                        }

                        "All Task" -> {
                            allTask()
                        }

                        "Account" -> {
                            account()
                        }

                        "Attendance Record" -> {
                            attendance()
                        }

                        "Classmates" -> {
                            classmates()
                        }

                        "Report" -> {
                            report()
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun morePrev() {
    More()
}