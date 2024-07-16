import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import `in`.instea.instea.data.FeedViewModel

//import androidx.lifecycle.viewmodel.CreationExtras.Empty.map

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PostList(feedViewModel: FeedViewModel) {
    var list =  GetPostData()
    Log.d("listsize", "PostCard: ${list.size}")
    LazyColumn {
      items(list){index->
          PostCard(
             index
          )

      }
    }

}