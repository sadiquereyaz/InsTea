package `in`.instea.instea.presentation.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import `in`.instea.instea.presentation.navigation.InsteaScreens

data class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedItem: ImageVector,
    val hasNotification: Boolean,
    val badgeCount: Int? = null,
    val route: String
)

object BottomNavItemData{
    val bottomNavItems = listOf(
        BottomNavItem(
            title = "feed",
            selectedIcon = Icons.Filled.Home,
            unselectedItem = Icons.Outlined.Home,
            hasNotification = false,
            route = InsteaScreens.Feed.name
        ),
        BottomNavItem(
            title = "Schedule",
            selectedIcon = Icons.Filled.DateRange,
            unselectedItem = Icons.Outlined.DateRange,
            hasNotification = true,
            route = InsteaScreens.Schedule.name
        ),
        BottomNavItem(
            title = "Inbox",
            selectedIcon = Icons.Filled.Email,
            unselectedItem = Icons.Outlined.Email,
            hasNotification = true,
            badgeCount = 45,
            route = InsteaScreens.UserList.name
        ),
        BottomNavItem(
            title = "Notices",
            selectedIcon = Icons.Filled.NotificationsActive,
            unselectedItem = Icons.Outlined.Notifications,
            hasNotification = true,
//            badgeCount = 45,
            route = InsteaScreens.Notice.name
        ),
        BottomNavItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Person,
            unselectedItem = Icons.Outlined.Person,
            hasNotification = false,
            route = InsteaScreens.SelfProfile.name
        )
    )
}