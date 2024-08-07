package `in`.`in`.instea.instea.screens.more.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Card
import `in`.instea.instea.R
import `in`.instea.instea.screens.more.composable.DeveloperItem
import `in`.instea.instea.screens.profile.OtherProfileScreen

@Preview
@Composable
fun classmateList(modifier: Modifier = Modifier) {
    val classmates = listOf("")
    Box(
        modifier = modifier.padding(16.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(classmates.size) { index ->
                classmate()
            }
        }
    }
}

@Preview
@Composable
private fun classmate() {
    Card(onClick = { /*TODO*/ }) {
        Row (verticalAlignment = Alignment.CenterVertically){
            Image(
                painter = painterResource(id = R.drawable.dp),
                contentDescription = null,
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(100.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text ="Classmate",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
