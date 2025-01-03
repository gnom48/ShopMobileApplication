package com.example.shopmobileapplication.ui.main

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopmobileapplication.ui.main.components.ModalBottomSheetProductSizes
import com.example.shopmobileapplication.ui.main.menu.BottomMenuItem
import com.example.shopmobileapplication.ui.main.menu.DrawerMenuContent
import com.example.shopmobileapplication.ui.main.menu.MainBottomNavigation
import com.example.shopmobileapplication.ui.theme.blueGradientStart
import com.example.shopmobileapplication.ui.theme.lightGrayBackground
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground

@Preview
@Composable
fun MainPreview() {
    Main(null)
}

@Composable
fun Main(
    mainNavController: NavController?
) {
    val context = LocalContext.current
    val bottomMenuNavController = rememberNavController()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isTablet = configuration.screenWidthDp >= 600

    var onHideDrawerMenu by remember { mutableStateOf<suspend () -> Unit>({}) }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = whiteGreyBackground
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(lightGrayBackground),
            topBar = { },
            floatingActionButton = {
                if (!isTablet && !isLandscape) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        color = Color.Transparent
                    ) {
                        MainBottomNavigation(navController = bottomMenuNavController, mainNavController = mainNavController, onHideDrawerMenu = onHideDrawerMenu)
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = { }
        ) {
            var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val drawerScope = rememberCoroutineScope()

            if (isLandscape || isTablet) {
                drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
                ModalBottomSheetProductSizes { onShow, onHide ->
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(0.3f)
                                .background(blueGradientStart)
                        ) {
                            DrawerMenuContent(
                                navController = mainNavController,
                                bottomNavController = bottomMenuNavController,
                                drawerScope = drawerScope,
                                drawerState = drawerState
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(0.7f)
                                .background(lightGrayBackground)
                        ) {
                            NavHost(modifier = Modifier.fillMaxSize(), navController = bottomMenuNavController, startDestination = BottomMenuItem.HomeScreen.route) {
                                composable(BottomMenuItem.HomeScreen.route) {
                                    HomeScreen(navController = mainNavController, bottomNavController = bottomMenuNavController, onShowModalBottomSheetProductSizes = onShow, onHideModalBottomSheetProductSizes = onHide, drawerMenuState = null, drawerMenuScope = null)
                                }
                                composable(BottomMenuItem.FavoritesScreen.route) {
                                    FavoritesScreen(navController = mainNavController)
                                }
                                composable(BottomMenuItem.NotificationsScreen.route) {
                                    NotificationsScreen()
                                }
                                composable(BottomMenuItem.ProfileScreen.route) {
                                    ProfileScreen(mainNavController)
                                }
                            }
                        }
                    }
                }
            } else {
                ModalBottomSheetProductSizes { onShow, onHide ->
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet(
                                modifier = Modifier.fillMaxSize(),
                                drawerContainerColor = blueGradientStart,
                                drawerShape = RectangleShape
                            ) {
                                DrawerMenuContent(
                                    navController = mainNavController,
                                    bottomNavController = bottomMenuNavController,
                                    drawerScope = drawerScope,
                                    drawerState = drawerState
                                )
                                onHideDrawerMenu = {
                                    drawerState.close()
                                }
                            }
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(it)
                                .background(lightGrayBackground)
                        ) {
                            NavHost(modifier = Modifier.fillMaxSize(), navController = bottomMenuNavController, startDestination = BottomMenuItem.HomeScreen.route) {
                                composable(BottomMenuItem.HomeScreen.route) {
                                    HomeScreen(navController = mainNavController, bottomNavController = bottomMenuNavController, onShowModalBottomSheetProductSizes = onShow, onHideModalBottomSheetProductSizes = onHide, drawerMenuState = drawerState, drawerMenuScope = drawerScope)
                                }
                                composable(BottomMenuItem.FavoritesScreen.route) {
                                    FavoritesScreen(navController = mainNavController)
                                }
                                composable(BottomMenuItem.NotificationsScreen.route) {
                                    NotificationsScreen()
                                }
                                composable(BottomMenuItem.ProfileScreen.route) {
                                    ProfileScreen(mainNavController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}