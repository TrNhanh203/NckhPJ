package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen

@Composable
fun AdminDashboardScreen(navController: NavController) {
    ScaffoldLayout(title = "Admin DashBoard", navController = navController, showBottomBar = true) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(modifier),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Admin Dashboard", style = MaterialTheme.typography.headlineMedium)

            DashboardOption(
                title = "Quản lý tài khoản",
                icon = Icons.Default.AccountCircle,
                color = Color.Blue,
                onClick = { navController.navigate(Screen.AdminAccount.route) }
            )

            DashboardOption(
                title = "Quản lý thiết bị",
                icon = Icons.Default.Settings,
                color = Color.Green,
                onClick = {  }
            )

            DashboardOption(
                title = "Quản lý phân công",
                icon = Icons.Default.AccountBox,
                color = Color.Red,
                onClick = {  }
            )
        }
    }
}

// Component hiển thị từng tùy chọn trong Dashboard
@Composable
fun DashboardOption(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontSize = 20.sp, color = color)
        }
    }
}
