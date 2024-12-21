package com.example.shopmobileapplication.ui.main.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.OrderDetailsView
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.SupabaseViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Composable
@Preview
fun OrderItemPreview() {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(10.dp)) {
        items(2) {
            Box(modifier = Modifier.fillMaxSize().padding(vertical = 10.dp)) {
                OrderItem(data = OrderDetailsView(
                    orderId = UUID.randomUUID().toString(),
                    userId = "user123",
                    quantityInOrder = 2,
                    productExampleId = 101,
                    productId = "prod567",
                    orderDateTime = 1633072861,
                    name = "Product Name",
                    price = 99.99,
                    image = "https://jordan-nike.ru/image/cache/catalog/%20123/111111/669eb400e4213c00012dee1c-1000x760.png",
                    sizeRus = 42.0,
                    color = "Red",
                    status = "Оформлен"
                )) {

                }
            }
        }
    }
}

@Composable
fun OrderItem(
    modifier: Modifier = Modifier.fillMaxSize(),
    data: OrderDetailsView,
    supabaseViewModel: SupabaseViewModel = viewModel(),
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    Surface(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteGreyBackground)
                .clickable {
                    onClick()
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(contentColor = Color.White, containerColor = Color.White),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .padding(10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    var imageSignedUrl by remember { mutableStateOf<String?>(null) }
                    supabaseViewModel.getUrlFromPublicBucketCallback(fileName = data.image) { url: String? ->
                        imageSignedUrl = url
                    }
                    AsyncImage(
                        model = imageSignedUrl,
                        contentDescription = "image",
                        error = painterResource(id = R.drawable.default_shoes),
                        modifier = Modifier
                            .width(100.dp)
                            .background(
                                shape = RoundedCornerShape(13.dp),
                                color = whiteGreyBackground
                            )
                            .aspectRatio(1f / 1f)
                            .padding(5.dp),
                        contentScale = ContentScale.FillWidth)
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "№" + data.orderId.subSequence(0..8) + "...",
                            maxLines = 1,
                            softWrap = false,
                            style = ralewaySubtitle,
                            overflow = TextOverflow.Clip,
                            color = blueGradientStart,
                            modifier = Modifier.clickable {
                                val clip = ClipData.newPlainText("Номер заказа", data.orderId)
                                clipboardManager.setPrimaryClip(clip)
                            }
                        )
                        val time = LocalDateTime.ofEpochSecond(data.orderDateTime, 0, ZoneOffset.UTC).toLocalTime().withSecond(0).withNano(0)
                        Text(
                            text = time.toString(),
                            style = ralewaySubtitle,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.LightGray
                        )
                    }
                    Text(
                        text = data.name,
                        style = ralewaySubtitle,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(R.string.size) + data.sizeRus.toString(),
                        style = ralewayRegular,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            modifier = Modifier.padding(end = 30.dp),
                            text = "₽" + (data.price * data.quantityInOrder).toString(),
                            style = ralewayRegular
                        )
                        Text(
                            text = "₽" + 0.toString(),
                            style = ralewayRegular,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }
    }
}