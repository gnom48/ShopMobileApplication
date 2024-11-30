package com.example.shopmobileapplication.ui.main.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Preview
@Composable
fun BarCodeFullScreenPreview() {
    BarCodeFullScreen(null) {}
}
@Composable
fun BarCodeFullScreen(
    barcodeBitmap: Bitmap?,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = {
        onClose()
    }) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp, horizontal = 10.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    bitmap = barcodeBitmap!!.asImageBitmap(),
                    contentDescription = "Barcode",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}