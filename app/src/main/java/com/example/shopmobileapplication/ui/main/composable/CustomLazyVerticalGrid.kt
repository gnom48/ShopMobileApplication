package com.example.shopmobileapplication.ui.main.composable

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.Seller
import com.example.shopmobileapplication.data.bucket.BucketRepositoryImpl
import com.example.shopmobileapplication.data.favorite.FavoriteRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.viewmodel.BucketViewModel
import com.example.shopmobileapplication.viewmodel.BucketViewModelFactory
import com.example.shopmobileapplication.viewmodel.FavoriteViewModel
import com.example.shopmobileapplication.viewmodel.FavoriteViewModelFactory
import com.example.shopmobileapplication.viewmodel.UserViewModel
import com.example.shopmobileapplication.viewmodel.UserViewModelFactory

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
        null
    )
}

@Composable
fun CustomLazyVerticalGrid(
    source: ArrayList<Product>,
    navController: NavController?,
    itemMinWidth: Int = 160,
    enableScroll: Boolean = true,
    userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
        UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )),
    bucketViewModel: BucketViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = BucketViewModelFactory(
        BucketRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )),
    favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = FavoriteViewModelFactory(
        FavoriteRepositoryImpl(LocalContext.current, SupabaseClient.client)
    ))
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val columns = screenWidth / itemMinWidth

    userViewModel.getCurrentUserInfo()
    LaunchedEffect(userViewModel.user) {
        if (userViewModel.user != null) {
            bucketViewModel.getBucketList(userViewModel.user!!)
            favoriteViewModel.getFavoriteList(userViewModel.user!!)
        }
    }

    if (enableScroll) {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(columns),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(source) { item ->
                MainContentItem(
                    product = item,
                    seller = Seller(item.sellerId, "Best Seller", ""),
                    navController = navController,
                    imageUrl = item.image,
                    isFavorite = favoriteViewModel.favorites.map { it.productId }.contains(item.id),
                    isInBucket = bucketViewModel.buckets.map { it.productId }.contains(item.id),
                    bucketViewModel = bucketViewModel,
                    favoriteViewModel = favoriteViewModel
                )
            }
        }
    } else {
        NonScrollableGrid(
            source = source,
            navController = navController,
            itemMinWidth = itemMinWidth,
            userViewModel = userViewModel,
            bucketViewModel = bucketViewModel,
            favoriteViewModel = favoriteViewModel
        )
    }
}

@Composable
fun NonScrollableGrid(
    source: ArrayList<Product>,
    navController: NavController?,
    itemMinWidth: Int = 160,
    userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
        UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )),
    bucketViewModel: BucketViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = BucketViewModelFactory(
        BucketRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )),
    favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = FavoriteViewModelFactory(
        FavoriteRepositoryImpl(LocalContext.current, SupabaseClient.client)
    ))
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            for (item in source) {
                Box(modifier = Modifier.width(itemMinWidth.dp)) {
                    MainContentItem(
                        product = item,
                        seller = Seller(item.sellerId, "Best Seller", ""),
                        navController = navController,
                        imageUrl = item.image,
                        isFavorite = favoriteViewModel.favorites.map { it.productId }.contains(item.id),
                        isInBucket = bucketViewModel.buckets.map { it.productId }.contains(item.id),
                        bucketViewModel = bucketViewModel,
                        favoriteViewModel = favoriteViewModel
                    )
                }
            }
        }
    }
}
