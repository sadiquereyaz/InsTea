package `in`.instea.instea.utility

import android.graphics.pdf.PdfRenderer
import android.net.Uri
import kotlinx.coroutines.withContext

    import android.content.Context
    import android.graphics.Bitmap
    import android.graphics.Canvas
    import android.graphics.Color
import android.os.ParcelFileDescriptor

import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.async
    import kotlinx.coroutines.awaitAll
import java.io.File
import java.io.InputStream

class PdfToBitmapConvertor(
        private val context: Context
    ) {
        var renderer: PdfRenderer? = null
        fun pdfToBitmaps(inputStream: InputStream): List<Bitmap> {
            val tempFile = createTempFileFromInputStream(inputStream)
            val parcelFileDescriptor = ParcelFileDescriptor.open(tempFile, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(parcelFileDescriptor)

            val bitmaps = mutableListOf<Bitmap>()
            for (pageIndex in 0 until pdfRenderer.pageCount) {
                val page = pdfRenderer.openPage(pageIndex)
                val bitmap = Bitmap.createBitmap(
                    page.width,
                    page.height,
                    Bitmap.Config.ARGB_8888
                )
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                bitmaps.add(bitmap)
                page.close()
            }

            pdfRenderer.close()
            parcelFileDescriptor.close()

            // Clean up temporary file
            tempFile.delete()

            return bitmaps
        }

        private fun createTempFileFromInputStream(inputStream: InputStream): File {
            val tempFile = File(context.cacheDir, "temp_pdf_file.pdf")
            tempFile.outputStream().use { fileOutputStream ->
                inputStream.copyTo(fileOutputStream)
            }
            return tempFile
        }
        suspend fun pdfToBitmaps(contentUri: Uri): List<Bitmap> {
            return withContext(Dispatchers.IO) {
                renderer?.close()

                context
                    .contentResolver
                    .openFileDescriptor(contentUri, "r")
                    ?.use { descriptor ->
                        with(PdfRenderer(descriptor)) {
                            renderer = this

                            return@withContext (0 until pageCount).map { index ->
                                async {
                                    openPage(index).use { page ->
                                        val bitmap = Bitmap.createBitmap(
                                            page.width,
                                            page.height,
                                            Bitmap.Config.ARGB_8888
                                        )

                                        val canvas = Canvas(bitmap).apply {
                                            drawColor(Color.WHITE)
                                            drawBitmap(bitmap, 0f, 0f, null)
                                        }

                                        page.render(
                                            bitmap,
                                            null,
                                            null,
                                            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                                        )

                                        bitmap
                                    }
                                }
                            }.awaitAll()
                        }
                    }
                return@withContext emptyList()
            }
        }
    }
