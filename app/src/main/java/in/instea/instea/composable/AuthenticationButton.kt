package `in`.instea.instea.composable

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import `in`.instea.instea.R
import `in`.instea.instea.data.viewmodel.ERROR_TAG
import kotlinx.coroutines.launch

@Composable
fun AuthenticationButton(
//    buttonText: Int,
    onGetCredentialResponse: (Credential) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    Button(
        onClick = {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.default_web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            coroutineScope.launch {
                try {
                    Log.d(ERROR_TAG, "Requesting credential...")
                    val result = credentialManager.getCredential(
                        request = request,
                        context = context
                    )
                    Log.d(ERROR_TAG, "Credential received successfully.")
                    onGetCredentialResponse(result.credential)
                } catch (e: GetCredentialException) {
                    Log.d(ERROR_TAG, "Failed to get credential: ${e.message}")
                    Log.d(ERROR_TAG, "Cause: ${e.cause}")
                    Log.d(ERROR_TAG, "Exception Class: ${e::class.java}")
                    Log.d(ERROR_TAG, "Stack Trace: ${Log.getStackTraceString(e)}")
                } catch (e: Exception) {
                    Log.d(ERROR_TAG, "Unexpected error: ${e.message}")
                    Log.d(ERROR_TAG, "Cause: ${e.cause}")
                    Log.d(ERROR_TAG, "Exception Class: ${e::class.java}")
                    Log.d(ERROR_TAG, "Stack Trace: ${Log.getStackTraceString(e)}")
                }
            }
        },
//        colors = ButtonDefaults.buttonColors(containerColor = Purple40),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier.padding(horizontal = 16.dp).size(24.dp),
            contentDescription = "Google logo"
        )

        Text(
            text = "Sign In With Google",
            fontSize = 16.sp,
            modifier = Modifier.padding(0.dp, 6.dp)
        )
    }
}