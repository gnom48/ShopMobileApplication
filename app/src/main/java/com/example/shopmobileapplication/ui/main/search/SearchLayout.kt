package com.example.shopmobileapplication.ui.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.main.components.CustomAlertDialog
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.greyText
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.ProductViewModel
import com.example.shopmobileapplication.ui.viewmodel.ProductViewModelFactory

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

    var isSearchBarActive by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = if (isSearchBarActive) 0.dp else 8.dp,
                start = if (isSearchBarActive) 0.dp else 8.dp,
                top = 0.dp,
                bottom = 8.dp
            )
            .background(color = whiteGreyBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {
        var query by rememberSaveable { mutableStateOf("") }
        var letsSearch by rememberSaveable { mutableStateOf(false) }
        var searchHistory by remember { mutableStateOf<List<String>>(listOf()) }

        LaunchedEffect(letsSearch) {
            if (letsSearch) {
                productViewModel.getSearchResultsByProductName(query)
                letsSearch = false
            }
        }

        SearchBar(
            modifier = Modifier
                .weight(1f)
                .padding(end = if (isSearchBarActive) 0.dp else 10.dp)
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
            placeholder = {
                Text(text = stringResource(R.string.search), style = ralewaySubregular)
            },
            trailingIcon = {
                if (isSearchBarActive) {
                    Icon(
                        modifier = Modifier.clickable {
                            if (query.isNotEmpty()) {
                                query = ""
                            } else {
                                isSearchBarActive = false
                            }
                        },
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon"
                    )
                }
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

                LazyColumn(modifier = Modifier, contentPadding = PaddingValues(5.dp)) {
                    item {
                        Text(text = "Результаты поиска", style = ralewayRegular, modifier = Modifier.padding(5.dp))
                    }
                    if (productViewModel.searchResults.isEmpty()) {
                        item {
                            Text(text = "Ничего не найдено!", style = ralewaySubregular, modifier = Modifier.padding(5.dp))
                        }
                    } else {
                        items(productViewModel.searchResults) { product ->
                            Row(
                                modifier = Modifier
                                    .padding(all = 5.dp)
                                    .clickable {
                                        navController?.navigate(Layouts.DETAILS_SCREEN + "/${product.id}") {
                                            launchSingleTop = true
                                        }
                                    }
                            ) {
                                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(text = product.name, style = ralewaySubregular)
                            }
                        }
                    }
                }
            }
        }

        if (!isSearchBarActive) {
            IconButton(
                onClick = {
                    navController?.navigate(Layouts.FILTERS_SCREEN) {
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .background(shape = CircleShape, color = blueGradientStart)
//                        .size(56.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search_filters),
                    contentDescription = "Фильтры",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp))
            }
        }
    }
}