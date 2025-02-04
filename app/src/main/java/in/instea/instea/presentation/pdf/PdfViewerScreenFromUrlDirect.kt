package `in`.instea.instea.presentation.pdf

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import `in`.instea.instea.utility.PdfToBitmapConvertor
import `in`.instea.instea.utility.convertGoogleDriveUrlToDirectDownload
import `in`.instea.instea.utility.fetchAndRenderPdf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun PdfViewerScreenFromUrlDirect(
    modifier: Modifier = Modifier,
    pdfDriveUrl: String
) {
    val context = LocalContext.current
    val pdfBitmapConverter = remember { PdfToBitmapConvertor(context) }
    var downloadableUrl by remember {
        mutableStateOf(
            convertGoogleDriveUrlToDirectDownload(
                pdfDriveUrl
            ) ?: "www.google.com"
        )
    }
    var renderedPages by remember { mutableStateOf<List<Bitmap>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var show by remember { mutableStateOf(true) }
    var error: String? by remember { mutableStateOf(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (show) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = downloadableUrl,
                onValueChange = { downloadableUrl = it },
                label = { Text("Enter PDF URL") },
                trailingIcon = {
                    if (downloadableUrl.isNotEmpty()) {
                        IconButton(onClick = { downloadableUrl = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                }
            )

            Button(
                onClick = {
                    show = false
                    scope.launch {
                        isLoading = true
                        try {
                            renderedPages = fetchAndRenderPdf(downloadableUrl, pdfBitmapConverter)
                        } catch (e: Exception) {
                            error = e.message
                            e.printStackTrace()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Render PDF")
            }
        }
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading PDF...")
            }
        } else if (error != null) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
                Text(error ?: "an error occurred!")
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(renderedPages) { index, page ->
                    PdfPage(page = page)
                }
            }
        }
    }
}


