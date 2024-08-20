package `in`.instea.instea.screens.more.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import `in`.instea.instea.data.viewmodel.AuthViewModel

@Composable
fun AccountComp(
    modifier: Modifier = Modifier,
    logout: () -> Unit,
    deleteAccount: () -> Unit,
) {
    Box(modifier = modifier.padding(10.dp)) {
        Column {
            // sign out
            TextButton(onClick = {
                logout()
//                navigateToAuth()
            }) {
                Text(text = "Sign Out")
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.PersonRemove,
                    contentDescription = "Logout",
                    modifier = Modifier.size(20.dp)
                )
            }
            TextButton(onClick = {
                deleteAccount()
//                navigateToAuth()
            }) {
                Text(text = "Delete Account", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.HeartBroken,
                    tint = MaterialTheme.colorScheme.error,
                    contentDescription = "Logout",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}