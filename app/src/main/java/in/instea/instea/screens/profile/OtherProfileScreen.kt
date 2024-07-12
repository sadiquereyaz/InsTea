package `in`.instea.instea.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.data.PostData
import `in`.instea.instea.model.UserModel
import `in`.instea.instea.model.profile.OtherProfileUiState
import `in`.instea.instea.model.profile.OtherProfileViewModel
import `in`.instea.instea.ui.AppViewModelProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map

@Composable
fun OtherProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: OtherProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    when (val otherProfileUiState = viewModel.otherProfileUiState.collectAsState().value) {
        is OtherProfileUiState.Success -> {
            val userDataState = otherProfileUiState.userData?.collectAsState(initial = UserModel())
            val userData = userDataState?.value ?: UserModel()
            val profilePostsState = otherProfileUiState.profilePosts?.collectAsState(initial = emptyList())
            val profilePosts = profilePostsState?.value ?: emptyList()

        Box(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //user detail
                UserTitle(
                        userData = userData,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 16.dp),
                    onSubUserNameClick = { },
                        iconButtonData = Pair(Icons.Default.Email, "Conversation")
                )
                Divider(modifier = Modifier.padding(16.dp))

                    // Academic detail
                AcademicDetails(
                        userData = userData,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Start)
                )
                Divider(modifier = Modifier.padding(16.dp))

                    // About
                Text(
                        text = userData.about ?: "I like seeking good literature.",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .align(Alignment.Start)
                )

                    // Social link
                SocialLink(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 24.dp),
                    currentUser = false
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = "Profile Post (10)",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp
                )
                Divider(
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
                // FEED
                    TabItem(modifier = Modifier, postList = emptyList())
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
        is OtherProfileUiState.Error -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "An error occurred. Please try again.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp
                )
            }
        }
        is OtherProfileUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
