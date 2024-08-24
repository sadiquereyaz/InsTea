import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.data.datamodel.Message
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.FeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InboxScreen(
    userId: String,
    chatViewModel: ChatviewModel = viewModel(factory = AppViewModelProvider.Factory),
    feedViewModel: FeedViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    var receiverRoom by remember { mutableStateOf(userId + feedViewModel.currentuser) }
    var senderRoom by remember { mutableStateOf(feedViewModel.currentuser + userId) }


    val chatList =
        chatViewModel.chatUiState.collectAsState(initial = emptyList()).value // Observe the flow directly

    var textState by remember { mutableStateOf("") }
    val scrollState = rememberScrollState(0)
    LaunchedEffect(senderRoom, receiverRoom) {
        chatViewModel.getChats(senderRoom, receiverRoom)
    }


    Column(
        modifier = Modifier

            .fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .imePadding()
        ) {
            items(chatList) { message -> // Access messages from chatList.chatstate
                if (message.senderId == feedViewModel.currentuser) {
                    SenderTextField(message)
                } else {
                    ReceiverTextField(message)
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp), // TextField takes available space
                    value = textState,
                    onValueChange = { textState = it },
                    placeholder = { Text("Enter message") },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.onSecondary,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    shape = RoundedCornerShape(50.dp),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    trailingIcon = {
                        if (textState.isNotEmpty()) {
                            Icon(
                                modifier = Modifier
                                    .size(30.dp)
                                    .padding(1.dp)
                                    .clickable {
                                        textState = reduceMultipleSpaces(textState)
                                        if (textState.isNotEmpty()) {
                                            chatViewModel.insertMessages(
                                                message = Message(
                                                    message = textState,

                                                    senderId = feedViewModel.currentuser!!,
                                                ),
                                                receiverRoom = userId + feedViewModel.currentuser,
                                                senderRoom = feedViewModel.currentuser + userId
                                            )
                                            Log.d(
                                                "rooms",
                                                "InboxScreen: $senderRoom , $receiverRoom"
                                            )
                                            textState = ""
                                        }
                                    },
                                imageVector = Icons.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White
                            )
                        }
                    }
                )


            }
//                Card(
//
//                    modifier = Modifier
//                        .align(Alignment.CenterEnd)
//
//                        .size(50.dp)
//                        .padding(4.dp),
//                    shape = RoundedCornerShape(100.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.primary
//                    )
//                ) {
//                    Icon(
//                        modifier = Modifier
//                            .size(30.dp)
//                            .align(Alignment.CenterHorizontally)
//                            .padding(1.dp)
//                            .clickable {
//                                textState = reduceMultipleSpaces(textState)
//                                if(textState.isNotEmpty()){
//                                chatViewModel.insertMessages(
//                                    message = `in`.instea.instea.data.datamodel.Message(
//                                        message = textState,
//                                        senderId = feedViewModel.currentuser!!,
//                                    ),
//                                    receiverRoom = userId + feedViewModel.currentuser,
//                                    senderRoom = feedViewModel.currentuser + userId
//                                )
//                                Log.d("rooms", "InboxScreen: $senderRoom , $receiverRoom")
//                                textState = ""
//                                }
//                            },
//                        imageVector = Icons.Filled.Send,
//                        contentDescription = "Send",
//                        tint = Color.White
//                    )
//                }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SenderTextField(message: Message) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentWidth()
                    .fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.Bottom // Align items to bottom in Row
            ) {
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f),
                    text = message.message,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = message.timeStamp.formateForMessage(),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverTextField(message: Message) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Card(
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .wrapContentWidth()
                    .fillMaxWidth(0.5f),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.Bottom // Align items to bottom in Row
            ) {
                Text(
                    modifier = Modifier
                        .padding(5.dp)
                        .weight(1f),
                    text = message.message,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = message.timeStamp.formateForMessage(),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}


//data class TimeOfMessage(
//    var date: Date = Date(), // No-argument constructor required by Firebase
//) {
//    fun format(): String {
//        val dateFormat = SimpleDateFormat("HH:MM", Locale.getDefault())
//        return dateFormat.format(this.date)
//    }
//
//
//}