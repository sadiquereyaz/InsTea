import android.content.res.Resources.Theme
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import `in`.instea.instea.R
import `in`.instea.instea.data.FeedViewModel
import `in`.instea.instea.data.PostData
import `in`.instea.instea.data.downVotes
import `in`.instea.instea.data.upVotes
import kotlinx.coroutines.launch
import kotlin.math.abs

//@Preview
//@Composable
//private fun p() {
//    PostCard(
//        profilePic = R.drawable.ic_launcher_foreground,
//        name = "Hammad", location = "Delhi", content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
//    )
//}

@Composable
fun PostCard(
    post: PostData,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }


    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        modifier = Modifier.padding(8.dp)
    ) {

        Box(modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = if (post.profileImage != null) post.profileImage else R.drawable.ic_launcher_foreground),
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black),
                        contentDescription = "Profile"
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(text = post.name!!, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = post.department!!,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


                Column {
                    val displayText =
                        if (isExpanded) post.postDescription!! else post.postDescription?.take(50)
                    Text(
                        text = displayText!!,
                        modifier = Modifier.padding(2.dp)
                    )
                    if (post.postDescription?.length!! > 50) {

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
                Box(contentAlignment = Alignment.BottomStart) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.6f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        UpAndDownVoteButtons(post)
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Unspecified
                            ),
                            modifier = Modifier.padding(0.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chatbubble),
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )

                        }


                    }
                }

            }
        }
    }
}

@Composable
fun UpAndDownVoteButtons(post: PostData) {
    val isUpVoted = rememberSaveable { mutableStateOf(false) }
    val isDownVoted = rememberSaveable { mutableStateOf(false) }
    val feedViewModel = FeedViewModel()
    val mAuth = Firebase.auth
    val coroutineScope = rememberCoroutineScope()
    val userDislikeCurrentPost =
        post.downVote.userDislikedCurrentPost.contains(feedViewModel.currentuser)
    val userlikeCurrentPost = post.upVote.userLikedCurrentPost.contains(feedViewModel.currentuser)
    IconButton(
        onClick = {
            isUpVoted.value = !isUpVoted.value

            if (isUpVoted.value) {
                isDownVoted.value = false
                val likeBy = post.upVote.userLikedCurrentPost
                likeBy.add(mAuth.currentUser?.uid)
                coroutineScope.launch {
                    feedViewModel.updateUpVote(
                        post,
                        upVotes(post.upVote.like + 1, likeBy)
                    )
                }
            }
        },
        modifier = Modifier.padding(8.dp),
        enabled = userlikeCurrentPost == false
    ) {
        Image(
            painter = painterResource(id = if (userlikeCurrentPost) R.drawable.uparrowfilled else R.drawable.arrowupoutlined),
            contentDescription = "Upvote",
            modifier = Modifier.size(20.dp)
        )
    }
    Text(
        text = if (post.upVote.like - post.downVote.dislike > 0)
            abs(post.upVote.like - post.downVote.dislike).toString()
        else
            post.upVote.like.toString()
    )
    IconButton(
        onClick = {
            isDownVoted.value = !isDownVoted.value
            if (isDownVoted.value) {
                isUpVoted.value = false
                val dislikeBy = post.downVote.userDislikedCurrentPost
                dislikeBy.add(mAuth.currentUser?.uid)
                coroutineScope.launch {
                    feedViewModel.updateDownVote(
                        post,
                        downVotes(post.downVote.dislike + 1, dislikeBy)
                    )
                }
            } // Reset upvote if downvoting
        },
        modifier = Modifier.padding(8.dp),
        enabled = userDislikeCurrentPost == false
    ) {
        Image(
            painter = painterResource(id = if (userDislikeCurrentPost) R.drawable.arrowdownfilled else R.drawable.arrowdownoutline),
            contentDescription = "Downvote",
            modifier = Modifier.size(20.dp)
        )
    }


}