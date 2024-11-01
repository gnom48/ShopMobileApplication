package com.example.shopmobileapplication.ui.main.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.Product
import com.example.shopmobileapplication.ui.main.ProductDetailsHelper

@Composable
@Preview
fun MiniProductContentPreview() {
    MiniProductContent(ProductDetailsHelper.defaultProduct) { }
}

@Composable
fun MiniProductContent(product: Product, onClick: () -> Unit) {
    Card(modifier = Modifier.size(80.dp).clickable { onClick() }, shape = RoundedCornerShape(23.dp), colors = CardDefaults.cardColors(
        Color.White), elevation = CardDefaults.cardElevation(0.dp)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = R.drawable.onboard_2_all), contentDescription = "image", modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
                contentScale = ContentScale.FillWidth)

        }
    }
}