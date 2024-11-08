package com.example.shopmobileapplication.utils

import android.content.Context
import android.net.Uri
import java.io.IOException

@Throws(IOException::class)
fun Uri.uriToBytesArray(context: Context): ByteArray? {
    return context.contentResolver.openInputStream(this)?.use { it.buffered().readBytes() }
}

fun getAllSizesRus(start: Double, end: Double, step: Double): List<Double> {
    return generateSequence(start) { current ->
        if (current + step <= end) current + step else null
    }.toList()
}
