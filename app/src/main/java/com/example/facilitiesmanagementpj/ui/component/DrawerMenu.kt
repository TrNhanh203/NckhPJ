package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.navigation.Screen

@Composable
fun DrawerMenu(navController: NavController, closeDrawer: () -> Unit) {
    val loggedInUser = SessionManager.currentUser // ✅ Kiểm tra trạng thái đăng nhập

    ModalDrawerSheet(
        drawerContainerColor = Color.Black, // Màu nền đen
        modifier = Modifier.width(240.dp) // Chiếm 2/3 màn hình
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Menu", style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            // Tùy chọn Trang chủ
            NavigationDrawerItem(
                label = { Text("Trang chủ", color = Color.White) },
                selected = false,
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                    closeDrawer()
                }
            )

            // Nếu chưa đăng nhập, hiển thị tùy chọn Đăng nhập
            if (loggedInUser == null) {
                NavigationDrawerItem(
                    label = { Text("Đăng nhập", color = Color.White) },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                        closeDrawer()
                    }
                )
            } else {
                // Nếu đã đăng nhập, hiển thị tùy chọn Đăng xuất
                NavigationDrawerItem(
                    label = { Text("Đăng xuất", color = Color.White) },
                    selected = false,
                    onClick = {
                        SessionManager.logout() // ❌ Xóa session khi đăng xuất
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                        closeDrawer()
                    }
                )
            }
        }
    }
}
