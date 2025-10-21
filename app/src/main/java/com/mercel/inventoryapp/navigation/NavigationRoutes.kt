package com.mercel.inventoryapp.navigation

sealed class NavigationRoutes(val route: String) {
    object Login : NavigationRoutes("login")
    object Home : NavigationRoutes("home")
    object ItemList : NavigationRoutes("item_list/{category}") {
        fun createRoute(category: String) = "item_list/$category"
    }
    object ProductDetail : NavigationRoutes("product_detail/{itemId}") {
        fun createRoute(itemId: Int) = "product_detail/$itemId"
    }
    object Settings : NavigationRoutes("settings")
}