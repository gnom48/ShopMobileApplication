package com.example.shopmobileapplication.ui.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Order
import com.example.shopmobileapplication.data.bucket.BucketRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.order.OrderRepositoryImpl
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.main.components.CustomAlertDialog
import com.example.shopmobileapplication.ui.main.components.CustomTopAppBar
import com.example.shopmobileapplication.ui.main.components.DeliveryContactInfo
import com.example.shopmobileapplication.ui.main.components.OrderPrice
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.BucketViewModel
import com.example.shopmobileapplication.ui.viewmodel.BucketViewModelFactory
import com.example.shopmobileapplication.ui.viewmodel.OrderViewModel
import com.example.shopmobileapplication.ui.viewmodel.OrderViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
@Preview
fun OrderScreenPreview() {
    OrderScreen(
        orderList = listOf(Order("", "", 0, 5, 0, 1)),
        navController = null,
        onClose = {}
    )
}

@Composable
fun OrderScreen(
    orderList: List<Order>,
    navController: NavController?,
    bucketViewModel: BucketViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = BucketViewModelFactory(
        BucketRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    ),
    orderViewModel: OrderViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = OrderViewModelFactory(
        OrderRepositoryImpl(SupabaseClient.client)
    )
    ),
    onClose: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var columnHeight by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteGreyBackground)
            .onSizeChanged { size ->
                columnHeight = size.height
            }
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.order),
            onBackButtonClick = { onClose() },
            actionIconButton = { Spacer(modifier = Modifier.size(36.dp)) }
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            DeliveryContactInfo(modifier = Modifier.padding(10.dp))

            LaunchedEffect(Unit) {
                bucketViewModel.getBucketSum()
            }

            val context = LocalContext.current
            OrderPrice(orderPrice = bucketViewModel.bucketSum, delivery = 0.0) {
                coroutineScope.launch {
                    if (orderList.isNotEmpty()) {
                        orderViewModel.addOrder(orderList)
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context,
                                context.getString(R.string.bucker_is_empty), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    if (orderViewModel.newOrderId != null) {
        CustomAlertDialog(
            imageResId = R.drawable.succes_img,
            title = stringResource(R.string.order_succes),
            message = "",
            buttonText = stringResource(R.string.back_to_shopping),
            onDismiss = {
                orderViewModel.dismissSuccess()
                navController?.navigate(Layouts.MAIN_LAYOUT) {
                    popUpTo(Layouts.MAIN_LAYOUT) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }

                coroutineScope.launch {
                    orderViewModel.orders.forEach { o ->
                        bucketViewModel.deleteBucket(
                            Bucket(
                                userId = o.userId,
                                quantity = o.quantity,
                                productExampleId = o.productExampleId
                            )
                        )
                    }
                }
            },
            onConfirm = {
                orderViewModel.dismissSuccess()
            }
        )
    }
}