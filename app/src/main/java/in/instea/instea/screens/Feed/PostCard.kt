import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.R

//@Preview
//@Composable
//private fun p() {
//    PostCard(
//        profilePic = R.drawable.ic_launcher_foreground,
//        name = "Hammad", location = "Delhi", content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
//    )
//}

@Composable
fun PostCard(
    profilePic: Int?,
    name: String?,
    location: String?,
    content: String?,
    postImage: Int?,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }


    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        modifier = Modifier.padding(8.dp)
    ) {

        Box(modifier = Modifier.padding(8.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = if (profilePic != null) profilePic else R.drawable.ic_launcher_foreground),
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Black),
                        contentDescription = "Profile"
                    )
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(text = name!!, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(text = location!!, fontSize = 12.sp, fontWeight = FontWeight.Light)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


                Column {
                    val displayText = if (isExpanded) content!! else content?.take(50)
                    Text(
                        text = displayText!!,
                        modifier = Modifier.padding(2.dp)
                    )
                    TextButton(
                        onClick = {
                            isExpanded = !isExpanded
                        },

                        ) {
                        Text(
                            text = if (isExpanded) "Show Less" else "Read More",
                            fontSize = 12.sp
                        )
                    }
                }
                if (postImage != null) {
                    Image(
                        painter = painterResource(id = postImage),
                        contentDescription = "Post Image"

                    )
                }
                Box(contentAlignment = Alignment.BottomStart) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BottomButtons()
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Unspecified
                            ),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chatbubble),
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )

                        }
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Unspecified
                            ),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Share,
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )

                        }

                        IconButton(
                            onClick = { }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.MoreVert,
                                contentDescription = "",
                                tint = Color.Black
                            )
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun BottomButtons() {
    val isLiked = rememberSaveable {
        mutableStateOf(false)
    }
    IconButton(
        onClick = { isLiked.value = !isLiked.value },

        modifier = Modifier.padding(8.dp)
    ) {
        Image(
            imageVector = if (isLiked.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = "vote"
        )
    }
}