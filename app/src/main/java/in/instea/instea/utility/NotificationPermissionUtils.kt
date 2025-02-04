package `in`.instea.instea.utility

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat

@Composable
fun rememberNotificationPermissionLauncher(
    context: Context,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
): ActivityResultLauncher<String> {
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
            Toast.makeText(
                context,
                "Notification permission required. Go to Phone Setting > App > Instea > Allow Notification Permission",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

fun checkAndRequestNotificationPermission(
    context: Context,
    requestLauncher: ActivityResultLauncher<String>,
    onPermissionGranted: () -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            onPermissionGranted()
        } else {
            requestLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    } else {
        // For older versions, no permission needed
        onPermissionGranted()
    }
}


