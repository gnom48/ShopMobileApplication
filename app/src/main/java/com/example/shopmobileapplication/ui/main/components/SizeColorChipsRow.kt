package com.example.shopmobileapplication.ui.main.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.ProductSize
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle

@Preview
@Composable
fun SizeColorChipPreview() {
    SizeColorChipsRow(productSizes = listOf(), onSelected = { _, _ -> })
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SizeColorChipsRow(productSizes: List<ProductSize>, onSelected: (Int, ProductSize) -> Unit) {
    var selectedSize by remember { mutableStateOf<Double?>(null) }
    var selectedColor by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth()){
        val uniqueSizes = productSizes.map { it.sizeRus }.distinct().sorted()
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(16.dp).fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            if (uniqueSizes.isEmpty()) {
                Card(shape = RoundedCornerShape(23.dp), modifier = Modifier.height(50.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(0.dp)) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.no_sizes), style = ralewayRegular)
                    }
                }
                selectedSize = null
                return@Column
            }

            uniqueSizes.forEach { size ->
                Chip(
                    onClick = {
                        if (selectedSize == size) {
                            selectedSize = null
                            selectedColor = null
                            return@Chip
                        }
                        selectedSize = size
                        selectedColor = null
                    },
                    colors = ChipDefaults.chipColors(
                        backgroundColor = Color.White
                    ),
                    modifier = Modifier
                        .size(50.dp)
                        .border(
                            width = if (selectedSize == size) 2.dp else 0.dp,
                            color = if (selectedSize == size) blueGradientStart else Color.Transparent,
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Text(text = size.toString(), style = ralewaySubtitle)
                }
            }
        }

        if (selectedSize != null) {
            val uniqueColors = productSizes.filter { it.sizeRus == selectedSize }.map { it.color }.distinct()
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp).fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                if (uniqueColors.isEmpty()) {
                    Card(shape = RoundedCornerShape(23.dp), modifier = Modifier.height(50.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(0.dp)) {
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp), contentAlignment = Alignment.Center) {
                            Text(text = stringResource(R.string.no_colors), style = ralewayRegular)
                        }
                    }
                }

                uniqueColors.forEach { color ->
                    Chip(
                        onClick = {
                            if (selectedColor == color) {
                                selectedColor = null
                                return@Chip
                            }
                            selectedColor = color

                            val selectedProductSize = productSizes.find { it.sizeRus == selectedSize && it.color == selectedColor }
                            if (selectedProductSize != null) {
                                onSelected(productSizes.indexOf(selectedProductSize), selectedProductSize)
                            }
                        },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = try {
                                val hexColorString = "#" + color.substring(2)
                                Color(android.graphics.Color.parseColor(hexColorString))
                            } catch (e: Exception) {
                               Color.Blue
                            },
                            contentColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .size(50.dp)
                            .border(
                                width = if (selectedColor == color) 2.dp else 0.dp,
                                color = if (selectedColor == color) blueGradientStart else Color.Transparent,
                                shape = RoundedCornerShape(24.dp)
                            )
                    ) {

                    }
                }
            }
        }
    }
}
