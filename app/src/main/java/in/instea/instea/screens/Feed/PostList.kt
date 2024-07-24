package `in`.instea.instea.ui

import PostCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import `in`.instea.instea.data.FeedViewModel

@Composable
fun PostList(feedViewModel: FeedViewModel, navigateToProfile: (String) -> Unit) {
    val posts = feedViewModel.posts.collectAsState(initial = emptyList()).value.reversed()

    if (feedViewModel.isLoading.value) {
        Column {
            repeat(8) {
                ShimmerEffect()
            }
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(posts) { post ->
                PostCard(
                    post = post,
                    navigateToProfile = { navigateToProfile(post.postedByUser ?: "") }
                )
            }
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
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
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(brush)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(
                modifier = modifier
                    .height(12.dp)
                    .fillMaxWidth(0.7f)
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
        }
    }
}
