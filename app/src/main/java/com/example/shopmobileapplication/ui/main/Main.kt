package com.example.shopmobileapplication.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopmobileapplication.viewmodel.SupabaseViewModel
import com.example.shopmobileapplication.ui.main.menu.BottomMenuItem
import com.example.shopmobileapplication.ui.main.menu.MainBottomNavigation
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground

@Preview
@Composable
fun MainPreview() {
    Main(null)
}

@Composable
fun Main(
    mainNavController: NavController?,
    viewModel: SupabaseViewModel = viewModel()
) {
    val context = LocalContext.current
    val bottomMenuNavController = rememberNavController()

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = whiteGreyBackground
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            topBar = { },
            bottomBar = {
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .background(Color.Transparent)
                ) {
                    MainBottomNavigation(navController = bottomMenuNavController, mainNavController = mainNavController)
                }
            }
        ) {
            Box(
                modifier = Modifier.padding(it)
            ) {
                NavHost(modifier = Modifier.fillMaxSize(), navController = bottomMenuNavController, startDestination = BottomMenuItem.HomeScreen.route) {
                    composable(BottomMenuItem.HomeScreen.route) {
                        HomeScreen(navController = mainNavController)
                    }
                    composable(BottomMenuItem.FavoritesScreen.route) {
                        FavoritesScreen(navController = mainNavController)
                    }
                    composable(BottomMenuItem.NotificationsScreen.route) {
                        Text("notifications")
                    }
                    composable(BottomMenuItem.ProfileScreen.route) {
                        Text("profile")
                    }
//                    composable(BottomMenuItem.BucketScreen.route) {
//                        BucketScreen(navController = mainNavController)
//                    }
                }
            }
        }
    }
}