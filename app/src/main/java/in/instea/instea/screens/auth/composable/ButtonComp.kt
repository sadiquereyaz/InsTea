package `in`.instea.instea.screens.auth.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonComp(
    modifier: Modifier = Modifier,
    value: String, onButtonClicked: () -> Unit,
    isEnabled: Boolean = true
) {
//    val isDarkMode = isSystemInDarkTheme()
//    val textColor = if (isDarkMode) Color.White else Color.Black
    Button(
        onClick = {
            onButtonClicked()
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .padding(15.dp),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(50.dp),
        enabled = isEnabled
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .shadow(
                    4.dp,
                    shape = RoundedCornerShape(50.dp)
                )
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ComposablePreview(){
    ButtonComp(value = "Sign Up", onButtonClicked = {})
}