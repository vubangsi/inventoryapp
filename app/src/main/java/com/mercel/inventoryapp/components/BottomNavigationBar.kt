package com.mercel.inventoryapp.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mercel.inventoryapp.navigation.NavigationRoutes

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == NavigationRoutes.Home.route,
            onClick = {
                if (currentRoute != NavigationRoutes.Home.route) {
                    navController.navigate(NavigationRoutes.Home.route) {
                        popUpTo(NavigationRoutes.Home.route) { inclusive = true }
                    }
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentRoute == NavigationRoutes.Settings.route,
            onClick = {
                if (currentRoute != NavigationRoutes.Settings.route) {
                    navController.navigate(NavigationRoutes.Settings.route)
                }
            }
        )
    }
}