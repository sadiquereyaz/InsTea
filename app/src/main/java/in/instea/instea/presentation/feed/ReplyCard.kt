package `in`.instea.instea.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.R
import `in`.instea.instea.di.AppViewModelProvider
import `in`.instea.instea.domain.datamodel.Comments
import `in`.instea.instea.domain.datamodel.PostData
import `in`.instea.instea.domain.datamodel.Replies
import `in`.instea.instea.presentation.feed.FeedViewModel
import kotlinx.coroutines.launch

@Composable
fun ReplyCard(reply: Replies, comment: Comments, post: PostData) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(0.8f)
            .padding(start = 30.dp, end = 2.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(modifier = Modifier.padding(2.dp), horizontalAlignment = Alignment.Start) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.dp),
//                    modifier = Modifier
//                        .size(30.dp)
//                        .clip(CircleShape)
//                        .background(Color.Black)
//                        .clickable {},
//                    contentDescription = "Profile"
//                )

                Column(
                    modifier = Modifier.padding(start = 8.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "location",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    )

                    val displayText = if (isExpanded) reply.reply else reply.reply.take(100)
                    Text(
                        text = displayText!!, modifier = Modifier.padding(2.dp)
                    )
                    if (reply.reply.length!! > 100) {
                        TextButton(onClick = { isExpanded = !isExpanded }) {
                            Text(
                                text = if (isExpanded) "Show Less" else "Read More",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

//            Row(
//                verticalAlignment = Alignment.Top,
//                horizontalArrangement = Arrangement.SpaceBetween,
//                modifier = Modifier.fillMaxWidth()
//            ) {
                UpAndDownVoteButtonsForReply(comment, reply,post)
//            }
        }
    }
}


@Composable
fun UpAndDownVoteButtonsForReply(comment: Comments, reply: Replies, post: PostData) {
    val isUpVoted = rememberSaveable { mutableStateOf(false) }
    val isDownVoted = rememberSaveable { mutableStateOf(false) }
    val feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val coroutineScope = rememberCoroutineScope()

    var userDislikeCurrentPost by rememberSaveable {
        mutableStateOf(reply.userDislikeReply.contains(feedViewModel.currentuser))
    }
    var userlikeCurrentPost by rememberSaveable {
        mutableStateOf(reply.userDislikeReply.contains(feedViewModel.currentuser))
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxSize(0.7f).padding(3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Custom spacing between elements
        ) {


            Box(
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "report")
                val list = listOf("Report","Edit Comment")

            }
            Spacer(modifier = Modifier.width(20.dp).height(10.dp))


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
                                    reply.userDislikeReply.remove(feedViewModel.currentuser)
                                } else if (userlikeCurrentPost && !userDislikeCurrentPost) {
                                    reply.userLikedReply.remove(feedViewModel.currentuser)
                                    reply.userDislikeReply.add(feedViewModel.currentuser!!)
                                } else {
                                    reply.userDislikeReply.add(feedViewModel.currentuser!!)
                                }

                                val replyIndex = comment.replies.indexOf(reply)
                                val commmentIndex = post.comments.indexOf(comment)
                                comment.replies[replyIndex]= reply
                               post.comments[commmentIndex] = comment
                                feedViewModel.updateVotes(post)

                                // Update the local state to reflect changes
                                userDislikeCurrentPost =
                                    reply.userDislikeReply.contains(feedViewModel.currentuser)
                                userlikeCurrentPost =
                                    reply.userLikedReply.contains(feedViewModel.currentuser)
                            }
                        },
                    tint = (if (userDislikeCurrentPost) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)

                )
                Text(text = reply.userDislikeReply.size.toString(), fontSize = 10.sp, modifier = Modifier.padding(0.dp))

            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp) // Custom spacing between button and text
            ) {

                Text(text =reply.userLikedReply.size.toString(), fontSize = 10.sp, modifier = Modifier.padding(0.dp))
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
                                    reply.userLikedReply.remove(feedViewModel.currentuser)
                                } else if (!userlikeCurrentPost && userDislikeCurrentPost) {
                                    reply.userDislikeReply.remove(feedViewModel.currentuser)
                                    reply.userLikedReply.add(feedViewModel.currentuser!!)
                                } else {
                                    reply.userLikedReply.add(feedViewModel.currentuser!!)
                                }

                                val replyIndex = comment.replies.indexOf(reply)
                                val commmentIndex = post.comments.indexOf(comment)
                                comment.replies[replyIndex]= reply
                                post.comments[commmentIndex] = comment
                                feedViewModel.updateVotes(post)
                                // Update the local state to reflect changes
                                userDislikeCurrentPost =
                                    reply.userDislikeReply.contains(feedViewModel.currentuser)
                                userlikeCurrentPost =
                                    reply.userLikedReply.contains(feedViewModel.currentuser)
                            }
                        },
                    tint = (if (userlikeCurrentPost) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)


                )

            }
        }
    }



}

