import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.composable.BottomNavigationBar
import `in`.instea.instea.composable.InsteaTopAppBar
import `in`.instea.instea.data.BottomNavItemData
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.data.viewmodel.ScheduleViewModel
import `in`.instea.instea.navigation.InsteaNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsteaApp(
//    profileViewModel: ProfileViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
//    val profileUiState by profileViewModel.uiState.collectAsState()
    val selectedItemIndex = rememberSaveable {
        mutableIntStateOf(0);
    }

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = InsteaScreens.valueOf(
        backStackEntry?.destination?.route ?: InsteaScreens.Feed.name
    )
    LaunchedEffect(currentScreen) {
        selectedItemIndex.value =
            BottomNavItemData.bottomNavItems.indexOfFirst { it.route == currentScreen.name }
    }
    val bottomBarItems = listOf(
        InsteaScreens.Feed,
        InsteaScreens.Schedule,
        InsteaScreens.Inbox, //inbox
        InsteaScreens.SelfProfile
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                navController = navController
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
