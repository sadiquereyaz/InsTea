package `in`.instea.instea.screens.Feed

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.User
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.FeedViewModel

@Composable
fun UserListScreen(feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val userList by feedViewModel.userList.collectAsState()
    val currentUser = userList.find { it.userId == feedViewModel.currentuser }
    val chatPartners = currentUser?.chatPartners ?: emptyList()
    Log.d("UserList", "chatPartners: $chatPartners")
    LazyColumn {
        items(chatPartners) { chatPartner ->

            val user = userList.find { it.userId == chatPartner }
//            Log.d("UserList", "user: $user")

            if (user != null) {
                UserListCard( user)
            }
        }
    }
}

@Composable
fun UserListCard( user: User) {
//    Log.d("UserList", "`in`.instea.instea.screens.Feed.UserListCard: ${user}")
    Card(elevation = CardDefaults.cardElevation(15.dp)) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(text = user.username ?: "Unknown User")
            Text(text = user.dept ?: "Unknown Department")
        }
    }
}