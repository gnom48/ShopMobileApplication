package com.example.shopmobileapplication.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.ImageStorage
import com.example.shopmobileapplication.data.Order
import com.example.shopmobileapplication.data.ProductSize
import com.example.shopmobileapplication.data.bucket.BucketRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.ui.main.composable.CustomTopAppBar
import com.example.shopmobileapplication.ui.main.composable.OrderPrice
import com.example.shopmobileapplication.ui.main.composable.ProductCountClicker
import com.example.shopmobileapplication.ui.main.composable.ProductDeleteClicker
import com.example.shopmobileapplication.ui.main.composable.SwipeableItemWithActions
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.viewmodel.BucketViewModel
import com.example.shopmobileapplication.viewmodel.BucketViewModelFactory
import com.example.shopmobileapplication.viewmodel.UserViewModel
import com.skydoves.flexible.bottomsheet.material3.BottomSheetDefaults
import com.skydoves.flexible.bottomsheet.material3.FlexibleBottomSheet
import com.skydoves.flexible.core.FlexibleSheetSize
import com.skydoves.flexible.core.FlexibleSheetValue
import com.skydoves.flexible.core.rememberFlexibleBottomSheetState
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID

@Composable
@Preview
fun BucketScreenPreview() {
    BucketScreen(null)
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun BucketScreen(
    navController: NavController?,
    bucketViewModel: BucketViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = BucketViewModelFactory(
        BucketRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    LaunchedEffect(Unit) {
        bucketViewModel.getBucketList(UserViewModel.currentUser)
        bucketViewModel.getProductsInBucket(UserViewModel.currentUser)
    }

    LaunchedEffect(bucketViewModel.buckets) {
        if (bucketViewModel.buckets.isNotEmpty()) {
            bucketViewModel.getProductsSizesInBucket(UserViewModel.currentUser)
        }
    }

    val coroutineScope = rememberCoroutineScope()

    var columnHeight by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteGreyBackground)
            .onSizeChanged { size ->
                columnHeight = size.height
            }
    ) {
        CustomTopAppBar(
            title = stringResource(R.string.bucket_title),
            onBackButtonClick = { navController!!.popBackStack() },
            actionIconButton = { Spacer(modifier = Modifier.size(36.dp)) }
        )

        var currentSheetTarget by remember { mutableStateOf(FlexibleSheetValue.SlightlyExpanded) }
        val sheetState = rememberFlexibleBottomSheetState(
            flexibleSheetSize = FlexibleSheetSize(
                fullyExpanded = 1f,
                slightlyExpanded = 0.35f,
            ),
            allowNestedScroll = true,
            isModal = false,
            skipSlightlyExpanded = false,
            skipIntermediatelyExpanded = true,
            skipHiddenState = true
        )
        FlexibleBottomSheet(
            onDismissRequest = { },
            sheetState = sheetState,
            onTargetChanges = { sheetValue ->
                currentSheetTarget = sheetValue
            },
            containerColor = Color.White,
            shape = if (currentSheetTarget == FlexibleSheetValue.FullyExpanded) RectangleShape else BottomSheetDefaults.ExpandedShape
        ) {
            when (currentSheetTarget) {
                FlexibleSheetValue.FullyExpanded -> {
                    val orderList by remember { mutableStateOf<MutableList<Order>>(mutableListOf()) }
                    bucketViewModel.buckets.forEach { b ->
                        bucketViewModel.productsSizesInBucket.find { it.id == b.productExampleId }?.let {
                            orderList.add(
                                Order(
                                    id = UUID.randomUUID().toString(),
                                    userId = UserViewModel.currentUser.id,
                                    productExampleId = it.id,
                                    quantity = b.quantity,
                                    orderDate = LocalDateTime.now().toString()
                                )
                            )
                        }
                    }

                    OrderScreen(orderList = orderList, navController = navController, onClose = { currentSheetTarget = FlexibleSheetValue.SlightlyExpanded })
                }
                FlexibleSheetValue.SlightlyExpanded -> {
                    OrderPrice(bucketViewModel.bucketSum, 0.0) {
                        coroutineScope.launch {
                            when (sheetState.swipeableState.currentValue) {
                                FlexibleSheetValue.FullyExpanded -> {
                                    // TODO: confirm
                                }
                                FlexibleSheetValue.SlightlyExpanded -> sheetState.fullyExpand()
                                else -> { }
                            }
                        }
                    }
                }
                else -> { }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(whiteGreyBackground)
                .padding(10.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(whiteGreyBackground)
            ) {
                item {
                    Text(text = stringResource(R.string.products_in_bucket) + " ${bucketViewModel.buckets.size}", style = ralewaySubtitle, modifier = Modifier.padding(vertical = 5.dp))
                }
                items(bucketViewModel.productsSizesInBucket) { productSize: ProductSize ->
                    val currentPositionInBucket = bucketViewModel.buckets.find { it.productExampleId == productSize.id }
                    if (currentPositionInBucket != null) {
                        SwipeableItemWithActions(
                            leftAction = {
                                ProductCountClicker(count = currentPositionInBucket.quantity) { change: Int ->
                                    coroutineScope.launch {
                                        if (change <= 0) {
                                            bucketViewModel.deleteBucket(currentPositionInBucket)
                                        } else {
                                            currentPositionInBucket.quantity = change
                                            bucketViewModel.updateBucket(currentPositionInBucket)
                                        }
                                    }
                                }
                            },
                            rightAction = {
                                ProductDeleteClicker {
                                    coroutineScope.launch {
                                        bucketViewModel.deleteBucket(currentPositionInBucket)
                                    }
                                }
                            }
                        ) {
                            Card(
                                modifier = Modifier
                                    .height(130.dp)
                                    .fillMaxWidth()
                                    .background(whiteGreyBackground),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(contentColor = Color.White, containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                val pr = bucketViewModel.productsInBucket.find { it.id == productSize.productId }
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = ImageStorage.getLink(pr?.image),
                                            contentDescription = "image",
                                            modifier = Modifier
                                                .background(
                                                    shape = RoundedCornerShape(13.dp),
                                                    color = whiteGreyBackground
                                                )
                                                .fillMaxHeight()
                                                .aspectRatio(1f / 1f)
                                                .padding(5.dp),
                                            contentScale = ContentScale.FillWidth)
                                    }

                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(end = 10.dp)
                                    ) {
                                        Text(
                                            text = pr?.name ?: "",
                                            style = ralewaySubtitle,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = stringResource(R.string.size) + productSize.sizeRus.toString(),
                                            style = ralewayRegular,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "₽" + pr?.price ?: "0.0",
                                            style = ralewayRegular
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(170.dp))
                }
            }
        }
    }
}
