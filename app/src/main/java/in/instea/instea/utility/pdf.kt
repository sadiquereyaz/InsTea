package `in`.instea.instea.utility

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

fun convertGoogleDriveUrlToDirectDownload(url: String): String? {
//    val regex = """https://drive\.google\.com/file/d/([^/]+)/view""".toRegex()
    val regex = """https://drive\.google\.com/file/d/([^/]+)/view""".toRegex()
    val matchResult = regex.find(url)
    return matchResult?.groupValues?.get(1)?.let { fileId ->
        "https://drive.usercontent.google.com/uc?id=$fileId&export=download"
    }
}

fun extractGoogleDriveFileId(url: String): String? {
    val regex = """drive\.google\.com/file/d/([^/]+)/""".toRegex()
    return "https://drive.usercontent.google.com/uc?id=${regex.find(url)?.groupValues?.get(1)}&export=download"
}

suspend fun fetchAndRenderPdf(url: String, converter: PdfToBitmapConvertor): List<Bitmap> {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    return withContext(Dispatchers.IO) {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Failed to fetch PDF: ${response.message}")
            response.body?.byteStream()?.let { inputStream ->
                converter.pdfToBitmaps(inputStream)
            } ?: emptyList()
        }
    }
}