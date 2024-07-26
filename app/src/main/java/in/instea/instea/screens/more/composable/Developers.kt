package `in`.`in`.instea.instea.screens.more.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.R
import `in`.instea.instea.ui.theme.DarkColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Developers(modifier: Modifier = Modifier) {

    val developers = listOf(
        Developer(
            "Sadique Reyaz",
            "CSE' 26",
            "https://www.linkedin.com",
            "sadique_reyaz",
            R.drawable.dp
        ),
        Developer(
            "Hammad Qadir",
            "CSE' 26",
            "https://www.linkedin.com",
            "hamad_qadir",
            R.drawable.dp
        ),
        Developer(
            "Kashif Zafar",
            "CSE' 26",
            "https://www.linkedin.com/",
            "kashif_zafar",
            R.drawable.dp
        )
    )


    LazyColumn(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
    ) {

        items(developers.size) { index ->
            DeveloperItem(developer = developers[index])
        }
    }
}

data class Developer(
    val name: String,
    val batch: String,
    val linkedinId: String,
    val linkedinUsername: String,
    val image: Int
)

@Composable
fun DeveloperItem(developer: Developer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = developer.image),
                contentDescription = null,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(75.dp)
            )
            Column(modifier = Modifier.padding(5.dp)) {
                Text(text = developer.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = developer.batch, fontSize = 16.sp)
                Linkedin(developer = developer)
                Spacer(modifier = Modifier.height(6.dp))
            }


        }
    }
}

@Composable
fun Linkedin(developer: Developer, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Row {
        Image(
            painter = painterResource(id = R.drawable.linkedin),
            modifier = Modifier
                .size(24.dp)
                .padding(end = 4.dp),
            contentDescription = null
        )
        Text(
            text = developer.linkedinUsername,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDialog = true }, // Directly trigger dialog
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                fontStyle = FontStyle.Normal,
                color = DarkColors.primary
            ),
            textDecoration = TextDecoration.Underline
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Are you sure you want to open this LinkedIn profile?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    val webIntent: Intent = Uri.parse(developer.linkedinId).let { webpage ->
                        Intent(Intent.ACTION_VIEW, webpage)

                    }
                    context.startActivity(webIntent)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("No")
                }
            }
        )
    }
}


@Preview
@Composable
private fun DevelopersPreview() {
    Developers()
}