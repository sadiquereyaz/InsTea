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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.play.integrity.internal.o
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import `in`.instea.instea.R
import `in`.instea.instea.data.FeedViewModel
import `in`.instea.instea.data.datamodel.PostData
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.screens.profile.OtherProfileScreen

import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.log

@Composable
fun PostCard(
    post: PostData,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .padding(start = 3.dp, end = 3.dp, bottom = 0.dp)
            .background(MaterialTheme.colorScheme.background)

    ) {

        Box(modifier = Modifier.padding(2.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(
                            id = if (post.profileImage != null) post.profileImage
                            else R.drawable.ic_launcher_foreground
                        ),
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black)
                            .clickable {

                            },
                        contentDescription = "Profile"
                    )

                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(
                            text = "location",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "post.department!!",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


                Column {
                    Log.d("posts", "PostCard: ${post.postDescription}")
                    val displayText = if (isExpanded) post.postDescription!!
                    else post.postDescription?.take(100)
                    Text(
                        text = displayText!!, modifier = Modifier.padding(2.dp)
                    )
                    if (post.postDescription?.length!! > 100) {

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
                if (post.postImage != null) {
                    Image(
                        painter = painterResource(id = post.postImage),
                        contentDescription = "Post Image"

                    )
                }



                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(contentAlignment = Alignment.BottomStart) {
                        Button(
                            onClick = { }, colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent, contentColor = Color.Unspecified
                            ), modifier = Modifier.padding(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )

                        }

                    }
                    UpAndDownVoteButtons(post)
                }


            }
        }
        Divider(
            Modifier
                .height(3.dp)
                .fillMaxWidth()
        )
    }
}
@Composable
fun UpAndDownVoteButtons(post: PostData) {
    val isUpVoted = rememberSaveable { mutableStateOf(false) }
    val isDownVoted = rememberSaveable { mutableStateOf(false) }
    val feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val coroutineScope = rememberCoroutineScope()

    var userDislikeCurrentPost by rememberSaveable {
        mutableStateOf(post.userDislikedCurrentPost.contains(feedViewModel.currentuser))
    }
    var userlikeCurrentPost by rememberSaveable {
        mutableStateOf(post.userLikedCurrentPost.contains(feedViewModel.currentuser))
    }

    Box(contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.padding(0.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp) // Custom spacing between elements
        ) {

            Button(
                onClick = { },
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
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp) // Custom spacing between button and text
            ) {
                IconButton(
                    onClick = {
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
                            userDislikeCurrentPost =
                                post.userDislikedCurrentPost.contains(feedViewModel.currentuser)
                            userlikeCurrentPost =
                                post.userLikedCurrentPost.contains(feedViewModel.currentuser)
                        }
                    },
                    modifier = Modifier.padding(4.dp), // Minimal padding
                    enabled = true
                ) {
                    Image(
                        painter = painterResource(id = if (userlikeCurrentPost) R.drawable.uparrowfilled else R.drawable.arrowupoutlined),
                        contentDescription = "Upvote",
                        modifier = Modifier.height(10.dp),
                        colorFilter = ColorFilter.tint(if (userlikeCurrentPost) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)
                    )
                }

                Text(text = "U", fontSize = 10.sp, modifier = Modifier.padding(0.dp))
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
                Text(text = "D", fontSize = 10.sp, modifier = Modifier.padding(0.dp))

                IconButton(
                    onClick = {
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

                            // Update the local state to reflect changes
                            userDislikeCurrentPost =
                                post.userDislikedCurrentPost.contains(feedViewModel.currentuser)
                            userlikeCurrentPost =
                                post.userLikedCurrentPost.contains(feedViewModel.currentuser)
                        }
                    },
                    modifier = Modifier.padding(4.dp), // Minimal padding
                    enabled = true
                ) {
                    Image(
                        painter = painterResource(id = if (userDislikeCurrentPost) R.drawable.arrowdownfilled else R.drawable.arrowdownoutline),
                        contentDescription = "Downvote",
                        modifier = Modifier.height(10.dp),
                        colorFilter = ColorFilter.tint(if (userDislikeCurrentPost) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer)
                    )
                }
            }
        }
    }
}
