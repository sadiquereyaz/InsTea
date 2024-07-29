package `in`.instea.instea.screens.auth.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonComp(
    modifier: Modifier = Modifier,
    text: String,
    onButtonClicked: () -> Unit,
    isEnabled: Boolean = true,
    isLoading: Boolean = false
) {
    if (!isLoading) {
        Button(
            onClick = {
                onButtonClicked()
            },
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(),
            shape = RoundedCornerShape(50),
            enabled = isEnabled
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
//                .heightIn(48.dp)
                    .shadow(
                        5.dp,
                        shape = RoundedCornerShape(50)
                    )
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    } else {
        LinearProgressIndicator(modifier = modifier.fillMaxWidth())
    }
}

@Composable
@Preview(showBackground = true)
fun ComposablePreview() {
//    ButtonComp(text = "Sign Up", onButtonClicked = {})
}