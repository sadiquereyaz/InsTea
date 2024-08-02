import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.play.integrity.internal.o
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.PostData
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.FeedViewModel
import `in`.instea.instea.navigation.InsteaScreens
//import `in`.instea.instea.screens.Feed.CommentList
import `in`.instea.instea.screens.profile.OtherProfileScreen

import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.log

@Composable
fun PostCard(
    post: PostData,
    navigateToProfile: () -> Unit,
    feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory),
    userList: List<User>,
    navController: NavController
) {
    var isExpanded by remember { mutableStateOf(false) }
    var showComments by remember { mutableStateOf(false) } // State for showing/hiding CommentCard
    var expandDropdown by remember { mutableStateOf(false) }
    val moreList = listOf("Report", "Delete", "Edit")
    var user: User = User()

    Log.d("PostCard", "userlist: $userList")
    for (u in userList) {
        if (u.userId == post.postedByUser) {
            user = u
            break
        }
    }
    var userName = user.username
    Box(
        modifier = Modifier
            .padding(start = 3.dp, end = 3.dp, bottom = 0.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
//                        navController.navigate(InsteaScreens.OtherProfile.name + "/${if (user.userId != null) user.userId else " "}")
                        navigateToProfile()

                    }
            ) {
                (if (post.profileImage != null) post.profileImage
                else R.drawable.ic_launcher_foreground)?.let {
                    Image(
                        painter = painterResource(id = it),
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .clickable { navigateToProfile() },
                        contentDescription = "Profile"
                    )

                    Column(modifier = Modifier.padding(start = 8.dp)) {

                        if (post.isAnonymous)
                            userName = "UnderCover"
                        Text(
                            text = if (userName != null) {
                                userName!!
                            } else "", fontSize = 14.sp, fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = post.timestamp.format(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    }

                Spacer(modifier = Modifier.weight(1f)) // This pushes the Box to the end

                    Box(
                        modifier = Modifier.padding(end = 8.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(onClick = { expandDropdown = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }

                        DropdownMenu(
                            modifier = Modifier.height(100.dp),
                            expanded = expandDropdown,
                            onDismissRequest = { expandDropdown = false }) {
                            moreList.forEach { type ->
                                if (
                                    feedViewModel.currentuser == post.postedByUser
                                    && type != "Report"
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(type) },
                                        onClick = {
                                            if (type == "Delete") {
//                                                feedViewModel.DeletePost(post)
                                            }
                                            if (type == "Edit") {
                                                navController.navigate(InsteaScreens.EditPost.name + "/${post.postid}")
                                            }
                                            expandDropdown = false // Close the dropdown menu
                                        }
                                    )

                                } else if (
                                    feedViewModel.currentuser != post.postedByUser &&
                                    type != "Edit" && type != "Delete"
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(type) },
                                        onClick = {
                                            expandDropdown = false // Close the dropdown menu
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Post Description
            Column(horizontalAlignment = Alignment.Start) {
                val displayText = if (isExpanded) post.postDescription!!
                else post.postDescription?.take(100)
                Text(text = displayText!!, modifier = Modifier.padding(2.dp))
                if (post.postDescription?.length!! > 100) {
                    TextButton(onClick = { isExpanded = !isExpanded }) {
                        Text(text = if (isExpanded) "Show Less" else "Read More", fontSize = 12.sp)
                    }
                }
                if (post.edited) {
                    Text(
                        text = "Edited",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .padding(2.dp)
                            .align(Alignment.Start)
                    )
                }

            }

            // Post Image
            if (post.postImage != null) {
                Image(
                    painter = painterResource(id = post.postImage!!),
                    contentDescription = "Post Image"
                )
            }


            // Comment Button and Up/Down Vote Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Box(contentAlignment = Alignment.BottomStart) {
                    Button(
                        onClick = {
                            feedViewModel.inserLocal(post)
                            post.saved = !post.saved
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Unspecified
                        ),
                        modifier = Modifier.padding(0.dp)
                    ) {
                        Icon(
                            imageVector = if (post.saved) Icons.Filled.Bookmark else Icons.Outlined.Bookmark,
                            contentDescription = "",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                UpAndDownVoteButtons(post, showComments) { isVisible ->
                    showComments = isVisible
                }
            }

        }
    }
    Divider(
        Modifier
            .height(1.dp)
            .fillMaxWidth()
    )
    if (showComments) {
        CommentList(post, feedViewModel)
    }
}


@Composable
fun UpAndDownVoteButtons(post: PostData, showComments: Boolean, onCommentClick: (Boolean) -> Unit) {
    val isUpVoted = rememberSaveable { mutableStateOf(false) }
    val isDownVoted = rememberSaveable { mutableStateOf(false) }
    val feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val coroutineScope = rememberCoroutineScope()
    Log.d("currentuser", "UpAndDownVoteButtons: ${feedViewModel.currentuser}")
    var userDislikeCurrentPost by rememberSaveable {
        mutableStateOf(post.userDislikedCurrentPost.contains(feedViewModel.currentuser))
    }
    var userlikeCurrentPost by rememberSaveable {
        mutableStateOf(post.userLikedCurrentPost.contains(feedViewModel.currentuser))
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(3.dp, end = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Custom spacing between elements
        ) {

            Button(
                onClick = {
                    onCommentClick(!showComments) // Toggle comment visibility
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Unspecified
                ),
                modifier = Modifier
                    .padding(0.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chatbubble),
                    contentDescription = "comment",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp) // Custom spacing between button and text
            ) {

                Icon(
                    painter = painterResource(id = if (userDislikeCurrentPost) R.drawable.arrowdownfilled else R.drawable.arrowdownoutline),
                    contentDescription = "Downvote",
                    modifier = Modifier
                        .height(16.dp)
                        .clickable {
                            isDownVoted.value = !isDownVoted.value
                            if (isDownVoted.value) {
                                isUpVoted.value = false
                            }

                            coroutineScope.launch {
                                if (!userlikeCurrentPost && userDislikeCurrentPost) {
                                    post.userDislikedCurrentPost.remove(feedViewModel.currentuser)
                                } else if (userlikeCurrentPost && !userDislikeCurrentPost) {
                                    post.userLikedCurrentPost.remove(feedViewModel.currentuser)
                                    post.userDislikedCurrentPost.add(feedViewModel.currentuser)
                                } else {
                                    post.userDislikedCurrentPost.add(feedViewModel.currentuser)
                                }

                                feedViewModel.updateVotes(post)
                                isDownVoted.value = !isDownVoted.value
                                // Update the local state to reflect changes
                                userDislikeCurrentPost =
                                    post.userDislikedCurrentPost.contains(feedViewModel.currentuser)
                                userlikeCurrentPost =
                                    post.userLikedCurrentPost.contains(feedViewModel.currentuser)
                            }
                        },
                    tint = (if (userDislikeCurrentPost) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
                )
                Text(
                    text = (post.userDislikedCurrentPost.size - 1).toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(0.dp)
                )
            }

            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(Color.Black)
                    .align(Alignment.CenterVertically)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp) // Custom spacing between button and text
            ) {

                Text(
                    text = (post.userLikedCurrentPost.size - 1).toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(0.dp)
                )
                Icon(
                    painter = painterResource(id = if (userlikeCurrentPost) R.drawable.uparrowfilled else R.drawable.arrowupoutlined),
                    contentDescription = "Upvote",
                    modifier = Modifier
                        .height(16.dp)
                        .clickable {
                            isUpVoted.value = !isUpVoted.value

                            if (isUpVoted.value) {
                                isDownVoted.value = false
                            }

                            coroutineScope.launch {
                                if (userlikeCurrentPost && !userDislikeCurrentPost) {
                                    post.userLikedCurrentPost.remove(feedViewModel.currentuser)
                                } else if (!userlikeCurrentPost && userDislikeCurrentPost) {
                                    post.userDislikedCurrentPost.remove(feedViewModel.currentuser)
                                    post.userLikedCurrentPost.add(feedViewModel.currentuser)
                                } else {
                                    post.userLikedCurrentPost.add(feedViewModel.currentuser)
                                }

                                feedViewModel.updateVotes(post)

                                // Update the local state to reflect changes
                                isUpVoted.value = !isUpVoted.value
                                userDislikeCurrentPost =
                                    post.userDislikedCurrentPost.contains(feedViewModel.currentuser)
                                userlikeCurrentPost =
                                    post.userLikedCurrentPost.contains(feedViewModel.currentuser)
                            }
                        },
                    tint = (if (userlikeCurrentPost) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }
}

//@Composable
//fun UpAndDownVoteButtons(post: PostData) {
//    val isUpVoted = rememberSaveable { mutableStateOf(false) }
//    val isDownVoted = rememberSaveable { mutableStateOf(false) }
//    val feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
//    val coroutineScope = rememberCoroutineScope()
//    val showComments by remember {
//        mutableStateOf(false)
//    }
//    var userDislikeCurrentPost by rememberSaveable {
//        mutableStateOf(post.userDislikedCurrentPost.contains(feedViewModel.currentuser))
//    }
//    var userlikeCurrentPost by rememberSaveable {
//        mutableStateOf(post.userLikedCurrentPost.contains(feedViewModel.currentuser))
//    }
//
//    Box(
//        contentAlignment = Alignment.BottomEnd,
//        modifier = Modifier.padding(3.dp)
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(8.dp) // Custom spacing between elements
//        ) {
//
//            Button(
//                onClick = {
//
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Transparent,
//                    contentColor = Color.Unspecified
//                ),
//                modifier = Modifier
//                    .padding(0.dp)
//                    .align(Alignment.CenterVertically)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.chatbubble),
//                    contentDescription = "",
//                    modifier = Modifier.size(20.dp)
//                )
//            }
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp) // Custom spacing between button and text
//            ) {
//
//                Icon(
//                    painter = painterResource(id = if (userDislikeCurrentPost) R.drawable.arrowdownfilled else R.drawable.arrowdownoutline),
//                    contentDescription = "Downvote",
//                    modifier = Modifier
//                        .height(16.dp)
//                        .clickable {
//                            isDownVoted.value = !isDownVoted.value
//                            if (isDownVoted.value) {
//                                isUpVoted.value = false
//                            }
//
//                            coroutineScope.launch {
//                                if (!userlikeCurrentPost && userDislikeCurrentPost) {
//                                    post.userDislikedCurrentPost.remove(feedViewModel.currentuser)
//                                } else if (userlikeCurrentPost && !userDislikeCurrentPost) {
//                                    post.userLikedCurrentPost.remove(feedViewModel.currentuser)
//                                    post.userDislikedCurrentPost.add(feedViewModel.currentuser)
//                                } else {
//                                    post.userDislikedCurrentPost.add(feedViewModel.currentuser)
//                                }
//
//                                feedViewModel.updateVotes(post)
//
//                                // Update the local state to reflect changes
//                                userDislikeCurrentPost =
//                                    post.userDislikedCurrentPost.contains(feedViewModel.currentuser)
//                                userlikeCurrentPost =
//                                    post.userLikedCurrentPost.contains(feedViewModel.currentuser)
//                            }
//                        },
//                    tint = (if (userDislikeCurrentPost) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)
//
//                )
//                Text(text = "D", fontSize = 10.sp, modifier = Modifier.padding(0.dp))
//
//            }
//
//            Spacer(
//                modifier = Modifier
//                    .width(1.dp)
//                    .height(16.dp)
//                    .background(Color.Black)
//                    .align(Alignment.CenterVertically)
//            )
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp) // Custom spacing between button and text
//            ) {
//
//                Text(text = "U", fontSize = 10.sp, modifier = Modifier.padding(0.dp))
//                Icon(
//                    painter = painterResource(id = if (userlikeCurrentPost) R.drawable.uparrowfilled else R.drawable.arrowupoutlined),
//                    contentDescription = "Upvote",
//                    modifier = Modifier
//                        .height(16.dp)
//                        .clickable {
//                            isUpVoted.value = !isUpVoted.value
//
//                            if (isUpVoted.value) {
//                                isDownVoted.value = false
//                            }
//
//                            coroutineScope.launch {
//                                if (userlikeCurrentPost && !userDislikeCurrentPost) {
//                                    post.userLikedCurrentPost.remove(feedViewModel.currentuser)
//                                } else if (!userlikeCurrentPost && userDislikeCurrentPost) {
//                                    post.userDislikedCurrentPost.remove(feedViewModel.currentuser)
//                                    post.userLikedCurrentPost.add(feedViewModel.currentuser)
//                                } else {
//                                    post.userLikedCurrentPost.add(feedViewModel.currentuser)
//                                }
//
//                                feedViewModel.updateVotes(post)
//
//                                // Update the local state to reflect changes
//                                userDislikeCurrentPost =
//                                    post.userDislikedCurrentPost.contains(feedViewModel.currentuser)
//                                userlikeCurrentPost =
//                                    post.userLikedCurrentPost.contains(feedViewModel.currentuser)
//                            }
//                        },
//                    tint = (if (userlikeCurrentPost) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)
//
//
//                )
//
//
//            }
//        }
//    }
//    CommentCard(post = post)
//}
