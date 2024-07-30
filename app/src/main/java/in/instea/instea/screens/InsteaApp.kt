import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.composable.BottomNavigationBar
import `in`.instea.instea.composable.InsteaTopAppBar
//import `in`.instea.instea.composable.InsteaTopAppBar
import `in`.instea.instea.data.BottomNavItemData
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.navigation.InsteaNavHost

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
        InsteaScreens.SelfProfile
    )

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
                navController = navController,
                onAddButtonClicked = {
                 navController.navigate(InsteaScreens.Addpost.name)
                }
            )
        },
        bottomBar = {
            if (bottomBarItems.contains(currentScreen)) {
                BottomNavigationBar(selectedItemIndex, navController)
            }
        }
    ) { contentPadding ->
        InsteaNavHost(navController, contentPadding)
    }
}

@Preview
@Composable
fun InsteaAppPreview() {
    InsteaApp()
}
