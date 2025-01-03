package com.example.shopmobileapplication.ui.main.menu

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.Layouts
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.ralewayRegular
import com.example.shopmobileapplication.ui.theme.ralewayTitle
import com.example.shopmobileapplication.ui.viewmodel.SupabaseViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@Preview
fun DrawerMenuContentPreview() {
    DrawerMenuContent(null, null, null, null)
}

@Composable
fun DrawerMenuContent(
    navController: NavController?,
    bottomNavController: NavController?,
    drawerScope: CoroutineScope?,
    drawerState: DrawerState?,
    userViewModel: UserViewModel = viewModel(
        viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory = UserViewModelFactory(
            UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
        )
    ),
    supabaseViewModel: SupabaseViewModel = viewModel()
) {
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(blueGradientStart),
            contentAlignment = Alignment.CenterEnd
        ) {
            val configuration = LocalConfiguration.current
            val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
            val isTablet = configuration.screenWidthDp >= 600
            if (!isTablet && !isLandscape) {
                Image(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(vertical = 100.dp)
                        .clickable {
                            drawerScope?.launch {
                                drawerState?.close()
                            }
                        },
                    painter = painterResource(id = R.drawable.app_preview),
                    contentDescription = "preview",
                    contentScale = ContentScale.FillHeight
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 20.dp, vertical = 40.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            val navigationItems = listOf<NavigationItem>(
                BottomMenuItem.ProfileScreen,
                BottomMenuItem.HomeScreen,
                BottomMenuItem.BucketScreen,
                BottomMenuItem.FavoritesScreen,
                DrawerMenuItem.OrdersScreen,
                BottomMenuItem.NotificationsScreen,
                DrawerMenuItem.SettingsScreen,
                DrawerMenuItem.Exit
            )
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                item {
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        color = Color.LightGray
                    ) {
                        var avatarUrl by remember { mutableStateOf<String?>(null) }
                        LaunchedEffect(Unit) {
                            supabaseViewModel.getSignedUrlFromPrivateBucket(UserViewModel.currentUser.id, UserViewModel.currentUser.image.toString()) { url: String? ->
                                avatarUrl = url
                            }
                        }
                        AsyncImage(
                            model = avatarUrl,
                            error = painterResource(R.drawable.avatar_default_icon),
                            contentDescription = "avatar",
                            contentScale = ContentScale.Crop
                        )
                    }
                    Text(text = UserViewModel.currentUser.name, style = ralewayTitle, color = Color.White, modifier = Modifier.padding(10.dp))

                }
                items(navigationItems) { item: NavigationItem ->
                    if (item is DrawerMenuItem.Exit) {
                        HorizontalDivider(color = Color.LightGray, modifier = Modifier
                            .width(80.dp)
                            .padding(start = 20.dp)
                            .padding(vertical = 10.dp))
                    }
                    Button(
                        onClick = {
                            try {
                                when(item) {
                                    is BottomMenuItem.ProfileScreen, is BottomMenuItem.FavoritesScreen, is BottomMenuItem.NotificationsScreen, is BottomMenuItem.HomeScreen -> {
                                        bottomNavController!!.navigate(item.route) {
                                            launchSingleTop = true
                                        }
                                        drawerScope?.launch {
                                            drawerState?.close()
                                        }                                }
                                    is DrawerMenuItem.OrdersScreen, is DrawerMenuItem.SettingsScreen, is BottomMenuItem.BucketScreen -> {
                                        navController!!.navigate(item.route) {
                                            launchSingleTop = true
                                        }
                                        drawerScope?.launch {
                                            drawerState?.close()
                                        }
                                    }
                                    is DrawerMenuItem.Exit -> {
                                        userViewModel.signOut()
                                        navController!!.navigate(Layouts.SIGN_IN_LAYOUT) {
                                            popUpTo(Layouts.MAIN_LAYOUT) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                        drawerScope?.launch {
                                            drawerState?.close()
                                        }
                                    }
                                    else -> { }
                                }
                            } catch (_: Exception) { }

                        },
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Row {
                            Icon(painter = painterResource(id = item.iconId), contentDescription = item.title, tint = Color.White, modifier = Modifier.size(18.dp))
                            Text(text = item.title, style = ralewayRegular, color = Color.White, modifier = Modifier.padding(start = 20.dp))
                        }
                    }
                }
            }

//            navigationItems.forEach { item: NavigationItem ->
//                if (item is DrawerMenuItem.Exit) {
//                    HorizontalDivider(color = Color.LightGray, modifier = Modifier.padding(20.dp))
//                }
//                Button(
//                    onClick = {
//                        try {
//                            when(item) {
//                                is BottomMenuItem.ProfileScreen, is BottomMenuItem.FavoritesScreen, is BottomMenuItem.NotificationsScreen -> {
//                                    bottomNavController!!.navigate(item.route) {
//                                        launchSingleTop = true
//                                    }
//                                    drawerScope?.launch {
//                                        drawerState?.close()
//                                    }                                }
//                                is DrawerMenuItem.OrdersScreen, is DrawerMenuItem.SettingsScreen, is BottomMenuItem.BucketScreen -> {
//                                    navController!!.navigate(item.route) {
//                                        launchSingleTop = true
//                                    }
//                                    drawerScope?.launch {
//                                        drawerState?.close()
//                                    }
//                                }
//                                is DrawerMenuItem.Exit -> {
//                                    userViewModel.signOut()
//                                    navController!!.navigate(Layouts.SIGN_IN_LAYOUT) {
//                                        popUpTo(Layouts.MAIN_LAYOUT) {
//                                            inclusive = true
//                                        }
//                                        launchSingleTop = true
//                                    }
//                                    drawerScope?.launch {
//                                        drawerState?.close()
//                                    }
//                                }
//                                else -> { }
//                            }
//                        } catch (_: Exception) { }
//
//                    },
//                    colors = ButtonDefaults.buttonColors(Color.Transparent)
//                ) {
//                    Row {
//                        Icon(painter = painterResource(id = item.iconId), contentDescription = item.title, tint = Color.White, modifier = Modifier.size(18.dp))
//                        Text(text = item.title, style = ralewayRegular, color = Color.White, modifier = Modifier.padding(start = 20.dp))
//                    }
//                }
//            }
        }
    }
}
