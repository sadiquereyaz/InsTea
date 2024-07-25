import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.R
import `in`.instea.instea.data.datamodel.DateAndHour
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InboxScreen() {
    SenderTextField()
    ReceiverTextField()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SenderTextField() {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                TextField(
                    modifier = Modifier
                        .padding(6.dp)
                        .widthIn(min = 50.dp, max = 200.dp),
                    maxLines = 6,
                    value = "Inbox qqqqqwweerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr",
                    onValueChange = {},
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.padding(end = 10.dp, bottom = 2.dp)
                ) {
                    Text(
                        text = Time().format(),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 4.dp, end = 4.dp)
                    )
                }
            }
        }
    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverTextField() {

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )

        ) {
            Row(verticalAlignment = Alignment.Bottom) {
                TextField(
                    modifier = Modifier
                        .padding(6.dp)
                        .widthIn(min = 50.dp, max = 200.dp),
                    maxLines = 6,
                    value = "Inbox qqqqqwweerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr",
                    onValueChange = {},
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedTextColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.padding(end = 10.dp, bottom = 2.dp)
                ) {
                    Text(
                        text = Time().format(),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(bottom = 4.dp, end = 4.dp)
                    )
                }
            }
        }
    }
}

data class Time(
    var date: Date = Date(), // No-argument constructor required by Firebase
) {
    fun format(): String {
        val dateFormat = SimpleDateFormat("HH:MM", Locale.getDefault())
        return dateFormat.format(this.date)
    }


}