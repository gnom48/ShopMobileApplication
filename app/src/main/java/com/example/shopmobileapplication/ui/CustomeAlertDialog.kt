package com.example.shopmobileapplication.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewayOnButton
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle

@Composable
@Preview
fun CustomAlertDialogPreview() {
    CustomAlertDialog(imageResId = R.drawable.message_icon, title = "Заголовок", message = "Сообщение с важной информацией", onDismiss = { }, onConfirm = { })
}

@Composable
fun CustomAlertDialog(
    imageResId: Int,
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(44.dp, 44.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = title,
                    style = ralewaySubtitle,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = message,
                    style = ralewaySubregular,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                androidx.compose.material3.Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(10.dp, 20.dp)
                        .clip(RoundedCornerShape(14.dp)),
                    contentPadding = PaddingValues(10.dp, 10.dp),
                    colors = ButtonDefaults.buttonColors(blueGradientStart),
                    onClick = {
                        onConfirm()
                        onDismiss()
                    }
                ) {
                    androidx.compose.material3.Text(
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = "Ок",
                        style = ralewayOnButton
                    )
                }
            }
        }
    }
}
