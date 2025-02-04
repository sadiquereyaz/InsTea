package `in`.instea.instea.screens.more.composable

import `in`.instea.instea.utility.UserInfoUtil
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import `in`.instea.instea.R
import `in`.instea.instea.presentation.more.MoreUiState
import `in`.instea.instea.presentation.more.classmate


@Composable
fun classmateList(
    navigatetoOtherProfile: (String) -> Unit,
    uiState: MoreUiState,
    modifier: Modifier = Modifier
) {
    val classmates = uiState.classmateList
    Box(
        modifier = modifier.padding(16.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(classmates) { classmate ->
                classmate(navigatetoOtherProfile, classmate)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun classmate(navigatetoOtherProfile: (String) -> Unit, classmate: classmate) {
    Card(onClick = {
        navigatetoOtherProfile(classmate.userId)
    },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
    )
     {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = UserInfoUtil.getUserDpId(classmate.dpId),
                modifier = Modifier
                    .padding(3.dp)
                    .size(50.dp)
                    .border(
                        width = 1.dp, brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        ),
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                placeholder = painterResource(id= R.drawable.logo),
                error = painterResource(id = R.drawable.dp),
                contentDescription = "Profile"
            )
            
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = classmate.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
