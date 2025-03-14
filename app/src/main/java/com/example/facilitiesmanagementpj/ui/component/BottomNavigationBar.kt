package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.facilitiesmanagementpj.ui.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Screen.Home

    )

    NavigationBar(
        modifier = Modifier.height(56.dp),
        containerColor = Color.Black // Màu nền đen
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.route, color = Color.White) }, // Màu chữ trắng
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = { Icon(Icons.Default.DateRange, contentDescription = screen.route, tint = Color.White) }
            )

            NavigationBarItem(
                label = { Text(screen.route, color = Color.White) }, // Màu chữ trắng
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = { Icon(Icons.Default.Home, contentDescription = screen.route, tint = Color.White) }
            )

            NavigationBarItem(
                label = { Text(screen.route, color = Color.White) }, // Màu chữ trắng
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = { Icon(Icons.Default.Person, contentDescription = screen.route, tint = Color.White) }
            )
        }
    }
}
