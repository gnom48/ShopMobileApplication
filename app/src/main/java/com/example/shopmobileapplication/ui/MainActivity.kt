package com.example.shopmobileapplication.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopmobileapplication.data.network.SupabaseClient
import com.example.shopmobileapplication.data.user.UserRepositoryImpl
import com.example.shopmobileapplication.ui.auth.SignIn
import com.example.shopmobileapplication.ui.auth.SignUp
import com.example.shopmobileapplication.ui.greetings.Greetings
import com.example.shopmobileapplication.ui.greetings.Onboard
import com.example.shopmobileapplication.ui.main.AllOrdersScreen
import com.example.shopmobileapplication.ui.main.BucketScreen
import com.example.shopmobileapplication.ui.main.CategoriesLayout
import com.example.shopmobileapplication.ui.main.Main
import com.example.shopmobileapplication.ui.main.OrderDetailsScreen
import com.example.shopmobileapplication.ui.main.ProductDetailsScreen
import com.example.shopmobileapplication.ui.main.components.PdfViewer
import com.example.shopmobileapplication.ui.main.menu.BottomMenuItem
import com.example.shopmobileapplication.ui.main.menu.DrawerMenuItem
import com.example.shopmobileapplication.ui.main.search.FiltersScreen
import com.example.shopmobileapplication.ui.theme.ShopMobileApplicationTheme
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.ui.viewmodel.UserViewModel
import com.example.shopmobileapplication.ui.viewmodel.UserViewModelFactory
import com.example.shopmobileapplication.utils.SharedPreferecesHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory =
                UserViewModelFactory(UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
            )
            )

            val mainNavController = rememberNavController()
            val context = LocalContext.current

            ShopMobileApplicationTheme {
                NavHost(
                    modifier = Modifier.fillMaxSize().background(whiteGreyBackground),
                    navController = mainNavController,
                    startDestination = Layouts.GREETING_LAYOUT
                ) {
                    composable(Layouts.GREETING_LAYOUT) {
                        Greetings(
                            navController = mainNavController
                        )
                    }
                    composable(Layouts.ONBOARD_LAYOUT) {
                        if (SharedPreferecesHelper(context).getStringData(SharedPreferecesHelper.seenOnBoardKey) != null) {
                            mainNavController.navigate(Layouts.SIGN_IN_LAYOUT) {
                                popUpTo(Layouts.ONBOARD_LAYOUT) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else {
                            Onboard {
                                mainNavController.navigate(Layouts.SIGN_IN_LAYOUT) {
                                    popUpTo(Layouts.ONBOARD_LAYOUT) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                    composable(Layouts.SIGN_IN_LAYOUT) {
                        SignIn(
                            navController = mainNavController
                        )
                    }
                    composable(Layouts.SIGN_UP_LAYOUT) {
                        SignUp(
                            navController = mainNavController
                        )
                    }
                    composable(Layouts.MAIN_LAYOUT) {
                        Main (
                            mainNavController = mainNavController
                        )
                    }
                    composable(Layouts.CATEGORIES_LAYOUT) {
                        CategoriesLayout (
                            navController = mainNavController
                        )
                    }
                    composable(BottomMenuItem.BucketScreen.route) {
                        BucketScreen(
                            navController = mainNavController,
                            supabaseViewModel = viewModel()
                        )
                    }
                    composable(Layouts.BUCKET_LAYOUT) {
                        BucketScreen(
                            navController = mainNavController,
                            supabaseViewModel = viewModel()
                        )
                    }
                    composable(Layouts.DETAILS_SCREEN + "/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")
                        if (productId != null) {
                            ProductDetailsScreen(productId = productId, navController = mainNavController)
                        }
                    }
                    composable(DrawerMenuItem.OrdersScreen.route) {
                        AllOrdersScreen(navController = mainNavController)
                    }
                    composable(Layouts.ORDER_DETAILS_SCREEN + "/{orderId}/PRODUCT/{productExampleId}") { backStackEntry ->
                        val orderId = backStackEntry.arguments?.getString("orderId")
                        val productExampleId = try { backStackEntry.arguments?.getString("productExampleId")!!.toInt() } catch (e: Exception) { null }
                        if (orderId != null && productExampleId != null) {
                            OrderDetailsScreen(orderId = orderId, productSizeId = productExampleId, navController = mainNavController)
                        }
                    }
                    composable(Layouts.FILTERS_SCREEN) {
                        FiltersScreen(navController = mainNavController)
                    }
                    composable(Layouts.POLITICS_VIEWER) {
                        PdfViewer("policits.pdf")
                    }
                }
            }
        }
    }
}

