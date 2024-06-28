package `in`.instea.instea.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import `in`.instea.instea.data.BottomNavItemData

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomNavigationBar(
    selectedItemIndex: MutableState<Int>,
    navController: NavHostController,
) {
    NavigationBar {
        BottomNavItemData.bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex.value == index,
                onClick = {
                    selectedItemIndex.value = index
                    navController.navigate(item.route)
                },
                label = {
                    Text(text = item.title)
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Text(
                                    text = item.badgeCount.toString(),
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .padding(1.dp)
                                        .size(18.dp)
                                )
                            } else if (item.hasNotification) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector =
                            if (index == selectedItemIndex.value) {
                                item.selectedIcon
                            } else item.unselectedItem, contentDescription = item.title
                        )
                    }
                }
            )
        }
    }
}