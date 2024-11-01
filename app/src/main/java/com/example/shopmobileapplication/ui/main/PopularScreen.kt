package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.ui.CustomAlertDialog
import com.example.shopmobileapplication.ui.main.composable.CustomLazyVerticalGrid
import com.example.shopmobileapplication.ui.main.composable.CustomTopAppBar
import com.example.shopmobileapplication.ui.main.composable.FavoriteIconButton
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.viewmodel.ProductViewModel
import com.example.shopmobileapplication.viewmodel.ProductViewModelFactory

@Preview
@Composable
private fun PopularsScreenPreview() {
    PopularsScreen(navController = null)
}

@Composable
fun PopularsScreen(
    navController: NavController?,
    productViewModel: ProductViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = ProductViewModelFactory(
        ProductRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    LaunchedEffect(Unit) {
        productViewModel.getAllProducts()
    }

    if (productViewModel.isLoading) {
//        CircularProgressIndicator()
    } else if (productViewModel.error != null) {
        CustomAlertDialog(
            imageResId = R.drawable.message_icon,
            title = stringResource(R.string.error),
            message = stringResource(R.string.data_error),
            onDismiss = {
                productViewModel.dismissError()
            },
            onConfirm = {
                productViewModel.dismissError()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteGreyBackground)
    ) {
        var isBucketEmpty by remember { mutableStateOf(true) }

        CustomTopAppBar(
            title = stringResource(id = R.string.popular),
            onBackButtonClick = { navController!!.popBackStack() },
            actionIconButton = {
                FavoriteIconButton { }
            }
        )

        CustomLazyVerticalGrid(source = ArrayList(productViewModel.products), navController = navController)
    }
}