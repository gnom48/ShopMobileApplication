package com.example.shopmobileapplication.ui.main.menu

import com.example.shopmobileapplication.R

open class DrawerMenuItem(
    override val title: String,
    override val iconId: Int,
    override val route: String
): NavigationItem() {
    object OrdersScreen: BottomMenuItem("Заказы", R.drawable.orders, "OrdersScreen")
    object SettingsScreen: BottomMenuItem("Настройки", R.drawable.settings, "SettingsScreen")
    object Exit: BottomMenuItem("Выход", R.drawable.signout, "Exit")
}