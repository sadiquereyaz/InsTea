package `in`.instea.instea.screens.feed

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.FeedViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavHostController,
    feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val users = feedViewModel.usersByQuery.collectAsState().value
    var query by remember { mutableStateOf("") }



    users.sortedBy { it.username }
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    if (query.length <= 15) {
                        feedViewModel.SearchUser(query)

                    } else {
                        query = query.take(15)
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = Color.Transparent,

                    ),
                maxLines = 1,
                shape = RoundedCornerShape(20.dp),
                label = { Text("Search by username, dept or email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
        if (users.isEmpty()) {
            item { Text("No result to Show ...", modifier = Modifier.padding(8.dp)) }
        } else {
            items(users) { user ->
                UserListCard(user = user, navController)
            }
        }
    }
}