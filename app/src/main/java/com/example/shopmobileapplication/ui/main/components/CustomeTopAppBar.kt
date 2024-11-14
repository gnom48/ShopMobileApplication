package com.example.shopmobileapplication.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground

@Preview
@Composable
fun CustomTopAppBarPreview() {
    CustomTopAppBar(
        title = stringResource(id = R.string.favorites),
        onBackButtonClick = { },
        actionIconButton = {
            FavoriteIconButton { }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    onBackButtonClick: () -> Unit,
    actionIconButton: @Composable () -> Unit = { Spacer(modifier = Modifier.size(42.dp)) }
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth().background(whiteGreyBackground),
        colors = TopAppBarDefaults.topAppBarColors(containerColor = whiteGreyBackground),
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = ralewaySubtitle
                )
            }
        },
        navigationIcon = {
            BackIconButton { onBackButtonClick() }
        },
        actions = {
            actionIconButton()
        }
    )
}