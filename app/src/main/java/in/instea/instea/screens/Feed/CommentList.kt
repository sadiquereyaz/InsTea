package `in`.instea.instea.screens.Feed

import CommentCard
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import `in`.instea.instea.R
import `in`.instea.instea.data.FeedViewModel

@Composable
fun CommentList(feedViewModel: FeedViewModel) {
    Column {
        Row(verticalAlignment = Alignment.Top) {
            Icon(
                painter = painterResource(id = R.drawable.dp),
                contentDescription = "profile icon",
                modifier = Modifier.clip(CircleShape)
            )
            
        }
        repeat(10) {
            CommentCard(post = feedViewModel.posts.value[it])
        }
    }
}