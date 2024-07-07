import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.composable.BottomNavigationBar
import `in`.instea.instea.composable.InsteaTopAppBar
import `in`.instea.instea.data.AuthViewModel
import `in`.instea.instea.data.BottomNavItemData
import `in`.instea.instea.data.FeedViewModel
import `in`.instea.instea.data.ProfileViewModel
import `in`.instea.instea.model.InsteaScreens
import `in`.instea.instea.screens.AttendanceScreen
import `in`.instea.instea.screens.EditProfile
import `in`.instea.instea.screens.Login


//import `in`.instea.instea.screens.Feed
import `in`.instea.instea.screens.Notificaiton
import `in`.instea.instea.screens.Profile
import `in`.instea.instea.screens.ScheduleScreen
import `in`.instea.instea.screens.Signup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsteaApp(
    viewModel: ProfileViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val uiState by viewModel.uiState.collectAsState()
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
        InsteaScreens.Notification,
        InsteaScreens.Schedule
    )

    Scaffold(
        topBar = {
            InsteaTopAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null && !bottomBarItems.contains(
                    currentScreen
                ),
                navigateBack = { navController.navigateUp() },
                moveToProfile = { navController.navigate(InsteaScreens.Profile.name) },
                navController = navController
            )
        },
        bottomBar = {
            if (bottomBarItems.contains(currentScreen)) {
                BottomNavigationBar(selectedItemIndex, navController)
            }
        }
    ) { contentPadding ->
        NavHost(
            navController = navController,
            startDestination = InsteaScreens.Signup.name,
            modifier = Modifier
                .padding(contentPadding)
        ) {
            composable(route = InsteaScreens.Signup.name) {
                Signup(
                    viewModel = AuthViewModel(),
                    feedViewmodel = FeedViewModel(),
                    navController
                )
            }
            composable(route = InsteaScreens.Login.name) {
                Login(viewModel = AuthViewModel())
            }
            composable(route = InsteaScreens.Feed.name) {
                FEED(navController = navController, feedViewModel = FeedViewModel())
            }
            composable(route = InsteaScreens.Notification.name) {
                Notificaiton(navController = navController)
            }
            composable(route = InsteaScreens.Schedule.name) {
                ScheduleScreen(navController = navController)
            }
            composable(route = InsteaScreens.Attendance.name) {
                AttendanceScreen(navController = navController)
            }
            composable(route = InsteaScreens.Profile.name) {
                Profile(
                    userName = uiState.userName,
                    onEditIconClicked = {
                        navController.navigate(InsteaScreens.EditProfile.name)
                    },
                    department = uiState.selectedDepartment,
                    semester = uiState.selectedSemester,
                    hostel = uiState.selectedHostel,
                    instagram = uiState.instagram,
                    linkedin = uiState.linkedin
                )
            }
            composable(route = InsteaScreens.EditProfile.name) {
                EditProfile(
                    userName = uiState.userName,
                    onUserNameChanged = { viewModel.onUserNameChanged(it) },
                    onDepartmentChanged = { viewModel.onDepartmentChange(it) },
                    onSemesterChanged = { viewModel.onSemesterChange(it) },
                    onSaveButtonClicked = {},
                    onCancelButtonClicked = {},
                    selectedDepartment = uiState.selectedDepartment,
                    selectedSemester = uiState.selectedSemester,
                    selectedHostel = uiState.selectedHostel,
                    instagram = uiState.instagram,
                    linkedin = uiState.linkedin
                )
            }


        }
    }
}

@Preview
@Composable
fun InsteaAppPreview() {
    InsteaApp()
}
