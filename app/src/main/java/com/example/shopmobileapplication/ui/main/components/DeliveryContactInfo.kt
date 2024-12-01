package com.example.shopmobileapplication.ui.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.theme.lightGrayBackground
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewaySubregular
import com.example.shopmobileapplication.ui.theme.ralewaySubtitle
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModelFactory
import io.github.jan.supabase.gotrue.gotrue

@Composable
fun DeliveryContactInfo(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
        UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
    )
    )
) {
    LaunchedEffect(Unit) {
        userViewModel.getCurrentUserInfo()
    }

    var enterEmail by remember { mutableStateOf(userViewModel.userInfo?.email) }
    var enterPhone by remember { mutableStateOf(userViewModel.userInfo?.phone) }

    LaunchedEffect(userViewModel.userInfo) {
        enterEmail = userViewModel.userInfo?.email
        enterPhone = userViewModel.userInfo?.phone
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(stringResource(R.string.contact_info), style = ralewayRegular)

            var enterEmailEnabled by remember { mutableStateOf(false) }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = whiteGreyBackground),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "email",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(1f)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text("xyz@gmail.com", style = ralewaySubregular) },
                        value = enterEmail ?: "",
                        onValueChange = {
                            enterEmail = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                enterEmailEnabled = false
                            }
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = lightGrayBackground,
                            unfocusedIndicatorColor = lightGrayBackground
                        ),
                        textStyle = ralewayRegular,
                        enabled = enterEmailEnabled
                    )
//                    Text(userViewModel.userInfo?.email ?: stringResource(R.string.enter_email), style = ralewaySubtitle, modifier = Modifier.padding(bottom = 5.dp))
                    Text("Email", style = ralewaySubregular)
                }
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            enterEmailEnabled = true
                        }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = "email",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            var enterPhoneEnabled by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = whiteGreyBackground),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Call,
                            contentDescription = "phone",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .weight(1f)
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 5.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        placeholder = { Text("8800555353", style = ralewaySubregular) },
                        value = enterPhone ?: "",
                        onValueChange = {
                            enterPhone = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                enterPhoneEnabled = false
                            }
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = lightGrayBackground,
                            unfocusedIndicatorColor = lightGrayBackground
                        ),
                        textStyle = ralewayRegular,
                        enabled = enterPhoneEnabled
                    )
                    Text(SupabaseClient.client.gotrue.currentUserOrNull()?.phone ?: stringResource(R.string.phone), style = ralewaySubtitle)//, modifier = Modifier.padding(bottom = 5.dp))
//                    Text(stringResource(R.string.phone), style = ralewaySubregular)
                }
                Box(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable {
                            enterPhoneEnabled = true
                        }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = "edit",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Text(stringResource(R.string.address), style = ralewayRegular)
            Text("1082 Аэропорт, Нигерии", style = ralewaySubregular)
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                contentScale = ContentScale.FillWidth,
                contentDescription = "map",
                painter = painterResource(id = R.drawable.map)
            )

            Text(stringResource(R.string.payment_method), style = ralewayRegular)
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                Card(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = whiteGreyBackground),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.card_icon),
                            contentDescription = "email",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Column(
                    modifier = Modifier.padding(end = 10.dp)
                ) {
//                            Text("DbL Card", style = ralewaySubtitle, modifier = Modifier.padding(bottom = 5.dp))
//                            Text("**** **** 0696 4629", style = ralewaySubregular)

                    Text(stringResource(R.string.pay_in_office), style = ralewaySubtitle, modifier = Modifier.padding(bottom = 5.dp))
                }
//                        Box(modifier = Modifier.padding(10.dp)) {
//                            Icon(
//                                imageVector = Icons.Outlined.KeyboardArrowDown,
//                                contentDescription = "more",
//                                modifier = Modifier.size(24.dp)
//                            )
//                        }
            }
        }
    }
}