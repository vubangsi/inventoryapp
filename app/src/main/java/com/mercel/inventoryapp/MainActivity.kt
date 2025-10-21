package com.mercel.inventoryapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mercel.inventoryapp.components.BottomNavigationBar
import com.mercel.inventoryapp.data.datastore.UserPreferences
import com.mercel.inventoryapp.navigation.InventoryNavigation
import com.mercel.inventoryapp.navigation.NavigationRoutes
import com.mercel.inventoryapp.ui.theme.InventoryappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryappTheme {
                InventoryApp()
            }
        }
    }
}

@Composable
fun InventoryApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    
    val isLoggedIn by userPreferences.isLoggedIn.collectAsState(initial = false)
    val startDestination = if (isLoggedIn) NavigationRoutes.Home.route else NavigationRoutes.Login.route
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val shouldShowBottomNav = currentRoute != NavigationRoutes.Login.route
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomNav) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        InventoryNavigation(
            navController = navController,
            startDestination = startDestination
        )
    }
}