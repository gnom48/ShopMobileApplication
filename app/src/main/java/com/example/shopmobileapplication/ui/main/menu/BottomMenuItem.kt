package com.example.shopmobileapplication.ui.main.menu

import com.example.shopmobileapplication.R

open class BottomMenuItem(
    val title: String,
    val iconId: Int,
    val route: String
) {
    object HomeScreen: BottomMenuItem("Home", R.drawable.bottom_menu_icon_home, "HomeScreen")
    object FavoritesScreen: BottomMenuItem("Favotite", R.drawable.bottom_menu_icon_favorite, "FavoritesScreen")
    object BucketScreen: BottomMenuItem("Bucket", R.drawable.bucket_icon, "BucketScreen")
    object NotificationsScreen: BottomMenuItem("Notifications", R.drawable.bottom_menu_icon_notifications, "NotificationsScreen")
    object ProfileScreen: BottomMenuItem("Profile", R.drawable.bottom_menu_icon_profile, "ProfileScreen")

    companion object {
        const val DetailsScreen: String = "Details"
    }
}