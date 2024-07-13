package `in`.instea.instea.screens.profile

import PostCard
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.RoomPostModel
import `in`.instea.instea.data.datamodel.UserModel
import `in`.instea.instea.data.viewmodel.SelfProfileViewModel
import `in`.instea.instea.data.viewmodel.AppViewModelProvider

@Composable
fun SelfProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: SelfProfileViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val profileUiState by viewModel.profileUiState.collectAsState()
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //user detail
            val userData = profileUiState.userData
            UserTitle(
                userData = userData,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 16.dp),
                onSubUserNameClick = { }
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
                text = userData?.about ?: "About",
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )

            //social link
            SocialLink(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 20.dp,
                    bottom = 20.dp
                )
            )
            // FEED tabs
            PersonalizedFeed(profileUiState = profileUiState)
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
                        .clickable { /* Handle click on Jamians */ },
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

    }
}

@Composable
private fun PersonalizedFeed(profileUiState: ProfileUiState) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val savedPosts = profileUiState.savedPosts ?: emptyList()
    val myPosts = profileUiState.savedPosts ?: emptyList()

    val tabs = listOf("Saved (${savedPosts.size})", "My Posts (${myPosts.size})")

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
            TabItem(postList = savedPosts, noPostText = "No Saved Post")
        }

        1 -> {
            TabItem(postList = myPosts)
        }
    }
}

@Composable
fun TabItem(
    modifier: Modifier = Modifier,
    postList: List<RoomPostModel>,
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
                PostCard(
                )
            }
        }
    }
}

@Composable
fun SocialLink(modifier: Modifier, currentUser: Boolean = true) {
    LazyRow(
        modifier = modifier,
    ) {
        val linkList = listOf<Pair<String, ImageVector>>(
            Pair("sadique_reyaz", Icons.Default.AddCircle),
            Pair("sadique_", Icons.Default.AccountCircle),
            Pair("sadiquereyaz", Icons.Default.Home)
        )
        items(linkList) {
            SocialItem(it, onSocialItemClick = {})
        }
        if (currentUser) {
            item {
                SocialItem(
                    Pair("Add Social link", Icons.Default.AddCircle),
                    onSocialItemClick = {},
                )
            }
        }
    }
}

@Composable
private fun SocialItem(
    it: Pair<String, ImageVector>,
    onSocialItemClick: () -> Unit,
    modifier: Modifier = Modifier
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
            .clickable { onSocialItemClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = it.second,
            contentDescription = "social icon"
        )
        Text(text = it.first, modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun AcademicDetails(userData: UserModel?, modifier: Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AcademicItem(icon = Icons.Default.AddCircle, text = "Jamia Millia Islamia")
        AcademicItem(icon = Icons.Default.Home, text = "Computer Engineering-VII")
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
    userData: UserModel?,
    modifier: Modifier,
    onSubUserNameClick: () -> Unit,
    dpId: Int = R.drawable.dp,
    iconButtonData: Pair<ImageVector, String> = Pair(Icons.Default.Edit, "Edit Profile")
) {
    Row(
        modifier = modifier.clickable { onSubUserNameClick() },
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
                text = userData?.username ?: "Default User",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            // sub-username
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // instea points
                Text(
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
                )

                //sub username button
                Row(
                    modifier = Modifier
                        .clickable { }
                        .padding(start = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = iconButtonData.first,
                        contentDescription = "subUserName icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = iconButtonData.second, modifier = Modifier.padding(start = 4.dp),
//                        textDecoration = TextDecoration.Underline,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfilePreview() {
    SelfProfileScreen()
}