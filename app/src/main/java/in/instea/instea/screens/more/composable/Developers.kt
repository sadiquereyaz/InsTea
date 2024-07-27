package `in`.instea.instea.screens.more.composable

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import `in`.instea.instea.R

@Composable
fun Developers(modifier: Modifier = Modifier) {

    val developers = listOf(
        DeveloperModel(
            "Sadique Reyaz",
            "https://www.linkedin.com",
            R.drawable.dp
        ),
        DeveloperModel(
            "Hammad Qadir",
            "https://www.linkedin.com",
            R.drawable.dp
        ),
        DeveloperModel(
            "Kashif Zafar",
            "https://www.linkedin.com/",
            R.drawable.dp
        )
    )
    Box(
        modifier = modifier.padding(16.dp)
    ){
        LazyRow(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(developers.size) { index ->
                DeveloperItem(developer = developers[index])
            }
        }
    }
}

data class DeveloperModel(
    val name: String,
    val userId: String,
    val image: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperItem(developer: DeveloperModel) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .height(height = 190.dp)
            .width(width = 170.dp),
        onClick = {
            showDialog = true
        },

    ) {
        Column(
            modifier = Modifier
//                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = developer.image),
                contentDescription = null,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(100.dp)
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = developer.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = "CSE'24 JMI", fontSize = 16.sp)
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirmation") },
                    text = { Text("Are you sure you want to open this LinkedIn profile?") },
                    confirmButton = {
                        Button(onClick = {
                            showDialog = false
                            val webIntent: Intent = Uri.parse(developer.userId).let { webpage ->
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
    }
}

@Composable
fun Linkedin(developer: DeveloperModel, modifier: Modifier = Modifier) {
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
        /*Text(
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
        )*/
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmation") },
            text = { Text("Are you sure you want to open this LinkedIn profile?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    val webIntent: Intent = Uri.parse(developer.userId).let { webpage ->
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