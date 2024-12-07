package com.example.shopmobileapplication.ui.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.Seller
import com.example.shopmobileapplication.data.bucket.BucketRepositoryImpl
import com.example.shopmobileapplication.data.favorite.FavoriteRepositoryImpl
import com.example.shopmobileapplication.data.favorite.LocalFavoriteRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.viewmodel.BucketViewModel
import com.example.shopmobileapplication.ui.viewmodel.BucketViewModelFactory
import com.example.shopmobileapplication.ui.viewmodel.FavoriteViewModel
import com.example.shopmobileapplication.ui.viewmodel.FavoriteViewModelFactory
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.utils.SharedPreferecesHelper
import kotlinx.coroutines.launch

@Composable
@Preview
fun CustomLazyVerticalGridPreview() {
    CustomLazyVerticalGrid(
        source = arrayListOf(
            Product("jk32ln4", "Nike Air Max", 0, "Super crosses", 752.0, "https://moveonline.ru/upload/iblock/6c4/jiidzbqb4y7dh9mj8exzeh8ne9n019v4.jpg", 0),
            Product("jk32ln4", "Nike Air Max", 0, "Super crosses", 752.0, "https://moveonline.ru/upload/iblock/6c4/jiidzbqb4y7dh9mj8exzeh8ne9n019v4.jpg", 0),
            Product("jk32ln4", "Nike Air Max", 0, "Super crosses", 752.0, "https://moveonline.ru/upload/iblock/6c4/jiidzbqb4y7dh9mj8exzeh8ne9n019v4.jpg", 0),
            Product("jk32ln4", "Nike Air Max", 0, "Super crosses", 752.0, "https://moveonline.ru/upload/iblock/6c4/jiidzbqb4y7dh9mj8exzeh8ne9n019v4.jpg", 0),
            Product("jk32ln4", "Nike Air Max", 0, "Super crosses", 752.0, "https://moveonline.ru/upload/iblock/6c4/jiidzbqb4y7dh9mj8exzeh8ne9n019v4.jpg", 0)
        ),
        null, {_->}, {}
    )
}

@Composable
fun CustomLazyVerticalGrid(
    source: ArrayList<Product>,
    navController: NavController?,
    onShowProductSizes: (product: Product?) -> Unit, onHideProductSizes: () -> Unit,
    itemMinWidth: Int = 160,
    enableScroll: Boolean = true,
    bucketViewModel: BucketViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = BucketViewModelFactory(
        BucketRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    ),
    favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = FavoriteViewModelFactory(
        if (UserViewModel.currentUser == UserRepositoryImpl.GUEST) LocalFavoriteRepositoryImpl(
            SharedPreferecesHelper(LocalContext.current)
        )
        else FavoriteRepositoryImpl(LocalContext.current, SupabaseClient.client)
    //        FavoriteRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    val localCoroutineScope = rememberCoroutineScope()

    val screenWidth = LocalConfiguration.current.screenWidthDp
    val columns = screenWidth / itemMinWidth

    LaunchedEffect(favoriteViewModel.favorites, bucketViewModel.buckets) {
        bucketViewModel.getProductsInBucket(UserViewModel.currentUser)
        favoriteViewModel.getFavoriteList(UserViewModel.currentUser)
    }

    if (enableScroll) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Adaptive(minSize = 120.dp), //GridCells.Fixed(columns),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(source) { item ->
                val isInFavorite = favoriteViewModel.favorites.map { it.productId }.contains(item.id)
                val isInBucket = bucketViewModel.productsInBucket.map { it.id }.contains(item.id)

                MainContentItem(
                    product = item,
                    seller = Seller(item.sellerId, "Best Seller", ""),
                    navController = navController,
                    isInBucket = isInBucket,
                    isInFavorite = isInFavorite,
                    onBucketClick = { prod: Product, nav: NavController ->
                        onShowProductSizes(prod)
                    },
                    onFavoriteClick = { prod: Product, nav: NavController ->
                        localCoroutineScope.launch {
                            if (!isInFavorite) {
                                favoriteViewModel.addFavorite(
                                    Favorite(
                                        UserViewModel.currentUser.id,
                                        prod.id
                                    )
                                )
                            } else {
                                favoriteViewModel.deleteFavorite(
                                    Favorite(
                                        UserViewModel.currentUser.id,
                                        prod.id
                                    )
                                )
                            }
                        }
                    }
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (item in source) {
                    val isInFavorite = favoriteViewModel.favorites.map { it.productId }.contains(item.id)
                    val isInBucket = bucketViewModel.productsInBucket.map { it.id }.contains(item.id)

                    Box(modifier = Modifier.width(itemMinWidth.dp)) {
                        MainContentItem(
                            seller = Seller(item.sellerId, "Best Seller", ""),
                            product = item,
                            navController = navController,
                            isInBucket = isInBucket,
                            isInFavorite = isInFavorite,
                            onBucketClick = { prod: Product, nav: NavController ->
                                onShowProductSizes(prod)
                            },
                            onFavoriteClick = { prod: Product, nav: NavController ->
                                localCoroutineScope.launch {
                                    if (!isInFavorite) {
                                        favoriteViewModel.addFavorite(
                                            Favorite(
                                                UserViewModel.currentUser.id,
                                                prod.id
                                            )
                                        )
                                    } else {
                                        favoriteViewModel.deleteFavorite(
                                            Favorite(
                                                UserViewModel.currentUser.id,
                                                prod.id
                                            )
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

