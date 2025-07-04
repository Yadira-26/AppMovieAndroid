package com.example.appandroidmovie.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.appandroidmovie.AppDestinations

// Define los ítems de la barra de navegación
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem(AppDestinations.HOME_ROUTE, Icons.Filled.Home, "Inicio")
    object Search : BottomNavItem(AppDestinations.SEARCH_ROUTE, Icons.Filled.Search, "Buscar")
    object Favorites : BottomNavItem(AppDestinations.FAVORITES_ROUTE, Icons.Filled.Favorite, "Favoritos")
}

@Composable
fun AppBottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.Favorites,
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Evita acumular múltiples copias de la misma pantalla en la pila de retroceso
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        // Evita relanzar la misma pantalla si ya está seleccionada
                        launchSingleTop = true
                        // Restaura el estado al volver a una pantalla previamente seleccionada
                        restoreState = true
                    }
                }
            )
        }
    }
}