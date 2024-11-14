package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.favorite.FavoriteRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.ui.main.components.CustomAlertDialog
import com.example.shopmobileapplication.ui.main.components.CustomLazyVerticalGrid
import com.example.shopmobileapplication.ui.main.components.CustomTopAppBar
import com.example.shopmobileapplication.ui.main.components.FavoriteIconButton
import com.example.shopmobileapplication.ui.main.components.ModalBottomSheetProductSizes
import com.example.shopmobileapplication.ui.main.menu.BottomMenuItem
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.FavoriteViewModel
import com.example.shopmobileapplication.ui.viewmodel.FavoriteViewModelFactory
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel

@Preview
@Composable
private fun FavoritesScreenPreview() {
    FavoritesScreen(navController = null)
}

@Composable
fun FavoritesScreen(
    navController: NavController?,
    favoriteViewModel: FavoriteViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = FavoriteViewModelFactory(
        FavoriteRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    LaunchedEffect(Unit) {
        favoriteViewModel.getProductsInFavorite(UserViewModel.currentUser)
    }
    if (favoriteViewModel.isLoading) {
//        CircularProgressIndicator()
    } else if (favoriteViewModel.error != null) {
        CustomAlertDialog(
            imageResId = R.drawable.message_icon,
            title = stringResource(R.string.error),
            message = stringResource(R.string.data_error),
            onDismiss = {
                favoriteViewModel.dismissError()
            },
            onConfirm = {
                favoriteViewModel.dismissError()
            }
        )
    }

    ModalBottomSheetProductSizes { onShow, onHide ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(whiteGreyBackground)
        ) {
            CustomTopAppBar(
                title = stringResource(id = R.string.favorites),
                onBackButtonClick = {
                    navController!!.navigate(BottomMenuItem.HomeScreen.route) {
                        launchSingleTop = true
                    }
                },
                actionIconButton = {
                    FavoriteIconButton { }
                }
            )

            if (favoriteViewModel.productsInFavorite.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(style = ralewaySubtitle, text = stringResource(R.string.empty_favorite_data), textAlign = TextAlign.Center)
                }
            }

            CustomLazyVerticalGrid(
                source = ArrayList(favoriteViewModel.productsInFavorite),
                navController = navController,
                onShowProductSizes = { p: Product? ->
                    onShow(p)
                },
                onHideProductSizes = {
                    onHide()
                }
            )
        }
    }
}