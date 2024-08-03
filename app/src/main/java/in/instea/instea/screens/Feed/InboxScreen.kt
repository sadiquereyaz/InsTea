import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.modifier.ModifierLocalReadScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.DateAndHour
import `in`.instea.instea.data.viewmodel.AppViewModelProvider
import `in`.instea.instea.data.viewmodel.FeedViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
                .fillMaxWidth()
                .weight(1f)
                .padding(8.dp)

        ) {
            items(chatList) { message ->
                if (message.senderId == feedViewModel.currentuser) {
                    SenderTextField(message)
                } else {
                    ReceiverTextField(message)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp),
                value = textState,
                onValueChange = { textState = it },
                placeholder = { Text("Enter message") },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(50.dp),
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )

            Card(

                modifier = Modifier
                    .size(50.dp)
                    .padding(6.dp),
                shape = RoundedCornerShape(100.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(1.dp)
                        .clickable {
                            val currentTimeStamp = System.currentTimeMillis()
                            textState = reduceMultipleSpaces(textState)
                            chatViewModel.insertMessages(
                                message = Message(
                                    message = textState,
                                    senderId = feedViewModel.currentuser!!,
                                    timeStamp = DateAndHour()
                                ),
                                receiverRoom = userId + feedViewModel.currentuser,
                                senderRoom = feedViewModel.currentuser + userId
                            )
                            Log.d("rooms", "InboxScreen: $senderRoom , $receiverRoom")
                            textState = ""
                        },
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SenderTextField(message: Message) {
    val senderLight = Color("#2FCC59".toColorInt())
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.BottomEnd)
            .padding(5.dp, end = 10.dp)
           ,
        contentAlignment = Alignment.CenterEnd
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp), // Apply rounded corners
            color = senderLight, // Set the background color
            modifier = Modifier.padding(4.dp) // Add padding around the content
        ){
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
            modifier= Modifier
               .widthIn(min = 60.dp,max =(screenWidth/2))

               .background(senderLight)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 8.dp, end = 10.dp, top = 8.dp)
                ,
                text = message.message,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,

                ),

                overflow = TextOverflow.Clip
            )

            Text(
                text = message.timeStamp.formateForMessage(),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontSize = 8.sp,
                modifier = Modifier.align(Alignment.End).padding(start=8.dp,bottom = 2.dp,end=4.dp)

                )
        }}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverTextField(message: Message) {
    var receiver = remember {
        mutableStateOf(Color("#278EFF".toColorInt()))
    }
//    val receiverDark = Color("#26252A".toColorInt())
    if(isSystemInDarkTheme()){
        receiver.value = Color("#26252A".toColorInt())
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .wrapContentSize(Alignment.BottomStart)
            .padding(5.dp, end = 10.dp)
        ,
        contentAlignment = Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = receiver.value,
            modifier = Modifier.padding(4.dp)
            ){
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom,
                modifier= Modifier
                    .widthIn(min = 60.dp)
                    .background(receiver.value)
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 4.dp, end = 10.dp, top = 5.dp)
                    ,
                    text = message.message,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,

                        ),

                    overflow = TextOverflow.Clip
                )

                Text(
                    text = message.timeStamp.formateForMessage(),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    fontSize = 8.sp,
                    modifier = Modifier.align(Alignment.End).padding(start = 8.dp,bottom = 2.dp,end=4.dp)

                )
            }}
    }
}


data class TimeOfMessage(
    var date: Date = Date(), // No-argument constructor required by Firebase
) {
    fun format(): String {
        val dateFormat = SimpleDateFormat("HH:MM", Locale.getDefault())
        return dateFormat.format(this.date)
    }

}