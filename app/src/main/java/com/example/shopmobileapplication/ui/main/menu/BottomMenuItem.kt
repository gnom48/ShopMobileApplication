package com.example.shopmobileapplication.ui.main.menu

import com.example.shopmobileapplication.R

open class BottomMenuItem(
    override val title: String,
    override val iconId: Int,
    override val route: String
): NavigationItem() {
    object HomeScreen: BottomMenuItem("Главная", R.drawable.bottom_menu_icon_home, "HomeScreen")
    object FavoritesScreen: BottomMenuItem("Избранное", R.drawable.bottom_menu_icon_favorite, "FavoritesScreen")
    object BucketScreen: BottomMenuItem("Корзина", R.drawable.bucket_icon, "BucketScreen")
    object NotificationsScreen: BottomMenuItem("Уведомления", R.drawable.bottom_menu_icon_notifications, "NotificationsScreen")
    object ProfileScreen: BottomMenuItem("Профиль", R.drawable.bottom_menu_icon_profile, "ProfileScreen")
}

