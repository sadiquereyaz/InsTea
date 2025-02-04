package `in`.instea.instea.presentation.components

//import `in`.instea.instea.data.AuthViewModel
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import `in`.instea.instea.R
import `in`.instea.instea.presentation.feed.FeedViewModel
import `in`.instea.instea.presentation.navigation.InsteaScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsteaTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    currentScreen: InsteaScreens,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
    moveToAttendanceSummary: (Int?) -> Unit,
    moveToSelfProfile: () -> Unit,
    moveToOtherProfile: () -> Unit,
    navController: NavHostController,
    onAddButtonClicked: () -> Unit,
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
                    navController.navigate(InsteaScreens.Search.name)
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
                IconButton(
                    onClick = { moveToAttendanceSummary(1) }
                ) {
                    Icon(imageVector = Icons.Default.Percent, contentDescription = "Attendance")
                }
            } else if (currentScreen == InsteaScreens.SelfProfile) {
                IconButton(onClick = {
                    moveToAttendanceSummary(-1)
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.more),
                        contentDescription = "Edit Profile"
                    )
                }
            } /*else if (currentScreen == InsteaScreens.More) {
                IconButton(onClick = { navController.navigate(InsteaScreens.EditProfile.name) }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Edit Profile")
                }
            }*/
        }
    )
}

@Composable
fun Filteruser(feedViewModel: FeedViewModel, query:String){
    val users = feedViewModel.userList.collectAsState().value

}
