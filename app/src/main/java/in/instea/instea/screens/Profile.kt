package `in`.instea.instea.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Profile(
    userName: String,
    onEditIconClicked: () -> Unit,
    modifier: Modifier = Modifier,
    department: String,
    semester: String,
    hostel: String,
    instagram: String,
    linkedin: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 8.dp),
                text = userName,
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row {
            IconBox(
                modifier = Modifier,
                icon = Icons.Default.ThumbUp,
                text = "125"
            )
            IconBox(
                modifier = Modifier,
                icon = Icons.Default.Email,
                text = "Message"
            )
            IconBox(
                modifier = Modifier,
                icon = Icons.Default.DateRange,
                text = "65%"
            )
        }
//        Divider(
//            modifier = Modifier.padding(16.dp)
//        )
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(10))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(top = 16.dp, end = 16.dp)
        ) {
            UserDetails(
                icon = Icons.Outlined.Home,
                title = hostel,
                subtitle = "Room no. 209")
            UserDetails(
                icon = Icons.Outlined.AccountBox,
                title = instagram,
                subtitle = "Instagram"
            )
            UserDetails(
                icon = Icons.Outlined.AccountCircle,
                title = linkedin,
                subtitle = "LinkedIn"
            )
        }
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("All Posts", "Saved")
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                )
            }
        }
        when (selectedTabIndex) {
            0 -> {
                Text(
                    text = "All Posts",
                    fontSize = 32.sp,
                    modifier = Modifier.padding(top = 120.dp)
                )
            }

            1 -> {
                Text(text = "Saved", fontSize = 32.sp, modifier = Modifier.padding(top = 120.dp))
            }
        }
    }
}

@Composable
private fun UserDetails(
    icon: ImageVector,
    title: String,
    subtitle: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = icon,
                contentDescription = null
            )
            Column(
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    modifier = Modifier,
                    text = subtitle,
//                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

        }
    }
}

@Composable
private fun IconBox(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 100.dp, minHeight = 32.dp)
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(20)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = icon,
                contentDescription = text
            )
            Text(text = text)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfilePreview() {
//    Profile(navController = rememberNavController())
}