package `in`.instea.instea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.screens.Feed
import `in`.instea.instea.screens.Notificaiton
import `in`.instea.instea.screens.Profile
import `in`.instea.instea.screens.SCREENS
import `in`.instea.instea.ui.theme.InsTeaTheme
//import in.instea.instea.screens.Feed
//import in.instea.instea.screens.Notificaiton
//import in.instea.instea.screens.Profile
//import in.instea.instea.screens.SCREENS
//import in.instea.instea.ui.theme.InsTeaTheme

data class BottomNavItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unselectedItem: ImageVector,
    val hasNotification:Boolean,
    val badgeCount:Int?=null,
    val ROUTE:String
)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InsTeaTheme {


                BottomNavigation()
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BottomNavigation(){
        val navController= rememberNavController()
        val items = listOf(
            BottomNavItem(
                title= "feed",
                selectedIcon = Icons.Filled.Home,
                unselectedItem = Icons.Outlined.Home,
                hasNotification = false,
                ROUTE = SCREENS.FEED


            ),
            BottomNavItem(
                title= "profile",
                selectedIcon = Icons.Filled.Person,
                unselectedItem = Icons.Outlined.Person,
                hasNotification = true,
                ROUTE = SCREENS.PROFILE


            ),
            BottomNavItem(
                title= "Notification",
                selectedIcon = Icons.Filled.Notifications,
                unselectedItem = Icons.Outlined.Notifications,
                hasNotification = true,
                badgeCount = 45,
                ROUTE = SCREENS.NOTIFICATIONS

            )
        )
        var selectedItemIndex = rememberSaveable {
            mutableStateOf(0);
        }


        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed{ index, item->
                        NavigationBarItem(
                            selected = selectedItemIndex.value == index,
                            onClick = {
                                selectedItemIndex.value = index
                                navController.navigate(item.ROUTE)

                            },
                            label = {
                                Text(text = item.title)
                            },
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if(item.badgeCount != null){
                                            Text(text = item.badgeCount.toString(),
                                                fontSize = 12.sp,

                                                modifier = Modifier

                                                    .padding(1.dp)
                                                    .size(18.dp))

                                        }else if(item.hasNotification){
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector =
                                        if (index == selectedItemIndex.value) {
                                            item.selectedIcon
                                        }else item.unselectedItem
                                        , contentDescription = item.title
                                    )
                                }
                            }
                        )

                    }
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = SCREENS.FEED,
                modifier = Modifier
                    .padding(it))
            {
                composable(route= SCREENS.FEED){
                    Feed(navController = navController)
                }

                composable(route= SCREENS.PROFILE){
                    Profile(navController = navController)

                }
                composable(route= SCREENS.NOTIFICATIONS){
                    Notificaiton(navController = navController)
                }

            }

        }

    }

}