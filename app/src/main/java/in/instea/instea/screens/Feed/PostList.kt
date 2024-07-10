import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import `in`.instea.instea.data.FeedViewModel

//import androidx.lifecycle.viewmodel.CreationExtras.Empty.map

@Composable
fun PostList(feedViewModel: FeedViewModel) {
    val feeduiState by feedViewModel.feedUiState.collectAsState()
    val postDataList=feeduiState.posts.reversed()
    LazyColumn {
      items(postDataList){index->
          PostCard(
              index)

      }
    }

}