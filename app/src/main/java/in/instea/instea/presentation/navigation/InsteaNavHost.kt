package `in`.instea.instea.presentation.navigation

import android.annotation.SuppressLint
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import `in`.instea.instea.presentation.AttendanceScreen
import `in`.instea.instea.presentation.feed.UserListScreen
import `in`.instea.instea.presentation.feed.search.SearchScreen
import `in`.instea.instea.presentation.auth.UserInfoScreen
import `in`.instea.instea.presentation.auth.composable.signUp
import `in`.instea.instea.presentation.feed.EditPost
import `in`.instea.instea.presentation.feed.FeedScreen
import `in`.instea.instea.presentation.feed.FeedContent
import `in`.instea.instea.presentation.inbox.InboxScreen
import `in`.instea.instea.presentation.more.MoreDestination
import `in`.instea.instea.presentation.profile.ProfileDestination
import `in`.instea.instea.presentation.profile.ProfileScreen
import `in`.instea.instea.presentation.schedule.EditScheduleDestination
import `in`.instea.instea.presentation.schedule.EditScheduleScreen
import `in`.instea.instea.presentation.schedule.ScheduleScreen

import `in`.instea.instea.presentation.more.MoreScreen
import `in`.instea.instea.presentation.notice.NoticeScreen

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
            /*AuthenticationScreen(
                navigateToFeed = {
                    navController.navigate(InsteaScreens.Feed.name*//*"${MoreDestination.route}/${4}"*//*) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                navigateToUserInfo = {
                    navController.navigate(InsteaScreens.UserInfo.name)
                },
                snackBarHostState = snackBarHostState
            )*/
//            RestrictScreenshot { PdfViewerScreenFromUrlDirect(pdfDriveUrl = "https://drive.google.com/file/d/191hWNBOwJtVEQMe7liOiuKzJEj8hQAPK/view?usp=drivesdk") }
            OpenWebView(url = "https://drive.google.com/file/d/1--ZtGuyamagUiXGMbZ9DYoR0XLcjp5IQ/view?usp=drivesdk")
//            OpenWebView(url = "https://www.google.com")
        }
        composable(route = InsteaScreens.UserInfo.name) {
            UserInfoScreen(
                openAndPopUp = {
                    navController.navigate(InsteaScreens.Feed.name) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                snackBarHostState = snackBarHostState,
            )
        }
        composable(route = InsteaScreens.Feed.name) {
            FeedScreen(
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
                moveBackAndRefresh = {
                    navController.navigate(InsteaScreens.Schedule.name) {
                        popUpTo(InsteaScreens.Schedule.name) { inclusive = true }
                    }
                },
                snackBarHostState = snackBarHostState
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
                onSubUsernameClick = { uid -> navController.navigate(InsteaScreens.Inbox.name + "/$uid") },
                navigateToDevelopers = {
                    navController.navigate("${MoreDestination.route}/${0}")
                },
                snackBarHostState = snackBarHostState
            )
        }
        composable(route = InsteaScreens.EditProfile.name) {
            /*EditProfileScreen(
                navigateToAddAcademics = { navController.navigate(InsteaScreens.AddAcademicInfo.name) },
                navigateBack = { navController.popBackStack() }
            )*/
            UserInfoScreen(
                openAndPopUp = {
                    navController.navigateUp()
                },
                snackBarHostState = snackBarHostState
            )
        }
        composable(route = InsteaScreens.Addpost.name) {
            FeedContent()
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
            UserListScreen(navController = navController)
        }

        composable(route = InsteaScreens.signup.name) {
            signUp(navController = navController)
        }
        composable(route = InsteaScreens.Search.name) {
            SearchScreen(navController = navController)
        }
        composable(route = InsteaScreens.Notice.name) {
            NoticeScreen(navController = navController)
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun OpenWebView(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}


interface NavigationDestinations {
    val route: String
    val title: String

}

