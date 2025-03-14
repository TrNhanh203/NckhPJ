package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout

@Composable
fun AdminDashboardScreen(navController: NavController) {

    ScaffoldLayout(title = "Admin DashBoard", navController = navController, showBottomBar = true) { modifier ->
        Column(
            modifier = Modifier.fillMaxSize().then(modifier),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Admin dashboard", style = MaterialTheme.typography.headlineMedium)
        }
    }


}