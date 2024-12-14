package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductCategory
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.main.components.BucketIconButton
import com.example.shopmobileapplication.ui.main.components.CategoriesPanel
import com.example.shopmobileapplication.ui.main.components.CustomAlertDialog
import com.example.shopmobileapplication.ui.main.components.CustomLazyVerticalGrid
import com.example.shopmobileapplication.ui.main.components.DriverMenuIconButton
import com.example.shopmobileapplication.ui.main.menu.BottomMenuItem
import com.example.shopmobileapplication.ui.main.search.SearchLayout
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.ralewayTitle
import com.example.shopmobileapplication.ui.theme.white
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.ProductViewModel
import com.example.shopmobileapplication.ui.viewmodel.ProductViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(null, null, {}, {}, null, null)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController?,
    bottomNavController: NavController?,
    onShowModalBottomSheetProductSizes: (p: Product) -> Unit,
    onHideModalBottomSheetProductSizes: () -> Unit,
    drawerMenuState: DrawerState?,
    drawerMenuScope: CoroutineScope?,
    productViewModel: ProductViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = ProductViewModelFactory(
        ProductRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    LaunchedEffect(Unit) {
        productViewModel.getProductsCategories()
        productViewModel.getAllProducts(2)
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
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = whiteGreyBackground),
            title = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.size(14.dp),
                        painter = painterResource(id = R.drawable.bzdylinka_on_main_app_top_panel),
                        contentDescription = "nothing"
                    )
                    Text(
                        text = stringResource(R.string.main_title),
                        style = ralewayTitle
                    )
                }
            },
            navigationIcon = {
                drawerMenuState?.let { state ->
                    DriverMenuIconButton {
                        drawerMenuScope?.let { scope ->
                            scope.launch {
                                state.open()
                            }
                        }
                    }
                }
            },
            actions = {
                BucketIconButton {
                    navController?.navigate(BottomMenuItem.BucketScreen.route) {
                        launchSingleTop = true
                    }
                }
            }
        )

        SearchLayout(navController = navController, productViewModel = productViewModel)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            CategoriesPanel(null, productViewModel.productCategories) { selectedCategory: ProductCategory ->
                navController!!.navigate(Layouts.CATEGORIES_LAYOUT) {
                    launchSingleTop = true
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Популярное", style = ralewaySubtitle)

                TextButton(
                    onClick = {

                    }
                ) {
                    Text(text = "Все", style = ralewaySubtitle, color = blueGradientStart)
                }
            }

            CustomLazyVerticalGrid(
                source = ArrayList(productViewModel.products),
                navController = navController,
                enableScroll = false,
                onShowProductSizes = { p: Product? ->
                    p?.let { onShowModalBottomSheetProductSizes(it) }
                },
                onHideProductSizes = {
                    onHideModalBottomSheetProductSizes()
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Акции", style = ralewaySubtitle)

                TextButton(
                    onClick = {

                    }
                ) {
                    Text(text = "Все", style = ralewaySubtitle, color = blueGradientStart)
                }
            }

            Surface(
                shape = RoundedCornerShape(18),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(whiteGreyBackground)
                    .padding(start = 10.dp, end = 10.dp, bottom = 120.dp),
                shadowElevation = 3.dp
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(white)
                        .padding(5.dp),
                    painter = painterResource(id = R.drawable.sales),
                    contentDescription = "Акция",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}