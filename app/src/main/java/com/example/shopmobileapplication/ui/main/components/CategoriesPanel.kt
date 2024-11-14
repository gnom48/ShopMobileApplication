package com.example.shopmobileapplication.ui.main.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.ProductCategory
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.greyText
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle

object CategoriesHelper {
    final val defaultCategoriesList: List<ProductCategory> = listOf(ProductCategory(0, "Все"))
    final val defaultSelectedCategory: ProductCategory = ProductCategory(0, "Все")
}

@Composable
@Preview
fun CategoriesPanelPreview() {
    CategoriesPanel(null) { _ -> }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoriesPanel(
    selectedCategory: ProductCategory?,
    categories: List<ProductCategory> = CategoriesHelper.defaultCategoriesList,
    onItemClickListener: (category: ProductCategory) -> Unit
) {
    var singleSelectedItem by remember {
        mutableStateOf(selectedCategory)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.categories), style = ralewaySubtitle, modifier = Modifier.padding(horizontal = 10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentPadding = PaddingValues(5.dp)
        ) {
            items(categories) { category: ProductCategory ->
                Chip(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    onClick = {
                        singleSelectedItem = category
                        onItemClickListener(category)
                    },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = if (singleSelectedItem != null) if (singleSelectedItem!!.id == category.id) blueGradientStart else Color.White else Color.White,
                        disabledBackgroundColor = Color.LightGray
                    )
                ) {
                    Text(
                        text = category.name,
                        style = ralewaySubregular,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        color = if (singleSelectedItem != null) if (singleSelectedItem!!.id == category.id) Color.White else greyText else greyText
                    )
                }
            }
        }
    }
}