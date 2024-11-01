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
import com.example.shopmobileapplication.data.ProductCategory
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.ui.CustomAlertDialog
import com.example.shopmobileapplication.ui.main.composable.CategoriesHelper
import com.example.shopmobileapplication.ui.main.composable.CategoriesPanel
import com.example.shopmobileapplication.ui.main.composable.CustomLazyVerticalGrid
import com.example.shopmobileapplication.ui.main.composable.CustomTopAppBar
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.viewmodel.ProductViewModel
import com.example.shopmobileapplication.viewmodel.ProductViewModelFactory

@Preview
@Composable
private fun CategoriesScreenPreview() {
    CategoriesLayout(navController = null, selected = CategoriesHelper.defaultSelectedCategory)
}

@Composable
fun CategoriesLayout(
    navController: NavController?,
    selected: ProductCategory = CategoriesHelper.defaultSelectedCategory,
    productViewModel: ProductViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = ProductViewModelFactory(
        ProductRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    var singleSelectedCategory by remember { mutableStateOf(selected) }

    LaunchedEffect(Unit) {
        productViewModel.getProductsCategories()
    }

    LaunchedEffect(singleSelectedCategory) {
        productViewModel.getProductsByCategory(singleSelectedCategory)
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
        CustomTopAppBar(
            onBackButtonClick = {
                navController!!.popBackStack()
            },
            title = stringResource(R.string.categories),
            actionIconButton = { }
        )

        CategoriesPanel(
            categories = productViewModel.productCategories,
            selectedCategory = selected
        ) { c ->
            singleSelectedCategory = c
        }

        CustomLazyVerticalGrid(
            source = ArrayList(productViewModel.products),
            navController = navController
        )
    }
}