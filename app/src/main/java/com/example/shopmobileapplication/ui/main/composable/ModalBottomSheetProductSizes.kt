package com.example.shopmobileapplication.ui.main.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Bucket
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.data.ProductSize
import com.example.shopmobileapplication.data.bucket.BucketRepositoryImpl
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.product.ProductRepositoryImpl
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewayOnButton
import com.example.shopmobileapplication.viewmodel.BucketViewModel
import com.example.shopmobileapplication.viewmodel.BucketViewModelFactory
import com.example.shopmobileapplication.viewmodel.ProductViewModel
import com.example.shopmobileapplication.viewmodel.ProductViewModelFactory
import com.example.shopmobileapplication.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
@Preview
fun ModalBottomSheetProductSizesPreview() {
    ModalBottomSheetProductSizes() { _,_ -> }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ModalBottomSheetProductSizes(
    bucketViewModel: BucketViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = BucketViewModelFactory(
        BucketRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    ),
    productViewModel: ProductViewModel = remember {
        ProductViewModelFactory(
            ProductRepositoryImpl(null, SupabaseClient.client)
        ).create(ProductViewModel::class.java)
    },
    content: @Composable (onShow: (product: Product?) -> Unit, onHide: () -> Unit) -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var selectedProductColorSize by remember { mutableStateOf<ProductSize?>(null) }

    LaunchedEffect(selectedProduct) {
        selectedProduct?.let { productViewModel.getProductsSizes(it.id) }
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetState = bottomSheetState,
        sheetContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                selectedProduct?.let { p ->
                    SizeColorChipsRow(productSizes = productViewModel.productSizes, onSelected = { _, ps -> ProductSize
                        selectedProductColorSize = ps
                    })
                }

                if (selectedProductColorSize != null) {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(10.dp, 20.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        contentPadding = PaddingValues(10.dp, 10.dp),
                        colors = ButtonDefaults.buttonColors(blueGradientStart),
                        onClick = {
                            scope.launch {
                                if (selectedProductColorSize != null) {
                                    bucketViewModel.addProductToBucket(
                                        Bucket(
                                            userId = UserViewModel.currentUser.id,
                                            productExampleId = selectedProductColorSize!!.id,
                                            quantity = 1
                                        )
                                    )
                                }
                                bottomSheetState.hide()
                            }
                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 5.dp),
                            text = stringResource(R.string.confirm),
                            style = ralewayOnButton
                        )
                    }
                }
            }
        }
    ) {
        content(
            onShow = { product ->
                selectedProduct = product
                scope.launch { bottomSheetState.show() }
            },
            onHide = {
                scope.launch { bottomSheetState.hide() }
            }
        )
    }
}