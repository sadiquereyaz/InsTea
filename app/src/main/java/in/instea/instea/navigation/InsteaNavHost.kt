package `in`.instea.instea.navigation

import FEED
import FeedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import `in`.instea.instea.screens.AttendanceScreen
import `in`.instea.instea.screens.EditProfile
import `in`.instea.instea.screens.InboxScreen
import `in`.instea.instea.screens.auth.SignInScreen
import `in`.instea.instea.screens.auth.SignUpScreen
import `in`.instea.instea.screens.profile.OtherProfileScreen
import `in`.instea.instea.screens.profile.SelfProfileScreen
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
                navController=navController
            )
        }
        composable(route = InsteaScreens.SignIn.name) {
            SignInScreen(navController = navController)
        }
        composable(route = InsteaScreens.Feed.name) {
            FEED(
                navController = navController
//                            navigateToOtherProfile = { navController.navigate("${InsteaScreens.OtherProfile.name}/${it}") }
            )
        }
        composable(route = InsteaScreens.Inbox.name) {
            InboxScreen(navController = navController)
        }
        composable(route = InsteaScreens.Schedule.name) {
            ScheduleScreen(navController = navController)
        }
        composable(route = InsteaScreens.EditSchedule.name) {
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
        composable(route = InsteaScreens.Addpost.name){
            FeedContent()
        }
    }
}