package `in`.instea.instea.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.OtherProfileUiState
import `in`.instea.instea.data.viewmodel.OtherProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun OtherProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: OtherProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userId: String = ""
) {
    when (val otherProfileUiState = viewModel.otherProfileUiState.collectAsState().value) {
        is OtherProfileUiState.Success -> {
            val userDataState = otherProfileUiState.userData?.collectAsState(initial = User())
            val userData = userDataState?.value ?: User()
            val profilePostsState =
                otherProfileUiState.profilePosts?.collectAsState(initial = emptyList())
            val profilePosts = profilePostsState?.value ?: emptyList()
            val coroutineScope = rememberCoroutineScope()

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
                        currentUser = false,
                        user = User(),
                        openSocialLink = {
                            coroutineScope.launch {
//                                viewModel.handleSocialItemClick(socialObj.link)
                            }
                        }
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
//                    TabItem(modifier = Modifier, postList = emptyList())
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
