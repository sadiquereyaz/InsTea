package `in`.instea.instea.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.presentation.components.BottomNavigationBar
import `in`.instea.instea.presentation.components.InsteaTopAppBar
import `in`.instea.instea.presentation.more.MoreDestination
import `in`.instea.instea.presentation.navigation.InsteaNavHost
import `in`.instea.instea.presentation.navigation.InsteaScreens
import `in`.instea.instea.presentation.profile.BottomNavItemData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsteaApp(
    navController: NavHostController = rememberNavController(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val selectedItemIndex = rememberSaveable {
        mutableIntStateOf(0);
    }

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = InsteaScreens.valueOf(
        backStackEntry?.destination?.route?.substringBefore("/") ?: InsteaScreens.Feed.name
    )
    LaunchedEffect(currentScreen) {
        selectedItemIndex.intValue =
            BottomNavItemData.bottomNavItems.indexOfFirst { it.route == currentScreen.name }
    }
    val bottomBarItems = listOf(
        InsteaScreens.Feed,
        InsteaScreens.Schedule,
        InsteaScreens.UserList, //inbox
        InsteaScreens.Notice, //inbox
        InsteaScreens.SelfProfile
    )
    val snackBarHostState = remember { SnackbarHostState() }
    Scaffold(
//        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        modifier = Modifier,
        topBar = {
            InsteaTopAppBar(
                scrollBehavior = scrollBehavior,
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null && !bottomBarItems.contains(
                    currentScreen
                ),
                navigateBack = { navController.navigateUp() },
                moveToSelfProfile = { navController.navigate(InsteaScreens.SelfProfile.name) },
                moveToOtherProfile = { navController.navigate(InsteaScreens.OtherProfile.name) },
                moveToAttendanceSummary = { index -> navController.navigate("${MoreDestination.route}/${index}") },
                navController = navController,
                onAddButtonClicked = { navController.navigate(InsteaScreens.Addpost.name) }
            )
        },
        bottomBar = {
            if (bottomBarItems.contains(currentScreen)) {
                BottomNavigationBar(selectedItemIndex, navController)
            }
        },
        snackbarHost ={ SnackbarHost(hostState = snackBarHostState)}
    ) { contentPadding ->
        InsteaNavHost(navController, contentPadding, snackBarHostState = snackBarHostState)
    }
}

@Preview
@Composable
fun InsteaAppPreview() {
    InsteaApp()
}
