package `in`.instea.instea.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import `in`.instea.instea.navigation.InsteaScreens

@Composable
fun AddClassButton(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Row(
        modifier = modifier
            .padding(top = 32.dp)
            .clickable {
                navController.navigate(route = InsteaScreens.EditSchedule.name)
            },
//        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "add class",
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Add Class",
            color = MaterialTheme.colorScheme.primary,
//                 fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}