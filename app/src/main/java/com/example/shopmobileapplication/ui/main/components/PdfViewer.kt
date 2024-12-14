package com.example.shopmobileapplication.ui.main.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import java.io.File
import java.io.FileOutputStream

@Composable
fun PdfViewer(fileName: String) {
    val context = LocalContext.current
    val renderer = remember { loadPdf(context, fileName) }

    var currentPage by remember { mutableStateOf(0) }

    renderer?.let {
        val page = it.openPage(currentPage)
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()

        Surface(modifier = Modifier.fillMaxSize()) {
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    if (currentPage > 0) {
                        currentPage--
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = whiteGreyBackground),
                enabled = currentPage > 0
            ) {
                BasicText(stringResource(R.string.previous))
            }
            Button(
                onClick = {
                    if (currentPage < it.pageCount - 1) {
                        currentPage++
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = whiteGreyBackground),
                enabled = currentPage < it.pageCount - 1
            ) {
                BasicText(stringResource(R.string.nexts))
            }
        }
    }
}

private fun loadPdf(context: Context, fileName: String): PdfRenderer? {
    return try {
        val pdfFile = File(context.cacheDir, fileName)
        // Копирование файла из assets в кэш
        context.assets.open(fileName).use { inputStream ->
            FileOutputStream(pdfFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        val fileDescriptor: ParcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
        PdfRenderer(fileDescriptor)
    } catch (e: Exception) {
        Log.e("PdfViewer", "Error loading PDF", e)
        null
    }
}