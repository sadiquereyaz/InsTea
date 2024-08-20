package `in`.instea.instea.screens.Feed

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
                UserListCard(user)
            }
        }
    }
}

@Composable
fun UserListCard(user: User) {
//    Log.d("UserList", "`in`.instea.instea.screens.Feed.UserListCard: ${user}")

    Box(modifier = Modifier.fillMaxWidth()) {
        Row(

            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(6.dp)
                .fillMaxWidth()
                .height(70.dp)
        ) {
            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "profile",
                modifier = Modifier
                    .padding(3.dp)
                    .size(50.dp)
                    .border(
                        width = 1.dp, brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape)

            )
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.padding(3.dp).fillMaxWidth(0.8f)
            ) {

                Text(
                    text = "user.username" ?: "Unknown User",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = "user.dept" ?: "Unknown Department",
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp
                )
            }
            Icon(
                imageVector = Icons.Filled.MoreHoriz, contentDescription = "block"
            )

        }
    }


}