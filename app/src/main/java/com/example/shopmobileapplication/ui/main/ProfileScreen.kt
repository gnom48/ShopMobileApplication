package com.example.shopmobileapplication.ui.main

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.generateBarcode
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.main.components.BarCodeFullScreen
import com.example.shopmobileapplication.ui.main.components.CustomTopAppBar
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.lightGrayBackground
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModelFactory
import com.google.gson.Gson
import kotlinx.serialization.SerialName

@Composable
@Preview
fun ProfileScreenPreview() {
    ProfileScreen(null)
}

@Composable
fun ProfileScreen(
    navController: NavController?,
    userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
        UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    var barcodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var barcodeWidth by remember { mutableIntStateOf(500) }
    var showBarCodeFullScreen by remember { mutableStateOf(false) }

    LaunchedEffect(userViewModel.discountCard) {
        userViewModel.discountCard?.let {
            barcodeBitmap = generateBarcode(
                Gson().toJson(object {
                    @SerialName("card_id") val cardId = "d2534f56e6ge56745"
                    @SerialName("product_example_id") val userId = "4g56456g456g456g56g4"
                }),
                barcodeWidth,
                200
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightGrayBackground)
    ) {
        CustomTopAppBar(
            title = "     " + stringResource(R.string.profile),
            onBackButtonClick = {
                navController?.popBackStack()
            },
            actionIconButton = {
                TextButton(
                    modifier = Modifier.background(lightGrayBackground),
                    onClick = {
                        // TODO: save
                    }
                ) {
                    Text(text = stringResource(R.string.complete), style = ralewayRegular, color = blueGradientStart, modifier = Modifier.padding(5.dp))
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Color.LightGray
            ) {
                Image(
                    imageVector = Icons.Default.AccountCircle,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "avatar",
                    contentScale = ContentScale.Crop
                )
            }

            Text(text = UserViewModel.currentUser.name, style = ralewaySubtitle, color = Color.Black, modifier = Modifier.padding(10.dp))

            TextButton(
                modifier = Modifier.background(lightGrayBackground),
                onClick = {
                    // TODO: edit avatar
                }
            ) {
                Text(text = stringResource(R.string.edit_avatar), style = ralewayRegular, color = blueGradientStart, modifier = Modifier.padding(5.dp))
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { intSize: IntSize ->
                        barcodeWidth = intSize.width
                    },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (barcodeBitmap == null) {
                        Text(
                            modifier = Modifier.padding(10.dp),
                            text = stringResource(R.string.discount_card),
                            style = ralewaySubtitle,
                            overflow = TextOverflow.Ellipsis
                        )
                    } else {
                        Image(
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    showBarCodeFullScreen = true
                                },
                            bitmap = barcodeBitmap!!.asImageBitmap(),
                            contentDescription = "Barcode",
                        )
                    }
                }
            }

            Column(modifier = Modifier.fillMaxSize()) {
                var userName by remember { mutableStateOf(UserViewModel.currentUser.name) }
                var userAddress by remember { mutableStateOf("NO ADDRESS") }
//            var userAddress by remember { mutableStateOf(UserViewModel.currentUser.address) }
                var userPhone by remember { mutableStateOf(UserViewModel.currentUser.phone ?: "") }

                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.your_name),
                    style = ralewaySubtitle
                )
                OutlinedTextField(
                    modifier = Modifier
                        .border(BorderStroke(0.dp, Color.Transparent))
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp),
                            imageVector = if (!userName.isNullOrEmpty()) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = "OK",
                            tint = if (!userName.isNullOrEmpty()) blueGradientStart else Color.Red
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text(stringResource(R.string.your_name), style = ralewaySubregular) },
                    value = userName,
                    onValueChange = {
                        userName = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    textStyle = ralewayRegular
                )

                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.address),
                    style = ralewaySubtitle
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp),
                            imageVector = if (!userAddress.isNullOrEmpty()) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = "OK",
                            tint = if (!userAddress.isNullOrEmpty()) blueGradientStart else Color.Red
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text(stringResource(R.string.address), style = ralewaySubregular) },
                    value = userAddress,
                    onValueChange = {
                        userAddress = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    textStyle = ralewayRegular
                )

                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.phone),
                    style = ralewaySubtitle
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        Icon(
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp),
                            imageVector = if (!userPhone.isNullOrEmpty()) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = "OK",
                            tint = if (!userPhone.isNullOrEmpty()) blueGradientStart else Color.Red
                        )
                    },
                    shape = RoundedCornerShape(14.dp),
                    placeholder = { Text("8(800)555-35-35", style = ralewaySubregular) },
                    value = userPhone,
                    onValueChange = {
                        userPhone = it
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.LightGray
                    ),
                    textStyle = ralewayRegular
                )
            }
        }
    }

    if (showBarCodeFullScreen && barcodeBitmap != null) {
        BarCodeFullScreen(barcodeBitmap = barcodeBitmap) {
            showBarCodeFullScreen = false
        }
    }
}
