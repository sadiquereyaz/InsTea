import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.R
import `in`.instea.instea.data.FeedViewModel
import `in`.instea.instea.data.datamodel.Comments
import `in`.instea.instea.data.datamodel.PostData
import `in`.instea.instea.data.datamodel.Replies
import `in`.instea.instea.data.viewmodel.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyList(post:PostData,comment: Comments,feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
   var textstate by remember{ mutableStateOf("") }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = if(comment.replies.isEmpty()) Modifier.height(100.dp).fillMaxWidth() else Modifier.fillMaxWidth().height(500.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dp),
                    contentDescription = "profile icon",
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(CircleShape)
                        .size(30.dp)
                        .align(Alignment.CenterVertically)
                )
                Card(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    OutlinedTextField(
                        value = textstate,
                        onValueChange = { textstate = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        placeholder = { Text(text = "Add a comment...") },
                        trailingIcon = {
                            if (textstate.isNotEmpty()) {
                                Icon(
                                    modifier = Modifier.clickable {
//                                        getting current comment index
                                        val commentIndex = post.comments.indexOf(comment)

                                        comment.replies.add(
                                            Replies(
                                                reply = textstate,
                                                replyByUser = feedViewModel.currentuser!!,
                                            )
                                        )
//                                        setting updated comment
                                       post.comments[commentIndex] = comment

                                        feedViewModel.updateComment(post)
                                        textstate = "" // Clear text field after sending comment
                                    },
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "comment send"
                                )
                            }
                        }
                    )

                }
            }
        }
        if (comment.replies.isEmpty()) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No Comments Yet...",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {
            items(comment.replies) { reply ->
                ReplyCard(reply,comment)
            }
        }
    }
}