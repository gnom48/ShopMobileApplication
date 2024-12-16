package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.order.OrderRepositoryImpl
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.main.components.CustomTopAppBar
import com.example.shopmobileapplication.ui.main.components.OrderItem
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.OrderViewModel
import com.example.shopmobileapplication.ui.viewmodel.OrderViewModelFactory
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

@Composable
fun AllOrdersScreen(
    navController: NavController?,
    orderViewModel: OrderViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = OrderViewModelFactory(
        OrderRepositoryImpl(SupabaseClient.client)
    )
    ),
    userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
        UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    LaunchedEffect(Unit) {
        orderViewModel.getOrdersDetails(UserViewModel.currentUser)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteGreyBackground)
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.orders),
            onBackButtonClick = {
                navController?.popBackStack()
            },
            actionIconButton = { Spacer(modifier = Modifier.size(36.dp)) }
        )

        val mappedOrdersDetails = orderViewModel.orderDetails.sortedByDescending { it.orderDateTime }.groupBy {
            LocalDateTime.ofEpochSecond(it.orderDateTime, 0, ZoneOffset.UTC).toLocalDate()
        }
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(10.dp)) {
            if (orderViewModel.orderDetails.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(style = ralewaySubtitle, text = stringResource(R.string.here_you_orders), textAlign = TextAlign.Center)
                    }
                }
            }
            mappedOrdersDetails.forEach { (date, orders) ->
                item {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp), horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = if (date == LocalDate.now()) stringResource(R.string.today) else date.toString(),
                            style = ralewaySubtitle,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.LightGray
                        )
                    }
                }
                items(orders.sortedByDescending { it.orderDateTime }) { item ->
                    OrderItem(data = item) {
                        navController?.navigate(Layouts.ORDER_DETAILS_SCREEN + "/${item.orderId}/PRODUCT/${item.productExampleId}")
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}