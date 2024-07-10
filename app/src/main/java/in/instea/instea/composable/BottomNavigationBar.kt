package `in`.instea.instea.composable

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.data.BottomNavItemData
import `in`.instea.instea.model.InsteaScreens

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomNavigationBar(
    selectedItemIndex: MutableState<Int>,
    navController: NavHostController,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    NavigationBar {
        BottomNavItemData.bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex.value == index,
                onClick = {

                    Log.d("backstack", "BottomNavigationBar: ${backStackEntry?.destination?.route}")
                    selectedItemIndex.value = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
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
@Composable
fun T(str:String){
    Toast.makeText(LocalContext.current, "", Toast.LENGTH_SHORT).show()

}