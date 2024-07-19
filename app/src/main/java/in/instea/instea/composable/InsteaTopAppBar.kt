package `in`.instea.instea.composable

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
//import `in`.instea.instea.data.AuthViewModel
import `in`.instea.instea.data.FeedViewModel
import `in`.instea.instea.data.viewmodel.AuthViewModel
import `in`.instea.instea.navigation.InsteaScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsteaTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    currentScreen: InsteaScreens,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    moveToSelfProfile: () -> Unit,
    moveToOtherProfile: () -> Unit,
    navController: NavHostController,
    onAddButtonClicked:  ()->Unit
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        title = {
            Text(
                text = currentScreen.title,
                fontWeight = FontWeight.Bold
            )
        },

        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "back",
                        modifier = Modifier.size(36.dp)
                    )
                }
            } else if (currentScreen == InsteaScreens.Feed) {

                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }
        },
        actions = {
            if (currentScreen == InsteaScreens.Feed) {
                // Add button on the right for Feed screen
                IconButton(onClick = {
                    onAddButtonClicked()
                }) {
                    Icon(
                        imageVector = Icons.Default.AddCircleOutline,
                        contentDescription = "Add"
                    )
                }
            } else if (currentScreen == InsteaScreens.Schedule) {
                IconButton(onClick = { navController.navigate(InsteaScreens.Attendance.name) }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Attendance")
                }
            } else if (currentScreen == InsteaScreens.SelfProfile) {
                IconButton(onClick = { AuthViewModel().signOut()
                navController.navigate(InsteaScreens.SignIn.name)}) {
                    Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Edit Profile")
                }
            } else if (currentScreen == InsteaScreens.EditProfile) {
                IconButton(onClick = { navController.navigate(InsteaScreens.EditProfile.name) }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Edit Profile")
                }
            }
        }
    )
}



