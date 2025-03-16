package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.screen.admin.DashboardOption

@Composable
fun DonViDashboardScreen(navController: NavController) {
    ScaffoldLayout(
        title = "Don Vi DashBoard",
        navController = navController,
        showBottomBar = true
    ) { modifier ->
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
                title = "Danh sách phòng",
                icon = Icons.Default.AccountCircle,
                color = Color.Blue,
                onClick = { navController.navigate(Screen.QLDVPhong.route) }
            )

            DashboardOption(
                title = "Danh sách báo cáo",
                icon = Icons.Default.Settings,
                color = Color.Green,
                onClick = {  }
            )

            DashboardOption(
                title = "Tạo Yêu Cầu Mới",
                icon = Icons.Default.Settings,
                color = Color.Yellow,
                onClick = {  }
            )

            DashboardOption(
                title = "Ds thiết bị của dvi",
                icon = Icons.Default.Settings,
                color = Color.Magenta,
                onClick = { navController.navigate(Screen.QLDVThietBiTheoDV.route) }
            )


        }

    }
}