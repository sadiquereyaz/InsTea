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
import `in`.instea.instea.screens.EditProfile
import `in`.instea.instea.screens.InboxScreen
import `in`.instea.instea.screens.auth.SignInScreen
import `in`.instea.instea.screens.auth.SignUpScreen
import `in`.instea.instea.screens.profile.OtherProfileScreen
import `in`.instea.instea.screens.profile.SelfProfileScreen
import `in`.instea.instea.screens.schedule.EditScheduleDestination
import `in`.instea.instea.screens.schedule.EditScheduleScreen
import `in`.instea.instea.screens.schedule.ScheduleScreen


@Composable
fun InsteaNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
//    scheduleViewModel: ScheduleViewModel
) {
    NavHost(
        navController = navController,
        startDestination = InsteaScreens.Schedule.name,
        modifier = Modifier
            .padding(contentPadding)
    ) {
        composable(route = InsteaScreens.Signup.name) {
            SignUpScreen(
                navController = navController
            )
        }
        composable(route = InsteaScreens.SignIn.name) {
            SignInScreen(navController = navController)
        }
        composable(route = InsteaScreens.Feed.name) {
            FEED(
                navController = navController
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
        composable(route = InsteaScreens.SelfProfile.name) {
            SelfProfileScreen()
        }
        composable(route = InsteaScreens.OtherProfile.name) {
            OtherProfileScreen()
        }
        composable(route = InsteaScreens.EditProfile.name) {
            EditProfile(

            )
        }
        composable(route = InsteaScreens.Addpost.name) {
            FeedContent()
        }
    }
}

interface NavigationDestinations {
    val route: String
    val title: String
}
