package com.example.shopmobileapplication.ui.main.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shopmobileapplication.R
import com.example.shopmobileapplication.ui.theme.mainMenuIconSelected
import com.example.shopmobileapplication.ui.theme.mainMenuIconUnselected
import kotlinx.coroutines.launch

@Composable
@Preview
fun BottomNavigationPreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { },
        bottomBar = {
            Box(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                MainBottomNavigation(navController = null, mainNavController = null) {}
            }
        }
    ) {
        Box(
            modifier = Modifier.padding(it)
        ) {

        }
    }
}

@Composable
fun MainBottomNavigation(
    navController: NavController?,
    mainNavController: NavController?,
    onHideDrawerMenu: suspend () -> Unit
) {
    val bottomNavigationItems = listOf(
        BottomMenuItem.HomeScreen,
        BottomMenuItem.FavoritesScreen,
        BottomMenuItem.BucketScreen,
        BottomMenuItem.NotificationsScreen,
        BottomMenuItem.ProfileScreen
    )

    val scope = rememberCoroutineScope()
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box (
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                painter = painterResource(R.drawable.rounded_bottom_menu_res),
                contentDescription = "Menu",
                contentScale = ContentScale.FillBounds
            )
        }

        BottomNavigation(
            modifier = Modifier.height(70.dp),
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        ) {
            val backStackEntry by navController!!.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route
            bottomNavigationItems.forEach { item: BottomMenuItem ->
                if (item !is BottomMenuItem.BucketScreen) {
                    BottomNavigationItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            navController!!.navigate(item.route) {
                                launchSingleTop = true
                            }
                            scope.launch {
                                onHideDrawerMenu()
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(item.iconId),
                                contentDescription = item.title,
                                modifier = Modifier.size(22.dp)
                            )
                        },
                        alwaysShowLabel = false,
                        selectedContentColor = mainMenuIconSelected,
                        unselectedContentColor = mainMenuIconUnselected,
                    )
                } else {
                    Spacer(modifier = Modifier.size(40.dp))
                }
            }
        }

        IconButton(
            modifier = Modifier
                .padding(bottom = 50.dp)
                .background(shape = CircleShape, color = mainMenuIconSelected),
            onClick = {
                mainNavController!!.navigate(BottomMenuItem.BucketScreen.route) {
                    launchSingleTop = true
                }
            }
        ) {
            Icon(
                painter = painterResource(BottomMenuItem.BucketScreen.iconId),
                contentDescription = BottomMenuItem.BucketScreen.title,
                tint = Color.White,
                modifier = Modifier
                    .size(34.dp)
                    .padding(5.dp)
            )
        }
    }
}