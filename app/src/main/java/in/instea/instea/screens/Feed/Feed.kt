package `in`.instea.instea.screens.Feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import `in`.instea.instea.R


@Composable
fun FEED(navController: NavController) {
    Box {
        Column {
            Image(
                painter = painterResource(id = androidx.core.R.drawable.notify_panel_notification_icon_bg),
                contentDescription ="Profile" )

            
            Row {
                
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
    }

}