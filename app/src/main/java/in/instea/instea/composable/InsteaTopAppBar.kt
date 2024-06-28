package `in`.instea.instea.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import `in`.instea.instea.model.InsteaScreens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsteaTopAppBar(
    modifier: Modifier = Modifier,
    currentScreen: InsteaScreens,
    canNavigateBack: Boolean,
    navigateBack: ()->Unit,
    moveToProfile: ()->Unit
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = currentScreen.title,
                fontWeight = FontWeight.Bold
            )
        },

        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
                }
            }
        },
        actions = {
            IconButton(onClick = moveToProfile) {
                if (currentScreen != InsteaScreens.Profile) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
                }
            }
        }
    )
}