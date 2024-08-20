import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.instea.instea.R
import `in`.instea.instea.data.viewmodel.FeedViewModel
import `in`.instea.instea.data.datamodel.Comments
import `in`.instea.instea.data.datamodel.PostData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentList(post: PostData, feedViewModel: FeedViewModel,navController: NavController) {
    var textstate by remember { mutableStateOf("") }
    var comments = remember{ mutableStateListOf<Comments>().apply { addAll(post.comments) }  }

    LazyColumn(

        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = if(post.comments.isEmpty()) Modifier
            .wrapContentSize()
            .heightIn(max = 200.dp)
            .fillMaxWidth()

        else Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(0.dp)
            .heightIn(max = 500.dp)
            .border(1.dp, brush = Brush.linearGradient(listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primaryContainer
            )), RoundedCornerShape(15.dp)),

    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(start=5.dp,top = 20.dp, bottom = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dp),
                    contentDescription = "profile icon",
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(CircleShape)
                        .size(40.dp)
                        .align(Alignment.CenterVertically)
                )

                    OutlinedTextField(
                        value = textstate,
                        onValueChange = { textstate = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp, end = 12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        placeholder = { Text(text = "Add a comment...") },
                        trailingIcon = {
                            if (textstate.isNotEmpty()) {
                                Icon(
                                    modifier = Modifier.clickable {
                                        comments.add(
                                            Comments(
                                                comment = textstate,
                                                commentByUser = feedViewModel.currentuser!!,
                                            )
                                        )
                                        post.comments.add(comments.last())
                                        feedViewModel.updateVotes(post)

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


        if (comments.isEmpty()) {

            item {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No Comments Yet...",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        } else {

            items(comments.reversed()) { comment ->
                Divider()
                CommentCard(comment = comment, post = post, navController = navController)

            }
        }
    }
}
