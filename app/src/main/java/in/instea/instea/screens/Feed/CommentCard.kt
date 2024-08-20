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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.instea.instea.R
import `in`.instea.instea.data.viewmodel.FeedViewModel
import `in`.instea.instea.data.datamodel.Comments
import `in`.instea.instea.data.datamodel.PostData
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.navigation.InsteaScreens
import kotlinx.coroutines.launch

@Composable
fun CommentCard(
    comment: Comments, post: PostData, navController: NavController,
    feedViewModel: FeedViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    ),
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var showReply by remember { mutableStateOf(false) }
val user = feedViewModel.userList.collectAsState().value.find { it.userId == comment.commentByUser }
    Box(
        modifier = Modifier
            .padding(start = 3.dp, end = 3.dp, bottom = 0.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.CenterStart

    ) {

        Box(modifier = Modifier.padding(2.dp)) {
            Column(
                modifier = Modifier

                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        navController.navigate(InsteaScreens.OtherProfile.name + "/${if (comment.commentByUser != null) comment.commentByUser else " "}")
                    }
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.logo
                        ),
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .clickable {

                            },
                        contentDescription = "Profile"
                    )

                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = if(user?.username != null) user.username else "custom",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,

                            )

                        val displayText = if (isExpanded) comment.comment
                        else comment.comment.take(100)
                        Text(
                            text = displayText!!, modifier = Modifier.padding(2.dp)
                        )
                        if (comment.comment.length!! > 100) {

                            TextButton(
                                onClick = {
                                    isExpanded = !isExpanded
                                },

                                ) {
                                Text(
                                    text = if (isExpanded) "Show Less" else "Read More",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


//                Spacer(modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp)
                ) {

                    UpAndDownVoteButtonsForComment(comment, post, navController = navController)

                }


            }
        }

    }
    if (showReply) {
        ReplyList(post = post, comment = comment)
    }
}

@Composable
fun UpAndDownVoteButtonsForComment(
    comment: Comments,
    post: PostData,

    navController: NavController,
) {
    val isUpVoted = rememberSaveable { mutableStateOf(false) }
    val isDownVoted = rememberSaveable { mutableStateOf(false) }
    val feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val coroutineScope = rememberCoroutineScope()
    var expandDropdown by remember { mutableStateOf(false) }
    val moreList = listOf("Report", "Delete", "Edit")
    var showComments by remember {
        mutableStateOf(false)
    }
    var userDislikeCurrentPost by rememberSaveable {
        mutableStateOf(comment.userDislikeComment.contains(feedViewModel.currentuser))
    }
    var userlikeCurrentPost by rememberSaveable {
        mutableStateOf(comment.userLikedComment.contains(feedViewModel.currentuser))
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Icon(
                    painter = painterResource(id = if (userDislikeCurrentPost) R.drawable.arrowdownfilled else R.drawable.arrowdownoutline),
                    contentDescription = "Downvote",
                    modifier = Modifier
                        .height(16.dp)
                        .clickable {
                            if (feedViewModel.currentuser != comment.commentByUser) {
                                isDownVoted.value = !isDownVoted.value
                                if (isDownVoted.value) {
                                    isUpVoted.value = false
                                }

                                coroutineScope.launch {
                                    if (!userlikeCurrentPost && userDislikeCurrentPost) {
                                        comment.userDislikeComment.remove(feedViewModel.currentuser)
                                    } else if (userlikeCurrentPost && !userDislikeCurrentPost) {
                                        comment.userLikedComment.remove(feedViewModel.currentuser)
                                        comment.userDislikeComment.add(feedViewModel.currentuser!!)
                                    } else {
                                        comment.userDislikeComment.add(feedViewModel.currentuser!!)
                                    }

                                    val commetIndex = post.comments.indexOf(comment)
//                                feedViewModel.updateVotes(post)
                                    post.comments[commetIndex] = comment
                                    feedViewModel.updateVotes(post)
                                    isDownVoted.value = !isDownVoted.value
                                    // Update the local state to reflect changes
                                    userDislikeCurrentPost =
                                        comment.userDislikeComment.contains(feedViewModel.currentuser)
                                    userlikeCurrentPost =
                                        comment.userLikedComment.contains(feedViewModel.currentuser)
                                }
                            }
                        },
                    tint = (if (userDislikeCurrentPost) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)

                )
                Text(
                    text = comment.userDislikeComment.size.toString(),
                    fontSize = 10.sp,
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
                    text = comment.userLikedComment.size.toString(),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(0.dp)
                )
                Icon(
                    painter = painterResource(id = if (userlikeCurrentPost) R.drawable.uparrowfilled else R.drawable.arrowupoutlined),
                    contentDescription = "Upvote",
                    modifier = Modifier
                        .height(16.dp)
                        .clickable {
                            if (feedViewModel.currentuser != comment.commentByUser) {
                                isUpVoted.value = !isUpVoted.value

                                if (isUpVoted.value) {
                                    isDownVoted.value = false
                                }

                                coroutineScope.launch {
                                    if (userlikeCurrentPost && !userDislikeCurrentPost) {
                                        comment.userLikedComment.remove(feedViewModel.currentuser)
                                    } else if (!userlikeCurrentPost && userDislikeCurrentPost) {
                                        comment.userDislikeComment.remove(feedViewModel.currentuser)
                                        comment.userLikedComment.add(feedViewModel.currentuser!!)
                                    } else {
                                        comment.userLikedComment.add(feedViewModel.currentuser!!)
                                    }

//                                feedViewModel.updateVotes(post)
                                    val commetIndex = post.comments.indexOf(comment)
//                                feedViewModel.updateVotes(post)
                                    post.comments[commetIndex] = comment
                                    feedViewModel.updateVotes(post)
                                    isUpVoted.value = !isUpVoted.value
                                    // Update the local state to reflect changes
                                    userDislikeCurrentPost =
                                        comment.userDislikeComment.contains(feedViewModel.currentuser)
                                    userlikeCurrentPost =
                                        comment.userLikedComment.contains(feedViewModel.currentuser)
                                }
                            }
                        },
                    tint = (if (userlikeCurrentPost) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)


                )
                Spacer(
                    modifier = Modifier
                        .width(20.dp)
                        .height(10.dp)
                )
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
                                            feedViewModel.DeletePost(post)
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
    }
//    if (showComments){


}

