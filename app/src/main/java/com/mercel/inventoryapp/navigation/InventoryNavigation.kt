package com.mercel.inventoryapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mercel.inventoryapp.data.model.ItemCategory
import com.mercel.inventoryapp.features.home.screen.HomeScreen
import com.mercel.inventoryapp.features.itemlist.screen.ItemListScreen
import com.mercel.inventoryapp.features.login.screen.LoginScreen
import com.mercel.inventoryapp.features.productdetail.screen.ProductDetailScreen
import com.mercel.inventoryapp.features.settings.screen.SettingsScreen

@Composable
fun InventoryNavigation(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationRoutes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavigationRoutes.Home.route) {
                        popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(NavigationRoutes.Home.route) {
            HomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(NavigationRoutes.ItemList.createRoute(category.name))
                },
                onSettingsClick = {
                    navController.navigate(NavigationRoutes.Settings.route)
                }
            )
        }
        
        composable(
            route = NavigationRoutes.ItemList.route,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryString = backStackEntry.arguments?.getString("category") ?: "ELECTRONICS"
            val category = ItemCategory.valueOf(categoryString)
            ItemListScreen(
                category = category,
                onItemClick = { itemId ->
                    navController.navigate(NavigationRoutes.ProductDetail.createRoute(itemId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(
            route = NavigationRoutes.ProductDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: 0
            ProductDetailScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(NavigationRoutes.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(NavigationRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}