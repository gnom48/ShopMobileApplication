package com.example.shopmobileapplication.ui.main.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Favorite
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.Seller
import com.example.shopmobileapplication.data.bucket.BucketRepositoryImpl
import com.example.shopmobileapplication.data.favorite.FavoriteRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.favoriteIconRed
import com.example.shopmobileapplication.ui.theme.favoriteRed
import com.example.shopmobileapplication.ui.theme.lightGrayBackground
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.viewmodel.BucketViewModel
import com.example.shopmobileapplication.viewmodel.BucketViewModelFactory
import com.example.shopmobileapplication.viewmodel.FavoriteViewModel
import com.example.shopmobileapplication.viewmodel.FavoriteViewModelFactory
import com.example.shopmobileapplication.viewmodel.SupabaseViewModel
import com.example.shopmobileapplication.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
@Preview
fun MainContentItemPreview() {
    MainContentItem(
        Seller(0, "Best Seller", ""),
        Product("e456rgt7hk97h8", "Nike Air Max", 1, "Best shoes", 752.0, "", 0),
        null,
        null
    )
}

@Composable
fun MainContentItem(
    seller: Seller,
    product: Product,
    imageUrl: String? = product.image,
    navController: NavController?,
    isFavorite: Boolean = false,
    isInBucket: Boolean = false,
    bucketViewModel: BucketViewModel = viewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = BucketViewModelFactory(
            BucketRepositoryImpl(LocalContext.current, SupabaseClient.client)
        )
    ),
    favoriteViewModel: FavoriteViewModel = viewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current!!,
        factory = FavoriteViewModelFactory(
            FavoriteRepositoryImpl(LocalContext.current, SupabaseClient.client)
        )
    ),
    supabaseViewModel: SupabaseViewModel = viewModel(),
) {
    val localCoroutineScope = rememberCoroutineScope()

    var isFavoriteState by remember { mutableStateOf(isFavorite) }
    var isInBucketState by remember { mutableStateOf(isInBucket) }

    Surface(
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(32f / 52f)
            .padding(5.dp)
            .background(Color.White)
            .background(lightGrayBackground)
            .clickable {
                navController!!.navigate(Layouts.DETAILS_SCREEN + "/${product.id}")
            }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, start = 5.dp)
                    .weight(0.1f),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = {
                        if (!isFavoriteState) {
                            localCoroutineScope.launch {
                                favoriteViewModel.addFavorite(Favorite(UserViewModel.currentUser!!.id, product.id))
                                isFavoriteState = true
                            }
                        } else {
                            localCoroutineScope.launch {
                                favoriteViewModel.deleteFavorite(Favorite(UserViewModel.currentUser!!.id, product.id))
                                isFavoriteState = false
                            }
                        }
                    },
                    modifier = Modifier
                        .background(shape = CircleShape, color = favoriteRed)
                        .fillMaxHeight()
                        .aspectRatio(1f / 1f)
                ) {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        imageVector = if (isFavoriteState) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = stringResource(R.string.favorites),
                        tint = if (isFavoriteState) favoriteIconRed else Color.Black
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .weight(0.4f)
                    .background(shape = RoundedCornerShape(15.dp), color = Color.Transparent)
            ) {

//                var imageSignedUrl by remember { mutableStateOf<String?>(null) }
//                supabaseViewModel.getSignedUrlFromBucket(fileName = "1000013704.jpg") { url: String? ->
//                    if (url != null) {
//                        imageSignedUrl = "http://31.129.102.158:8000/" + url //"https://png.pngtree.com/png-clipart/20220510/original/pngtree-cool-pair-of-casual-shoes-with-blue-wave-patterned-icon-element-png-image_7692533.png"
//                    }
//                }
                AsyncImage(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(shape = RoundedCornerShape(18.dp), color = Color.Transparent),
                    model = imageUrl,
                    error = painterResource(id = R.drawable.default_shoes),
                    contentDescription = "Фото",
                    contentScale = ContentScale.FillBounds,
                    onError = { e ->
                        Log.e("ERROR", e.toString())
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.3f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 5.dp, start = 5.dp)
                        .weight(0.8f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 5.dp),
                        text = seller.name,
                        style = ralewaySubtitle,
                        color = blueGradientStart,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = product.name,
                        style = ralewaySubtitle,
                        color = Color.LightGray,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 10.dp),
                        text = "₽ " + product.price,
                        style = ralewaySubtitle,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }

                IconButton(
                    onClick = {
                        localCoroutineScope.launch {
                            bucketViewModel.addProductToBucket(Bucket(UserViewModel.currentUser!!.id, product.id, 1))
                            isInBucketState = true
                        }
                    },
                    modifier = Modifier
                        .background(
                            shape = RoundedCornerShape(topStart = 20.dp),
                            color = blueGradientStart
                        )
                        .fillMaxWidth()
                        .aspectRatio(1f / 1f)
                        .weight(0.3f)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            painter = if (isInBucketState) painterResource(id = R.drawable.baseline_add_24) else painterResource(id = R.drawable.bucket_icon),
                            contentDescription = stringResource(R.string.bucket),
                            tint = Color.White
                        )
                    }
                }
            }

        }
    }
}