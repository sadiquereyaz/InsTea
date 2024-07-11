package `in`.instea.instea.screens.profile

import PostCard
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.R
import `in`.instea.instea.data.PostData
import `in`.instea.instea.model.UserModel
import `in`.instea.instea.model.profile.ProfileUiState
import `in`.instea.instea.model.profile.ProfileViewModel
import `in`.instea.instea.ui.AppViewModelProvider

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val profileUiState by viewModel.profileUiState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //user detail
        val userData = profileUiState.userData
        UserTitle(
            userData = userData,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 16.dp)
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
        // FEED
        PersonalizedFeed(profileUiState = profileUiState)
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
            TabItem(postList = myPosts, noPostText = "No Post Made")
        }
    }
}

@Composable
private fun TabItem(postList: List<PostData>, noPostText: String) {
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
                    name = post.name,
                    location = post.location,
                    content = post.postDescription
                )
            }
        }
    }
}

@Composable
private fun SocialLink(modifier: Modifier) {
    LazyRow(
        modifier = modifier,
    ) {
        val linkList = listOf<Pair<String, ImageVector>>(
            Pair("sadique_reyaz", Icons.Default.AddCircle),
            Pair("sadique_", Icons.Default.AccountCircle),
            Pair("sadiquereyaz", Icons.Default.Home)
        )
        items(linkList) {
            SocialItem(it)
        }
    }
}

@Composable
private fun SocialItem(it: Pair<String, ImageVector>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(end = 12.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(50)
            )
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
    Column(modifier = modifier) {
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
fun UserTitle(userData: UserModel?, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        //avatar
        Image(
            modifier = Modifier
                .size(60.dp)
                .clip(shape = RoundedCornerShape(50)),
            painter = painterResource(id = R.drawable.dp),
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

                //edit button
                Row(
                    modifier = Modifier
                        .clickable { }
                        .padding(start = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = "Edit Profile", modifier = Modifier.padding(start = 4.dp),
                        textDecoration = TextDecoration.Underline,
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
    ProfileScreen()
}