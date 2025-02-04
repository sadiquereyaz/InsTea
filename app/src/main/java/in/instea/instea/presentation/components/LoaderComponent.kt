package `in`.instea.instea.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import `in`.instea.instea.R

@Composable
fun LoaderComponent(modifier: Modifier = Modifier, iterationTime :Int = LottieConstants.IterateForever) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.hand_loader))
    LottieAnimation(
        composition = composition,
        iterations = iterationTime,
    )
}