package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.navigation.Screen

@Composable
fun BottomNavigationBar(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) } // ✅ Biến để hiển thị thông báo
    val currentUser = SessionManager.currentUser // ✅ Lấy thông tin tài khoản đang đăng nhập

    NavigationBar(
        modifier = Modifier.height(56.dp),
        containerColor = Color.Black
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        // Nút Dashboard (Điều hướng theo vai trò)
        NavigationBarItem(
            label = { Text("Dashboard", color = Color.White) },
            selected = currentRoute in listOf(Screen.AdminDashboard.route, Screen.KtvDashboard.route, Screen.DonViDashboard.route),
            onClick = {
                if (currentUser == null) {
                    showDialog = true // ✅ Hiển thị cảnh báo nếu chưa đăng nhập
                } else {
                    val dashboardRoute = when (currentUser.tenVaiTro) {
                        "Admin" -> Screen.AdminDashboard.route
                        "Kỹ Thuật Viên" -> Screen.KtvDashboard.route
                        "Quản Lý Đơn Vị" -> Screen.DonViDashboard.route
                        else -> null
                    }
                    if (dashboardRoute != null) {
                        navController.navigate(dashboardRoute)
                    }
                }
            },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Dashboard", tint = Color.White) }
        )

        // Nút Trang chủ
        NavigationBarItem(
            label = { Text("Trang chủ", color = Color.White) },
            selected = currentRoute == Screen.Home.route,
            onClick = { navController.navigate(Screen.Home.route) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ", tint = Color.White) }
        )

        // Nút Profile (Kiểm tra đăng nhập)
        NavigationBarItem(
            label = { Text("Hồ sơ", color = Color.White) },
            selected = currentRoute == Screen.Profile.route,
            onClick = {
                if (currentUser == null) {
                    showDialog = true // ✅ Hiển thị cảnh báo nếu chưa đăng nhập
                } else {
                    navController.navigate(Screen.Profile.route) // ✅ Nếu đã đăng nhập, chuyển đến trang Profile
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = "Hồ sơ", tint = Color.White) }
        )
    }

    // ✅ Dialog cảnh báo nếu chưa đăng nhập
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Bạn chưa đăng nhập") },
            text = { Text("Bạn có muốn chuyển đến trang đăng nhập không?") },
            confirmButton = {
                Button(onClick = {
                    navController.navigate(Screen.Login.route)
                    showDialog = false
                }) {
                    Text("Đến đăng nhập")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
