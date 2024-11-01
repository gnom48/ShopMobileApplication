package com.example.shopmobileapplication.ui.main.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.ui.theme.favoriteIconRed
import com.example.shopmobileapplication.ui.theme.favoriteRed
import com.example.shopmobileapplication.ui.theme.red
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground

@Composable
@Preview
fun BackIconButtonPreview() {
    BackIconButton { }
}

@Composable
fun BackIconButton(click: () -> Unit) {
    IconButton(
        onClick = {
            click()
        }
    ) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = CircleShape,
            color = Color.White
        ) {
            Icon(
                modifier = Modifier.size(32.dp).padding(4.dp),
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.back),
                tint = Color.Black
            )
        }
    }
}

@Composable
@Preview
fun FavoriteIconButtonPreview() {
    FavoriteIconButton(selected = false) { }
}

@Composable
fun FavoriteIconButton(modifier: Modifier = Modifier, selected: Boolean = false, click: () -> Unit) {
    IconButton(
        onClick = {
            click()
        }
    ) {
        Surface(
            modifier = modifier,
            shape = CircleShape,
            color = if (!selected) Color.White else whiteGreyBackground
        ) {
            Icon(
                modifier = Modifier.size(36.dp).padding(5.dp),
                //painter = painterResource(id = R.drawable.bottom_menu_icon_favorite),
                imageVector = if (!selected) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                contentDescription = stringResource(R.string.favorites),
                tint = if (!selected) Color.Black else red
            )
        }
    }
}

@Composable
@Preview
fun BucketIconButtonPreview() {
    BucketIconButton { }
}

@Composable
fun BucketIconButton(click: () -> Unit) {
    IconButton(
        onClick = {
            click()
        }
    ) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = CircleShape,
            color = Color.White
        ) {
            Icon(
                modifier = Modifier.size(36.dp).padding(6.dp),
                painter = painterResource(id = R.drawable.bucket_icon),
                contentDescription = stringResource(R.string.back),
                tint = Color.Black
            )
        }
    }
}

@Composable
@Preview
fun DriverMenuIconButtonPreview() {
    DriverMenuIconButton { }
}

@Composable
fun DriverMenuIconButton(click: () -> Unit) {
    IconButton(
        onClick = {
            click()
        }
    ) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = CircleShape,
            color = Color.Transparent
        ) {
            Image(
                modifier = Modifier.size(36.dp).padding(4.dp),
                painter = painterResource(id = R.drawable.hamburger_icon),
                contentDescription = stringResource(R.string.back)
            )
        }
    }
}