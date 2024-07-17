import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import `in`.instea.instea.data.FeedViewModel
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

//import androidx.lifecycle.viewmodel.CreationExtras.Empty.map


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PostList(feedViewModel: FeedViewModel) {
    val posts by feedViewModel.feedUiState.value.posts.collectAsState(initial = emptyList())

    LazyColumn {
        items(posts) { post -> PostCard(post = post) }
    }

}