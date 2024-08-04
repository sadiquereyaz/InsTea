package `in`.instea.instea.navigation

import EditPost
import FEED
import FeedContent
import InboxScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.instea.instea.screens.AttendanceScreen
import `in`.instea.instea.screens.Feed.UserListScreen
import `in`.instea.instea.screens.auth.AddInfo
import `in`.instea.instea.screens.auth.AuthenticationScreen
import `in`.instea.instea.screens.auth.UserInfoScreen
import `in`.instea.instea.screens.more.MoreDestination
import `in`.instea.instea.screens.more.MoreScreen
import `in`.instea.instea.screens.profile.EditProfileScreen
import `in`.instea.instea.screens.profile.ProfileDestination
import `in`.instea.instea.screens.profile.ProfileScreen
import `in`.instea.instea.screens.schedule.EditScheduleDestination
import `in`.instea.instea.screens.schedule.EditScheduleScreen
import `in`.instea.instea.screens.schedule.ScheduleScreen


@Composable
fun InsteaNavHost(
    navController: NavHostController,
    contentPadding: PaddingValues,
    snackBarHostState: SnackbarHostState,
//    scheduleViewModel: ScheduleViewModel
) {
    NavHost(
        navController = navController,
        startDestination = InsteaScreens.Authenticate.name  /*"${MoreDestination.route}/${4}"*/,
        modifier = Modifier
            .padding(contentPadding)
    ) {
        composable(route = InsteaScreens.Authenticate.name) {
            AuthenticationScreen(
                navigateToFeed = {
                    navController.navigate(InsteaScreens.SelfProfile.name/*"${MoreDestination.route}/${4}"*/) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigateToUserInfo = {
                    navController.navigate(InsteaScreens.UserInfo.name)
                }
            )
        }
        composable(route = InsteaScreens.UserInfo.name) {
            UserInfoScreen(
                openAndPopUp = { route ->
                    navController.navigate(route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onAddClick = {
                    navController.navigate(InsteaScreens.AddAcademicInfo.name)
                }
            )
        }
        composable(route = InsteaScreens.Feed.name) {
            FEED(
                navigateToProfile = { userId -> navController.navigate("${ProfileDestination.route}/${userId}") },
                navController = navController
            )
        }

        composable(route = InsteaScreens.Inbox.name + "/{userId}") { backStackEntry ->
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
        // self profile
        composable(route = InsteaScreens.SelfProfile.name) {
            ProfileScreen(
                onSubUsernameClick = {
                    navController.navigate(InsteaScreens.EditProfile.name)
                },
                navigateToDevelopers = {
                    navController.navigate("${MoreDestination.route}/${0}")
                },
                snackBarHostState = snackBarHostState
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
                },
                snackBarHostState = snackBarHostState
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
                navController = navController,
                snackBarHostState = snackBarHostState
            )
        }
        composable(route = InsteaScreens.EditPost.name + "/{postId}") { backstackEntry ->
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
