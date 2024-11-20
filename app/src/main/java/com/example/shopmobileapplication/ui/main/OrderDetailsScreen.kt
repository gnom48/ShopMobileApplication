package com.example.shopmobileapplication.ui.main

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.OrderStatus
import com.example.shopmobileapplication.data.generateBarcode
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.order.OrderRepositoryImpl
import com.example.shopmobileapplication.ui.main.components.CustomTopAppBar
import com.example.shopmobileapplication.ui.main.components.DeliveryContactInfo
import com.example.shopmobileapplication.ui.main.components.OrderItem
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.OrderViewModel
import com.example.shopmobileapplication.ui.viewmodel.OrderViewModelFactory
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.google.gson.Gson
import kotlinx.serialization.SerialName
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun OrderDetailsScreen(
    orderId: String, productSizeId: Int,
    navController: NavController?,
    orderViewModel: OrderViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = OrderViewModelFactory(
        OrderRepositoryImpl(SupabaseClient.client)
    )
    )
) {
    LaunchedEffect(Unit) {
        orderViewModel.getOrdersDetails(UserViewModel.currentUser, orderId)
    }

    var orderStatus by remember { mutableStateOf<String?>(null) }
    var barcodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var barcodeWidth by remember { mutableIntStateOf(500) }

    LaunchedEffect(orderViewModel.orderDetails) {
        if (orderViewModel.orderDetails.isNotEmpty()) {
            orderViewModel.orderDetails.firstOrNull { it.productExampleId == productSizeId }?.let {
                orderStatus = it.status
                when (orderStatus) {
                    OrderStatus.ORDER_DELIVERED -> {
                        barcodeBitmap = generateBarcode(
                            Gson().toJson(object {
                                @SerialName("order_id") val orderId = it.orderId
                                @SerialName("product_example_id") val productExampleId = it.productExampleId
                            }),
                            barcodeWidth,
                            200
                        )
                    }
                    OrderStatus.ORDER_CONFIRMED -> { }
                    OrderStatus.ORDER_TAKEN -> { }
                    else -> { }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteGreyBackground)
    ) {
        CustomTopAppBar(
            title = orderId.subSequence(0..8).toString() + "...",
            onBackButtonClick = {
                navController?.popBackStack()
            },
            actionIconButton = { Spacer(modifier = Modifier.size(36.dp)) }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(10.dp)
        ) {
            orderViewModel.orderDetails.firstOrNull { it.orderId == orderId && it.productExampleId == productSizeId }?.let {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    val date = LocalDateTime.ofEpochSecond(it.orderDateTime, 0, ZoneOffset.UTC).toLocalDate()
                    Text(
                        text = if (date == LocalDate.now()) stringResource(R.string.today) else date.toString(),
                        style = ralewaySubtitle,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.LightGray
                    )
                }

                OrderItem(modifier = Modifier.fillMaxWidth(), data = it) { }

                DeliveryContactInfo(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .onSizeChanged { intSize: IntSize ->
                            barcodeWidth = intSize.width
                        },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (barcodeBitmap == null) {
                            Text(
                                modifier = Modifier.padding(10.dp),
                                text = orderStatus ?: stringResource(R.string.status_not_fuzzy),
                                style = ralewaySubtitle,
                                overflow = TextOverflow.Ellipsis
                            )
                        } else {
                            Image(
                                modifier = Modifier.padding(10.dp),
                                bitmap = barcodeBitmap!!.asImageBitmap(),
                                contentDescription = "Barcode",
                            )
                        }
                    }
                }
            }
        }
    }
}