package com.example.shopmobileapplication.ui.greetings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.theme.blueGradientEnd
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.raleway
import com.example.shopmobileapplication.viewmodel.UserViewModel
import com.example.shopmobileapplication.viewmodel.UserViewModelFactory
import kotlinx.coroutines.delay

@Preview
@Composable
fun GreetingsPreview() {
    Greetings(null)
}

@Composable
fun Greetings(
    navController: NavController?,
    userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
        UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
    ))
) {
    LaunchedEffect(Unit) {
        userViewModel.refreshSessionIfNeeds()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(blueGradientStart, blueGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "MATULE",
                fontFamily = raleway,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 62.sp
            )
            Text(
                modifier = Modifier.padding(10.dp),
                text = "ME",
                fontFamily = raleway,
                color = Color.White,
                fontWeight = FontWeight.Light,
                fontSize = 36.sp
            )
        }
    }

    var firstTime by remember { mutableStateOf(true) }
    LaunchedEffect(userViewModel.isLoading) {
        if (!userViewModel.isLoading && !firstTime) {
            if (userViewModel.user == null) {
                navController?.navigate(Layouts.ONBOARD_LAYOUT) {
                    popUpTo(Layouts.GREETING_LAYOUT) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            else {
                delay(300)
                navController?.navigate(Layouts.MAIN_LAYOUT) {
                    popUpTo(Layouts.GREETING_LAYOUT) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        } else {
            firstTime = false
        }
    }
}