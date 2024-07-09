import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import `in`.instea.instea.composable.BottomNavigationBar
import `in`.instea.instea.composable.InsteaTopAppBar
import `in`.instea.instea.data.BottomNavItemData
import `in`.instea.instea.model.profile.ProfileViewModel
import `in`.instea.instea.model.InsteaScreens
import `in`.instea.instea.model.schedule.ScheduleViewModel
import `in`.instea.instea.screens.AttendanceScreen
import `in`.instea.instea.screens.profile.EditProfileScreen
import `in`.instea.instea.screens.Notificaiton
import `in`.instea.instea.screens.profile.ProfileScreen
import `in`.instea.instea.screens.schedule.EditScheduleScreen
import `in`.instea.instea.screens.schedule.ScheduleScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsteaApp(
    scheduleViewModel: ScheduleViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val profileUiState by profileViewModel.uiState.collectAsState()
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
        selectedItemIndex.value = BottomNavItemData.bottomNavItems.indexOfFirst { it.route == currentScreen.name }
    }
    val bottomBarItems = listOf(
        InsteaScreens.Feed,
        InsteaScreens.Schedule,
        InsteaScreens.Notification,
        InsteaScreens.Schedule
    )

    Scaffold(
        modifier  = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            InsteaTopAppBar(
                scrollBehavior = scrollBehavior,
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
            startDestination = InsteaScreens.Schedule.name,
            modifier = Modifier
                .padding(contentPadding)
        ) {
            composable(route = InsteaScreens.Feed.name) {
               FEED(navController = navController)
            }
            composable(route = InsteaScreens.Notification.name) {
                Notificaiton(navController = navController)
            }
            composable(route = InsteaScreens.Schedule.name) {
                ScheduleScreen(navController = navController, scheduleViewModel = scheduleViewModel)
            }
            composable(route = InsteaScreens.EditSchedule.name) {
                EditScheduleScreen(navController = navController, scheduleViewModel = scheduleViewModel)
            }
            composable(route = InsteaScreens.Attendance.name) {
                AttendanceScreen(navController = navController)
            }
            composable(route = InsteaScreens.Profile.name) {
                ProfileScreen(
                    userName = profileUiState.userName,
                    onEditIconClicked = {
                        navController.navigate(InsteaScreens.EditProfile.name)
                    },
                    department = profileUiState.selectedDepartment,
                    semester = profileUiState.selectedSemester,
                    hostel = profileUiState.selectedHostel,
                    instagram = profileUiState.instagram,
                    linkedin = profileUiState.linkedin
                )
            }
            composable(route = InsteaScreens.EditProfile.name) {
                EditProfileScreen(
                    userName = profileUiState.userName,
                    onUserNameChanged = { profileViewModel.onUserNameChanged(it) },
                    onDepartmentChanged = { profileViewModel.onDepartmentChange(it) },
                    onSemesterChanged = { profileViewModel.onSemesterChange(it) },
                    onSaveButtonClicked = {},
                    onCancelButtonClicked = {},
                    selectedDepartment = profileUiState.selectedDepartment,
                    selectedSemester = profileUiState.selectedSemester,
                    selectedHostel = profileUiState.selectedHostel,
                    instagram = profileUiState.instagram,
                    linkedin = profileUiState.linkedin
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
