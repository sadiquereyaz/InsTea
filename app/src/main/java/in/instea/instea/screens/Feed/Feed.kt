import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.ktx.auth
import `in`.instea.instea.R
import `in`.instea.instea.data.FeedViewModel
import `in`.instea.instea.data.User

//import `in`.instea.instea.data.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FEED(navController: NavController, feedViewModel: FeedViewModel) {
    val user by feedViewModel.user.collectAsState()

    Column {
        FeedContent(feedViewModel)
        PostList(feedViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
private fun FeedContent(feedViewModel: FeedViewModel) {
    var textState by remember { mutableStateOf("") }
    val user by feedViewModel.user.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)


            .border(
                1.dp,
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                TextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text(text = "What do you want ask") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(fontSize = 16.sp),
                    trailingIcon = {
                        if (textState.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Filled.Send,
                                contentDescription = "send",
                                modifier = Modifier.clickable {
                                    feedViewModel.writingPostDataInDB(
                                        user,
                                        null,
                                        textState,
                                        null
                                    )
                                }
                            )
                        }
                    }
                )

            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
//                TextButton(onClick = { /* Handle Ask button click */ }) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.communication),
//                        contentDescription = "Ask logo",
//                        modifier = Modifier
//                            .size(20.dp)
//                            .padding(2.dp)
//                    )
//                    Text(
//                        text = "Ask",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    )
//                }
//                TextButton(onClick = { /* Handle Answer button click */ }) {
//                    Icon(
//                        imageVector = Icons.Outlined.Edit,
//                        contentDescription = "Answer",
//                        modifier = Modifier
//                            .size(20.dp)
//                            .padding(2.dp)
//                    )
//                    Text(
//                        text = "Answer",
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    )
//                }

                var postType by remember {
                    mutableStateOf("visible")
                }
                TextButton(onClick = {
                    if (postType.equals("visible")) {
                        postType = "Anonymous"

                    } else {
                        postType = "visible"
                    }


                }) {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = "Post logo",
                        modifier = Modifier
                            .size(20.dp)
                            .padding(2.dp)
                    )
                    Text(
                        text = postType,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
