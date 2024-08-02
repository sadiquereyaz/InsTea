package `in`.instea.instea.navigation

import FEED
import FeedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.instea.instea.screens.AttendanceScreen
import `in`.instea.instea.screens.EditProfileScreen
import `in`.instea.instea.screens.InboxScreen
import `in`.instea.instea.screens.auth.AddInfo
import `in`.instea.instea.screens.auth.AuthenticationScreen
import `in`.instea.instea.screens.auth.UserInfoScreen
import `in`.instea.instea.screens.more.MoreDestination
import `in`.instea.instea.screens.more.MoreScreen
import `in`.instea.instea.screens.profile.ProfileDestination
import `in`.instea.instea.screens.profile.ProfileScreen
import `in`.instea.instea.screens.schedule.EditScheduleDestination
import `in`.instea.instea.screens.schedule.EditScheduleScreen
import `in`.instea.instea.screens.schedule.ScheduleScreen

@Composable
fun InsteaNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = InsteaScreens.Authenticate.name,
        modifier = Modifier
            .padding(contentPadding)
    ) {
        composable(route = InsteaScreens.UserInfo.name) {
            UserInfoScreen(
                navController = navController,
            )
        }
        composable(route = InsteaScreens.Authenticate.name) {
            AuthenticationScreen(
                openAndPopUp = { route, popUp ->
                     navController.navigate(route) {
//                    launchSingleTop = true
                    popUpTo(popUp) { inclusive = true }
                }
                }
            )
        }
        composable(route = InsteaScreens.Feed.name) {
            FEED(
                navigateToProfile = { userId -> navController.navigate("${ProfileDestination.route}/${userId}") }
            )
        }
        composable(route = InsteaScreens.Inbox.name) {
            InboxScreen(navController = navController)
        }
        composable(route = InsteaScreens.Schedule.name) {
            ScheduleScreen(
                navigateToEditSchedule = { id: Int, day: String ->
                    navController.navigate("${InsteaScreens.EditSchedule.name}/$id/$day")
                }
            )
        }
        composable(
            route = EditScheduleDestination.routeWithArg,
            arguments = listOf(
                navArgument(EditScheduleDestination.ID_ARG) { type = NavType.IntType },
                navArgument(EditScheduleDestination.DAY_ARG) { type = NavType.StringType }
            )
        ) {
            EditScheduleScreen(
                navController = navController
            )
        }
        composable(route = InsteaScreens.Attendance.name) {
            AttendanceScreen(navController = navController)
        }
        // self profile
        composable(route = InsteaScreens.SelfProfile.name) {
            ProfileScreen(
                onSubUsernameClick = {
                    navController.navigate(InsteaScreens.EditProfile.name)
                },
                navigateToDevelopers = {
                    navController.navigate("${MoreDestination.route}/${0}")
                }
            )
        }
        //OtherProfileScreen
        composable(
            route = ProfileDestination.routeWithArg,
            arguments = listOf(navArgument(ProfileDestination.USERID_ARG) {
                type = NavType.StringType
            })
        ) {
//            OtherProfileScreen()
            ProfileScreen(
                onSubUsernameClick = { navController.navigate(InsteaScreens.Inbox.name) },
                navigateToDevelopers = {
                    navController.navigate("${MoreDestination.route}/${0}")
                }
            )
        }
        composable(route = InsteaScreens.EditProfile.name) {
            EditProfileScreen(
                navigateToAddAcademics = { navController.navigate(InsteaScreens.AddAcademicInfo.name) },
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(route = InsteaScreens.Addpost.name) {
            FeedContent()
        }
        composable(route = InsteaScreens.AddAcademicInfo.name) {
            AddInfo(navController = navController)
        }
        composable(
            route = MoreDestination.routeWithArg,
            arguments = listOf(navArgument(MoreDestination.INDEX_ARG) {
                type = NavType.IntType
            })
        ) {
            MoreScreen(
                navController = navController
            )
        }
    }
}

interface NavigationDestinations {
    val route: String
    val title: String
}
