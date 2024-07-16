import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import `in`.instea.instea.R
import `in`.instea.instea.data.FeedViewModel

//import `in`.instea.instea.data.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FEED(navController: NavController, feedViewModel: FeedViewModel) {
    val user by feedViewModel.user.collectAsState()

    Column {
//        FeedContent(feedViewModel)
        PostList(feedViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun FeedContent(feedViewModel: FeedViewModel) {
    var textState by remember { mutableStateOf("") }
    val user by feedViewModel.user.collectAsState()
    var postType by remember { mutableStateOf("visible") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)


            .border(
                1.dp, MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)){
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription ="profile",
                    modifier = Modifier.size(30.dp)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                OutlinedTextField(value = textState,
                    onValueChange = { textState = it },
                    label = { Text(text = "What do you want ask") },
                    modifier = Modifier
                        .scrollable(
                            state = ScrollableState { 0f },
                            orientation = Orientation.Vertical
                        )
                        .fillMaxWidth()
                        .padding(3.dp)
                        .heightIn(min = 50.dp, max = 100.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = TextStyle(fontSize = 16.sp),
                    maxLines = 5,
                    trailingIcon = {
                        if (textState.isNotEmpty()) {
                            Icon(imageVector = Icons.Filled.Send,
                                contentDescription = "send",
                                modifier = Modifier.clickable {
                                    feedViewModel.writingPostDataInDB(
                                        user,
                                        null,
                                        textState,
                                        null,
                                        postType
                                    )
                                    textState = ""
                                }
                            )
                        }
                    })

            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
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
                        text = postType, fontWeight = FontWeight.Bold, fontSize = 16.sp
                    )
                }
            }
        }
    }
}
