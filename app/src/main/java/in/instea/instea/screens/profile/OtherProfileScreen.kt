package `in`.instea.instea.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.data.PostData
import `in`.instea.instea.model.UserModel

@Composable
fun OtherProfileScreen(
    modifier: Modifier = Modifier,
//    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
//    val profileUiState by viewModel.profileUiState.collectAsState()
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //user detail
//            val userData = profileUiState.userData
            val userData = UserModel()
            UserTitle(
                userData = userData,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 16.dp),
                onSubUserNameClick = { },
                iconButtonData = Pair<ImageVector, String>(Icons.Default.Email, "Conversation")

            )
            Divider(modifier = Modifier.padding(16.dp))

            // academic detail
            AcademicDetails(
                userData = userData,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Start)
            )
            Divider(modifier = Modifier.padding(16.dp))

            //about
            Text(
                text = userData?.about ?: "I like seeking good literature.",
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .align(Alignment.Start)
            )

            //social link
            SocialLink(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 24.dp),
                currentUser = false
            )
            Text(
                modifier = Modifier.align(Alignment.Start).padding(start =16.dp),
                text = "Profile Post (10)",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
            Divider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp))
            // FEED
            TabItem(modifier = Modifier, postList = emptyList<PostData>())
            Spacer(modifier = Modifier.weight(1f))
        }
        // developer
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Made With ❤️ by ",
            )
            Text(
                text = "Jamians", // Make Jamians clickable
                modifier = Modifier
                    .clickable { /* Handle click on Jamians */ },
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
