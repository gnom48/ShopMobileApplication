package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Order
import com.example.shopmobileapplication.data.bucket.BucketRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.ui.main.components.CustomTopAppBar
import com.example.shopmobileapplication.ui.main.components.OrderPrice
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.BucketViewModel
import com.example.shopmobileapplication.ui.viewmodel.BucketViewModelFactory
import kotlinx.coroutines.launch

@Composable
@Preview
fun OrderScreenPreview() {
    OrderScreen(
        orderList = listOf(Order("", "", 0, 5, "")),
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text("Контактная информация", style = ralewayRegular)

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Card(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = whiteGreyBackground),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Box(modifier = Modifier.padding(10.dp)) {
                                Icon(
                                    imageVector = Icons.Outlined.Email,
                                    contentDescription = "email",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text("example@mail.com", style = ralewaySubtitle, modifier = Modifier.padding(bottom = 5.dp))
                            Text("Email", style = ralewaySubregular)
                        }
                        Box(modifier = Modifier.padding(10.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Create,
                                contentDescription = "email",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Card(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = whiteGreyBackground),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Box(modifier = Modifier.padding(10.dp)) {
                                Icon(
                                    imageVector = Icons.Outlined.Call,
                                    contentDescription = "email",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text("+7(800)-555-35-35", style = ralewaySubtitle, modifier = Modifier.padding(bottom = 5.dp))
                            Text("Телефон", style = ralewaySubregular)
                        }
                        Box(modifier = Modifier.padding(10.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.Create,
                                contentDescription = "email",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Text("Адрес", style = ralewayRegular)
                    Text("1082 Аэропорт, Нигерии", style = ralewaySubregular)
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = "map",
                        painter = painterResource(id = R.drawable.map)
                    )

                    Text("Способ оплаты", style = ralewayRegular)
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Card(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(end = 10.dp),
                            colors = CardDefaults.cardColors(containerColor = whiteGreyBackground),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Box(modifier = Modifier.padding(10.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.card_icon),
                                    contentDescription = "email",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Text("DbL Card", style = ralewaySubtitle, modifier = Modifier.padding(bottom = 5.dp))
                            Text("**** **** 0696 4629", style = ralewaySubregular)
                        }
                        Box(modifier = Modifier.padding(10.dp)) {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                contentDescription = "more",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            LaunchedEffect(Unit) {
                bucketViewModel.getBucketSum()
            }

            OrderPrice(orderPrice = bucketViewModel.bucketSum, delivery = 0.0) {
                coroutineScope.launch {
                    // TODO: addOrder()
                }
            }
        }
    }
}