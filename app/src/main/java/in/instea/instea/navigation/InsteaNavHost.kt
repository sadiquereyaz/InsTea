package `in`.instea.instea.navigation

import EditPost
import FEED
import FeedContent
import InboxScreen
import UserListScreen
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

import `in`.instea.instea.screens.auth.AddInfo
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
        startDestination = InsteaScreens.Signup.name,
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
//                            navigateToOtherProfile = { navController.navigate("${InsteaScreens.OtherProfile.name}/${it}") }
            )
        }

        composable(route = InsteaScreens.Inbox.name+"/{userId}") {backStackEntry->
            val userId = backStackEntry.arguments?.getString("userId")

           InboxScreen(userId!!)
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
            SelfProfileScreen(
                navigateToEditProfile = { navController.navigate(InsteaScreens.EditProfile.name) }
            )
        }
        composable(route = InsteaScreens.OtherProfile.name) {
            OtherProfileScreen()
        }
        composable(route = InsteaScreens.EditProfile.name) {
            EditProfileScreen(
                navigateToAddAcademics = { navController.navigate(InsteaScreens.AddAcademicInfo.name) },
                navigateBack={navController.popBackStack()}
                )
        }
        composable(route = InsteaScreens.Addpost.name) {
            FeedContent()
        }
        composable(route = InsteaScreens.AddAcademicInfo.name) {
            AddInfo(navController = navController)
        }
        composable(route = InsteaScreens.EditPost.name+"/{postId}"){ backstackEntry->
            val post = backstackEntry.arguments?.getString("postId")
            EditPost(post!!)
        }
        composable(route = InsteaScreens.UserList.name) {
          UserListScreen()
        }

    }
}

interface NavigationDestinations {
    val route: String
    val title: String
}
