package com.example.shopmobileapplication.ui.main.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewayOnButton
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle

@Composable
@Preview
fun OrderPricePreview() {
    OrderPrice(753.95, 60.20) { }
}

@Composable
fun OrderPrice(orderPrice: Double, delivery: Double, onConfirm: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.order_sum), style = ralewaySubregular, modifier = Modifier.padding(5.dp))
            Text(text = "₽" + orderPrice.toString(), style = ralewayRegular, modifier = Modifier.padding(5.dp))
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.delivery), style = ralewaySubregular, modifier = Modifier.padding(5.dp))
            Text(text = "₽" + delivery.toString(), style = ralewayRegular, modifier = Modifier.padding(5.dp))
        }
        Image(painter = painterResource(id = R.drawable.punktir), contentDescription = "- - - - -", modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 5.dp), contentScale = ContentScale.FillWidth)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.result_coast), style = ralewaySubregular, modifier = Modifier.padding(5.dp))
            Text(text = "₽" + (delivery + orderPrice).toString(),
                style = ralewaySubtitle,
                color = blueGradientStart,
                modifier = Modifier.padding(5.dp)
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 5.dp, end = 5.dp, bottom = 5.dp, top = 10.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentPadding = PaddingValues(10.dp, 10.dp),
            colors = ButtonDefaults.buttonColors(blueGradientStart),
            onClick = {
                onConfirm()
            }
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = stringResource(R.string.lets_order),
                style = ralewayOnButton
            )
        }
    }
}
