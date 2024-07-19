package `in`.instea.instea.screens.auth.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import `in`.instea.instea.ui.theme.DarkColors

@Composable
fun ScreenChangeText(
    modifier: Modifier = Modifier
        .fillMaxWidth(),
    navController: NavController
) {
    val isDarkMode = isSystemInDarkTheme()
    val textColor = if (isDarkMode) Color.White else Color.Black
    val initialtxt = "Already have an account ? "
    val logintxt = "Login"
    val annotatedString = buildAnnotatedString {
        append(initialtxt)
        withStyle(style = SpanStyle(color = DarkColors.primary)) {
            pushStringAnnotation(
                tag = logintxt,
                annotation = logintxt
            )
            append(logintxt)
        }
    }
    ClickableText(text = annotatedString,
                  style = TextStyle(
                      color = textColor
                  ),
                  modifier = Modifier.padding(16.dp),
                  onClick = { offset ->
                      annotatedString.getStringAnnotations(offset, offset)
                          .firstOrNull()?.also { span ->
                              if (span.item == logintxt) {
                                  navController.navigate("Login")


                              }

                          }
                  })
}