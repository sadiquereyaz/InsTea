package `in`.instea.instea.screens.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.R
import `in`.instea.instea.composable.Loader
import `in`.instea.instea.data.datamodel.PostData
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.ProfileViewModel
import `in`.instea.instea.navigation.InsteaScreens
import `in`.instea.instea.navigation.NavigationDestinations
import kotlinx.coroutines.launch


object ProfileDestination : NavigationDestinations {
    override val route: String = InsteaScreens.OtherProfile.name
    override val title: String = InsteaScreens.Feed.title
    const val USERID_ARG = "userIdArg"
    val routeWithArg = "${route}/{$USERID_ARG}"
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onSubUsernameClick: (String) -> Unit,
    navigateToDevelopers: () -> Unit,
    snackBarHostState: SnackbarHostState,
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    // Trigger data refresh when this composable is first composed or recomposed
    LaunchedEffect(uiState.showSnackBar) {
        coroutineScope.launch {
            val message = uiState.errorMessage
            if (message != null) {
                snackBarHostState.showSnackbar(message = message)
            }
        }
    }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (!uiState.isLoading && uiState.errorMessage.isNullOrBlank()) {
            // when data is loaded without error message
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //user detail
                val userData = uiState.userData
                UserTitle(
                    userData = userData,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(horizontal = 16.dp),
                    iconButtonData = if (uiState.isSelfProfile) Pair(
                        Icons.Default.Edit,
                        "Edit Profile"
                    ) else Pair(Icons.Default.ChatBubble, "Inbox"),
                    onSubUserNameClick = { onSubUsernameClick(userData!!.userId!!) }
                )
                HorizontalDivider(modifier = Modifier/*.padding(16.dp)*/)

                // academic detail
                AcademicDetails(
                    userData = userData,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Start)
                )
                HorizontalDivider(modifier = Modifier/*.padding(horizontal = 16.dp)*/)
                //about
                Text(
                    text = userData?.about ?: stringResource(id = R.string.default_bio),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                //social link
                SocialLink(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
//                            top = 16.dp,
//                            bottom = 20.dp
                        )
                        .fillMaxWidth(),
                    user = userData,
                    socialList = uiState.socialList,
                    openSocialLink = { link ->
                        coroutineScope.launch {
                            viewModel.handleSocialItemClick(link, context)
                        }
                    }
                )
                // FEED tabs
                PersonalizedFeed(uiState = uiState)
                Spacer(modifier = Modifier.weight(1f))
            }
            // developer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Made With ❤️ by ",
                    )
                    Text(
                        text = "Jamians", // Make Jamians clickable
                        modifier = Modifier
                            .clickable { navigateToDevelopers() },
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else if (!uiState.errorMessage.isNullOrBlank()) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = uiState.errorMessage ?: "An error occur while loading profile data.",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
        }else{
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
               Loader()
            }
        }
    }
}

@Composable
private fun PersonalizedFeed(uiState: ProfileUiState) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val savedPosts = uiState.savedPosts ?: emptyList()
    val myPosts = uiState.savedPosts ?: emptyList()

    val tabs = mutableListOf("Profile Posts (${myPosts.size})")
    val showOtherTab = uiState.isSelfProfile
    if (showOtherTab) {
        tabs.add("Saved (${savedPosts.size})")
    }

    TabRow(selectedTabIndex = selectedTabIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = selectedTabIndex == index,
                onClick = { selectedTabIndex = index }
            )
        }
    }
    when (selectedTabIndex) {
        0 -> {
            TabItem(postList = myPosts)
        }

        1 -> {
            TabItem(postList = savedPosts, noPostText = "No Saved Post")
        }

    }
}

@Composable
fun TabItem(
    modifier: Modifier = Modifier,
    postList: List<PostData>,
    noPostText: String = "No Post Made"
) {
    if (postList.isEmpty()) {
        Text(
            text = noPostText,
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 120.dp)
        )
    } else {
        LazyColumn {
            items(postList) { post ->
//                PostCard(post, {})
            }
        }
    }
}

@Composable
fun SocialLink(
    modifier: Modifier,
    currentUser: Boolean = true,
    user: User?,
    socialList: List<SocialModel> = emptyList(),
    openSocialLink: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
    ) {
        items(socialList) { item ->
            val title = item.title
//            Log.d("TITLE", "SocialLink: $title")
            if (title.isNotEmpty())
                SocialItem(
                    title = title,
                    onSocialItemClicked = { openSocialLink(item.linkHead + title) },
                    icon = item.icon
                )
        }

        /*  if (currentUser) {
              item {
                  SocialItem(
                      Pair("Add Social link", Icons.Default.AddCircle),
                      onSocialItemClick = {},
                  )
              }
          }*/
    }
}

@Composable
private fun SocialItem(
    onSocialItemClicked: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    icon: Any
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(end = 12.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(50)
            )
            .clickable {
                onSocialItemClicked()
            }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        if (icon is ImageVector) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon as ImageVector,
                contentDescription = "social icon"
            )
        } else
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = icon as Int),
                contentDescription = "social icon"
            )
        Text(text = title, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun AcademicDetails(userData: User?, modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AcademicItem(icon = Icons.Default.AddCircle, text = userData?.university ?: "")
        AcademicItem(icon = Icons.Default.Home, text = "${userData?.dept}-${userData?.sem}" ?: "")
    }
}


@Composable
private fun AcademicItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = icon,
            contentDescription = "item icon"
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            fontSize = 18.sp,
            text = text,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun UserTitle(
    userData: User?,
    modifier: Modifier,
    onSubUserNameClick: () -> Unit,
    dpId: Int = R.drawable.dp,
    iconButtonData: Pair<ImageVector, String> = Pair(Icons.Default.Edit, "Edit Profile")
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //avatar
        Image(
            modifier = Modifier
                .size(60.dp)
                .clip(shape = RoundedCornerShape(50)),
            painter = painterResource(id = dpId),
            contentDescription = "profile avatar",
        )
        // username and sub bio
        Column(
            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp)
        ) {
            //username
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = userData?.username ?: "",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            // sub-username
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // TODO instea points
                /*Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("1.2H ")
                        }
                        append("InsTea")
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
//                    fontWeight = FontWeight.Bold
                )*/

                //sub username button
                Row(
                    modifier = Modifier
                        .clickable { onSubUserNameClick() }
                        .padding(start = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = iconButtonData.first,
                        contentDescription = "subUserName icon",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = iconButtonData.second, modifier = Modifier.padding(start = 4.dp),
//                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfilePreview() {
//    SelfProfileScreen()
}