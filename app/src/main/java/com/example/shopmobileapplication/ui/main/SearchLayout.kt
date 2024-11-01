package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.ui.CustomAlertDialog
import com.example.shopmobileapplication.ui.main.composable.CustomLazyVerticalGrid
import com.example.shopmobileapplication.ui.theme.greyText
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.viewmodel.ProductViewModel
import com.example.shopmobileapplication.viewmodel.ProductViewModelFactory

@Composable
@Preview
fun SearchLayoutPreview() {
    SearchLayout(null)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchLayout(
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

        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(containerColor = whiteGreyBackground),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.search),
                        style = ralewaySubtitle
                    )
                }
            },
            navigationIcon = {
                IconButton(
                    onClick = {

                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize(),
                        shape = CircleShape
                    ) {
                        Image(
                            modifier = Modifier
                                .size(36.dp, 36.dp),
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "back"
                        )
                    }
                }
            },
            actions = {
                Surface(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(Color.Transparent)
                        .padding(end = 10.dp),
                    shape = CircleShape
                ) {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.favorite_icon),
                        contentDescription = "back"
                    )
                }

            }
        )

        var query by rememberSaveable { mutableStateOf("") }
        var letsSearch by rememberSaveable { mutableStateOf(false) }
        var searchHistory by remember { mutableStateOf<List<String>>(listOf()) }
        var isSearchBarActive by rememberSaveable { mutableStateOf(true) }

        LaunchedEffect(letsSearch) {
            if (letsSearch) {
                productViewModel.getSearchResultsByProductName(query)
                letsSearch = false
            }
        }

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 5.dp)
                .background(shape = RoundedCornerShape(25.dp), color = Color.White),
            query = query,
            onQueryChange = {
                query = it
            },
            onSearch = {
                letsSearch = true
            },
            active = isSearchBarActive,
            onActiveChange = {
                isSearchBarActive = it
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = greyText,
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.microfone),
                    contentDescription = null,
                    tint = greyText,
                    modifier = Modifier.size(24.dp)
                )
            },
            placeholder = {
                Text(text = stringResource(R.string.search), style = ralewaySubregular)
            },
            tonalElevation = 8.dp,
            colors = SearchBarDefaults.colors(
                containerColor = Color.White,
                dividerColor = Color.Transparent
            )
        ) {
            Column(modifier = Modifier
                .background(color = whiteGreyBackground)
                .fillMaxSize()) {
//                    Divider()
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Absolute.Right
//                    ) {
//                        TextButton(
//                            modifier = Modifier
//                                .wrapContentSize(),
//                            onClick = {
//                                // TODO: очистить локальную историю поиска
//                                searchHistory
//                                    .toMutableList()
//                                    .clear()
//                            }
//                        ) {
//                            Text(
//                                style = ralewaySubregular,
//                                text = stringResource(R.string.clear_history)
//                            )
//                        }
//                    }

                if (searchHistory.isEmpty()) {
                    Text(
                        style = ralewayRegular,
                        modifier = Modifier.padding(10.dp),
                        text = "Найдите любые товары!"
                    )
                } else {
                    Text(
                        style = ralewayRegular,
                        modifier = Modifier.padding(10.dp),
                        text = "История поиска"
                    )
                    searchHistory.forEach {
                        if (it.isNotEmpty()) {
                            Divider()
                            Row(
                                modifier = Modifier.padding(all = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = it, style = ralewaySubregular)
                            }
                        }
                    }
                }

//                LazyColumn(modifier = Modifier, contentPadding = PaddingValues(5.dp)) {
//                    if (searchResults.isEmpty()) {
//                        item {
//                            Text(text = "Ничего не найдено!", style = ralewaySubregular, modifier = Modifier.padding(5.dp))
//                        }
//                    } else {
//                        items(searchResults) {
//                            Row(
//                                modifier = Modifier
//                                    .padding(all = 5.dp)
//                                    .clickable {
//                                        // TODO: открыть результат поиска
//                                    }
//                            ) {
//                                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
//                                Spacer(modifier = Modifier.width(10.dp))
//                                Text(text = it.name, style = ralewaySubregular)
//                            }
//                        }
//                    }
//                }

            }
        }

        CustomLazyVerticalGrid(source = ArrayList(productViewModel.searchResults), navController = navController)
    }
}