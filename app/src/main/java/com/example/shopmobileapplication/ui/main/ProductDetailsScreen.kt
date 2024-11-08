package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductSize
import com.example.shopmobileapplication.data.bucket.BucketRepositoryImpl
import com.example.shopmobileapplication.data.favorite.FavoriteRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.ui.main.composable.BucketIconButton
import com.example.shopmobileapplication.ui.main.composable.CategoriesHelper
import com.example.shopmobileapplication.ui.main.composable.CustomTopAppBar
import com.example.shopmobileapplication.ui.main.composable.SizeColorChipsRow
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.favoriteIconRed
import com.example.shopmobileapplication.ui.theme.favoriteRed
import com.example.shopmobileapplication.ui.theme.lightGrayBackground
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.ralewayTitle
import com.example.shopmobileapplication.ui.theme.unableBlue
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.viewmodel.BucketViewModel
import com.example.shopmobileapplication.viewmodel.BucketViewModelFactory
import com.example.shopmobileapplication.viewmodel.FavoriteViewModel
import com.example.shopmobileapplication.viewmodel.FavoriteViewModelFactory
import com.example.shopmobileapplication.viewmodel.ProductViewModel
import com.example.shopmobileapplication.viewmodel.ProductViewModelFactory
import com.example.shopmobileapplication.viewmodel.UserViewModel
import kotlinx.coroutines.launch

object ProductDetailsHelper {
    final val defaultProduct: Product = Product("0", "Nike Air Max 270 Essential", CategoriesHelper.defaultSelectedCategory.id,"Вставка Max Air 270 обеспечивает непревзойденный комфорт в течение всего дня. Изящный дизайн ........", 179.39, "", 0)
}

@Composable
@Preview
fun ProductDetailsScreenPreview() {
    ProductDetailsScreen("", null)
}

@Composable
fun ProductDetailsScreen(
    productId: String,
    navController: NavController?,
    productViewModel: ProductViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = ProductViewModelFactory(
        ProductRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    ),
    bucketViewModel: BucketViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = BucketViewModelFactory(
        BucketRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    ),
    favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = FavoriteViewModelFactory(
        FavoriteRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    var selectedProductColorSize by remember { mutableStateOf<ProductSize?>(null) }

    LaunchedEffect(Unit) {
        productViewModel.getProductById(productId)
        productViewModel.getProductsSizes(productId)
    }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteGreyBackground),
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.sneaker_shop), // TODO: seller name
                onBackButtonClick = { navController!!.popBackStack() },
                actionIconButton = {
                    BucketIconButton { }
                }
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier.height(56.dp)
            ) {
                FloatingActionButton(
                    modifier = Modifier.fillMaxHeight(),
                    onClick = {
                        coroutineScope.launch {
                            if (productViewModel.isInFavorites) {
                                favoriteViewModel.deleteFavorite(
                                    Favorite(
                                        userId = UserViewModel.currentUser.id,
                                        productId = productId
                                    )
                                )
                            } else {
                                favoriteViewModel.addFavorite(
                                    Favorite(
                                        userId = UserViewModel.currentUser.id,
                                        productId = productId
                                    )
                                )
                            }
                            productViewModel.getProductById(productId)
                        }
                    },
                    shape = CircleShape,
                    containerColor = if (productViewModel.isInFavorites) favoriteRed else lightGrayBackground
                ) {
                    Icon(
                        modifier = Modifier
                            .size(36.dp, 36.dp)
                            .padding(4.dp),
                        imageVector = if (productViewModel.isInFavorites) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorites),
                        tint = if (productViewModel.isInFavorites) favoriteIconRed else Color.Black
                    )
                }
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = 30.dp),
                    text = { Text(text = if (productViewModel.isInBucket) stringResource(R.string.add) else stringResource(R.string.to_bucket), style = ralewaySubtitle, color = Color.White) },
                    onClick = {
                        if (selectedProductColorSize != null) {
                            coroutineScope.launch {
                                if (productViewModel.isInBucket) {
                                    bucketViewModel.updateBucket(
                                        Bucket(
                                            userId = UserViewModel.currentUser.id,
                                            productExampleId = selectedProductColorSize!!.id,
                                            quantity = (bucketViewModel.buckets.find { it.productExampleId == selectedProductColorSize!!.id && it.userId == UserViewModel.currentUser.id }?.quantity ?: 0) + 1
                                        )
                                    )
                                }
                                bucketViewModel.addProductToBucket(
                                    Bucket(
                                        userId = UserViewModel.currentUser.id,
                                        productExampleId = selectedProductColorSize!!.id,
                                        quantity = 1
                                    )
                                )
                                productViewModel.getProductById(productId)
                            }
                        }
                    },
                    backgroundColor = if (selectedProductColorSize == null) unableBlue else blueGradientStart,
                    icon = {
                        Icon(
                            modifier = Modifier
                                .size(36.dp, 36.dp)
                                .padding(4.dp),
                            painter = if (productViewModel.isInBucket) painterResource(id = R.drawable.baseline_add_24) else painterResource(id = R.drawable.bucket_icon),
                            contentDescription = stringResource(R.string.bucket),
                            tint = Color.White
                        )
                    }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(whiteGreyBackground)
                .padding(paddingValues = paddingValues)
                .padding(horizontal = 10.dp)
        ) {
            if (productViewModel.product != null ) {
                Text(text = productViewModel.product!!.name, style = ralewayTitle, textAlign = TextAlign.Left, modifier = Modifier
                    .padding(vertical = 5.dp)
                    .padding(top = 10.dp))

                Text(text = CategoriesHelper.defaultSelectedCategory.name, style = ralewaySubregular, textAlign = TextAlign.Left, modifier = Modifier.padding(vertical = 5.dp))

                Text(text = "₽${productViewModel.product!!.price}", style = ralewayTitle, textAlign = TextAlign.Left, modifier = Modifier.padding(vertical = 5.dp))

                var rotation by remember { mutableStateOf(0f) }
                Box(modifier = Modifier
                    .padding(vertical = 5.dp)
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            rotation += dragAmount.x * 0.5f
                            change.consume()
                        }
                    }
                ) {
                    Image(painter = painterResource(id = R.drawable.onboard_2_all), contentDescription = "image", modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer(rotationY = rotation), contentScale = ContentScale.FillWidth)
                }

                SizeColorChipsRow(productSizes = productViewModel.productSizes) { _, productSize ->
                    selectedProductColorSize = productSize
                }

                Text(text = productViewModel.product!!.description, style = ralewaySubregular, textAlign = TextAlign.Left, modifier = Modifier.padding(vertical = 5.dp))
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 70.dp), horizontalArrangement = Arrangement.End) {
                    TextButton(
                        modifier = Modifier.background(Color.Transparent),
                        onClick = { }
                    ) {
                        Text(text = stringResource(R.string.more_info), style = ralewaySubregular, color = blueGradientStart)
                    }
                }
            }
        }
    }

}