package com.example.shopmobileapplication.ui.main.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.ui.main.composable.CustomAlertDialog
import com.example.shopmobileapplication.ui.main.composable.CustomTopAppBar
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.lightGrayBackground
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.utils.getAllSizesRus
import com.example.shopmobileapplication.viewmodel.ProductViewModel
import com.example.shopmobileapplication.viewmodel.ProductViewModelFactory
import kotlinx.coroutines.launch

@Composable
@Preview
fun FiltersScreenPreview() {
    FiltersScreen(null)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FiltersScreen(
    navController: NavController?,
    productViewModel: ProductViewModel = remember {
        ProductViewModelFactory(
            ProductRepositoryImpl(null, SupabaseClient.client)
        ).create(ProductViewModel::class.java)
    }
) {
    val scope = rememberCoroutineScope()
    var completeFilter by remember { mutableStateOf<ProductFilter?>(null) }
    var hasError by remember { mutableStateOf(false) }

    if (hasError) {
        CustomAlertDialog(
            imageResId = R.drawable.message_icon,
            title = stringResource(R.string.error),
            message = stringResource(R.string.error_maybe_incorrect),
            onDismiss = {
                hasError = false
            },
            onConfirm = {
                hasError = false
            }
        )
    }

    val filterBuilder = ProductFilter.Builder()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(whiteGreyBackground),
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.filters_search),
                onBackButtonClick = { navController?.popBackStack() },
                actionIconButton = { }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .height(56.dp),
                text = { Text(text = stringResource(R.string.lets_search), style = ralewaySubtitle, color = Color.White) },
                onClick = {
                    completeFilter = try { filterBuilder.build() } catch (_: Exception) { null }
                    scope.launch {
                        completeFilter?.let { productViewModel.getSearchResultByFilters(it) }
                        // TODO: show filter results
                    }
                },
                backgroundColor = blueGradientStart,
                icon = { }
                )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(whiteGreyBackground)
                .fillMaxSize()
                .padding(paddingValues)
                .padding(bottom = 100.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                text = "Цена",
                style = ralewaySubtitle
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "От", style = ralewaySubregular)
                var minPrice by remember { mutableStateOf<String>("") }
                OutlinedTextField(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 10.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text(text = "0.0₽", style = ralewaySubregular) },
                    value = minPrice,
                    onValueChange = {
                        minPrice = it
                        if (minPrice.isNotBlank()) {
                            filterBuilder.minPrice(try { minPrice.toDouble() } catch (_: Exception) { hasError = true; null })
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = lightGrayBackground,
                        unfocusedIndicatorColor = lightGrayBackground
                    ),
                    textStyle = ralewayRegular
                )

                Text(text = "До", style = ralewaySubregular)
                var maxPrice by remember { mutableStateOf<String>("") }
                OutlinedTextField(
                    modifier = Modifier
                        .width(100.dp)
                        .padding(horizontal = 10.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text(text = "1000.0₽", style = ralewaySubregular) },
                    value = maxPrice,
                    onValueChange = {
                        maxPrice = it
                        if (maxPrice.isNotBlank()) {
                            filterBuilder.maxPrice(try { maxPrice.toDouble() } catch (_: Exception) { hasError = true; null })
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = lightGrayBackground,
                        unfocusedIndicatorColor = lightGrayBackground
                    ),
                    textStyle = ralewayRegular
                )
            }

            Text(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp),
                text = "Размер",
                style = ralewaySubtitle
            )
            val sizesRus = getAllSizesRus(36.5, 44.5, 0.5)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                if (sizesRus.isEmpty()) {
                    Card(shape = RoundedCornerShape(23.dp), modifier = Modifier.height(50.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(0.dp)) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp), contentAlignment = Alignment.Center) {
                            Text(text = stringResource(R.string.no_sizes), style = ralewayRegular)
                        }
                    }
                    filterBuilder.size(null)
                    return@Column
                }

                sizesRus.forEach { size ->
                    Chip(
                        onClick = {
                            if (filterBuilder.sizesValue?.contains(size) == true) {
                                filterBuilder.removeSize(size)
                                return@Chip
                            }
                            filterBuilder.addSize(size)
                        },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = Color.White
                        ),
                        modifier = Modifier
                            .size(50.dp)
                            .border(
                                width = if (filterBuilder.sizesValue?.contains(size) == true) 2.dp else 0.dp,
                                color = if (filterBuilder.sizesValue?.contains(size) == true) blueGradientStart else Color.Transparent,
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {
                        Text(text = size.toString(), style = ralewaySubtitle)
                    }
                }
            }
        }
    }
}