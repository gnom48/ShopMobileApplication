package com.example.shopmobileapplication.utils

import android.content.Context
import android.net.Uri
import java.io.IOException
import kotlin.jvm.Throws

@Throws(IOException::class)
fun Uri.uriToBytesArray(context: Context): ByteArray? {
    return context.contentResolver.openInputStream(this)?.use { it.buffered().readBytes() }
}