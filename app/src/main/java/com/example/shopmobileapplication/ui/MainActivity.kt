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
import com.example.shopmobileapplication.ui.main.BucketScreen
import com.example.shopmobileapplication.ui.main.CategoriesLayout
import com.example.shopmobileapplication.ui.main.Main
import com.example.shopmobileapplication.ui.main.ProductDetailsScreen
import com.example.shopmobileapplication.ui.main.menu.BottomMenuItem
import com.example.shopmobileapplication.ui.main.search.FiltersScreen
import com.example.shopmobileapplication.ui.theme.ShopMobileApplicationTheme
import com.example.shopmobileapplication.ui.theme.whiteGreyBackground
import com.example.shopmobileapplication.viewmodel.UserViewModel
import com.example.shopmobileapplication.viewmodel.UserViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!, factory =
                UserViewModelFactory(UserRepositoryImpl(LocalContext.current, SupabaseClient.client)
            ))

            val mainNavController = rememberNavController()

            ShopMobileApplicationTheme {
                NavHost(
                    modifier = Modifier
                        .background(whiteGreyBackground)
                        .fillMaxSize(),
                    navController = mainNavController,
                    startDestination = Layouts.GREETING_LAYOUT
                ) {
                    composable(Layouts.GREETING_LAYOUT) {
                        Greetings(
                            navController = mainNavController
                        )
                    }
                    composable(Layouts.ONBOARD_LAYOUT) {
                        Onboard {
                            mainNavController.navigate(Layouts.SIGN_IN_LAYOUT)
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
                            navController = mainNavController
                        )
                    }
                    composable(Layouts.BUCKET_LAYOUT) {
                        BucketScreen(
                            navController = mainNavController
                        )
                    }
                    composable(Layouts.DETAILS_SCREEN + "/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")
                        if (productId != null) {
                            ProductDetailsScreen(productId = productId, navController = mainNavController)
                        }
                    }
                    composable(Layouts.FILTERS_SCREEN) {
                        FiltersScreen(navController = mainNavController)
                    }
                }
            }
        }
    }
}

