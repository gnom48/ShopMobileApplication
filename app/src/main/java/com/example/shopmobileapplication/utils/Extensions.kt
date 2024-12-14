package com.example.shopmobileapplication.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.IOException
import java.io.InputStream

@Throws(IOException::class)
fun Uri.uriToBytesArray(context: Context): ByteArray? {
    return context.contentResolver.openInputStream(this)?.use { it.buffered().readBytes() }
}

fun getAllSizesRus(start: Double, end: Double, step: Double): List<Double> {
    if (start < 0 || end < 0) {
        return emptyList()
    }
    if (step <= 0.0 || step >= end) {
        return listOf(start)
    }
    return generateSequence(start) { current ->
        if (current + step <= end) current + step else null
    }.toList()
}

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}