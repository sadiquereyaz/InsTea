package `in`.instea.instea.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import `in`.instea.instea.model.InsteaScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsteaTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    currentScreen: InsteaScreens,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    moveToProfile: () -> Unit,
    navController: NavHostController
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
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
            }
        },
        actions = {
            if (currentScreen == InsteaScreens.Feed) {
                IconButton(onClick = moveToProfile) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
                }
            } else if (currentScreen == InsteaScreens.Schedule){
                IconButton(onClick = { navController.navigate(InsteaScreens.Attendance.name) }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Attendance")
                }
            }else if (currentScreen == InsteaScreens.Profile){
                IconButton(onClick = { navController.navigate(InsteaScreens.EditProfile.name) }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile")
                }
            }
        }
    )
}