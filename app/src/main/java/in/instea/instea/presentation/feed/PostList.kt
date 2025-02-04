package `in`.instea.instea.presentation.feed

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PostList(feedViewModel: FeedViewModel, navigateToProfile: (String) -> Unit, navController : NavHostController) {
    val posts = feedViewModel.posts.collectAsState(initial = emptyList()).value.reversed()

    val userList by feedViewModel.userList.collectAsState()
    var currentSwipeIndex by remember { mutableStateOf<Int?>(null) }
    if (feedViewModel.isLoading.value) {
        Column {
            repeat(15) {
                ShimmerEffect()

            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(posts) { post ->
                val index = posts.indexOf(post)
                if(post.reports.hasReport < 2){
                PostCard(
                    post = post,
//                    navigateToProfile = { navigateToProfile(post.postedByUser ?: "") },
                    userList = userList,
                    navController = navController,
                    index = index,
                    isVisible = currentSwipeIndex == index,
                    onSwiped = { currentSwipeIndex = index },
                    onClose = { currentSwipeIndex = null }
                )
                }


            }

        }
    }
}


@Composable
fun ShimmerEffect(modifier: Modifier = Modifier) {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f)
    )
    val transition = rememberInfiniteTransition()
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = FastOutLinearInEasing
            )
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )
    ShimmerGridItem(brush = brush, modifier = modifier)
}

@Composable
fun ShimmerGridItem(brush: Brush, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Spacer(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(brush)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(
                modifier = modifier
                    .height(12.dp)
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Spacer(
                modifier = modifier
                    .height(3.dp)
                    .fillMaxWidth()
            )
            Spacer(
                modifier = modifier
                    .height(12.dp)
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
            Spacer(modifier.height(1.dp))
            Spacer(
                modifier = modifier
                    .height(12.dp)
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(brush)
            )
        }
    }
}
