package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel

@Composable
fun CustomBottomBar(
    navController: NavController,
    currentRoute: String?
) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp), // tổng chiều cao chứa cả FAB và bar
        contentAlignment = Alignment.BottomCenter
    ) {
        // BottomBar nền
        Surface(
            tonalElevation = 8.dp,
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 36.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dashboard
                BottomBarItem(
                    icon = Icons.Default.DateRange,
                    label = "Dashboard",
                    isSelected = currentRoute in listOf(Screen.AdminDashboard.route, Screen.KtvDashboard.route, Screen.DonViDashboard.route) == true,
                    onClick = {
                        if (currentUser == null) showDialog = true
                        else {
                            val route = when (currentUser!!.tenVaiTro) {
                                "Admin" -> Screen.AdminDashboard.route
                                "Kỹ Thuật Viên" -> Screen.KtvDashboard.route
                                "Quản Lý Đơn Vị" -> Screen.DonViDashboard.route
                                else -> null
                            }
                            route?.let { navController.navigate(it) }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(64.dp)) // chừa chỗ cho FAB

                // Hồ sơ
                BottomBarItem(
                    icon = Icons.Default.Person,
                    label = "Hồ sơ",
                    isSelected = currentRoute == Screen.Profile.route,
                    onClick = {
                        if (currentUser == null) showDialog = true
                        else navController.navigate(Screen.Profile.route)
                    }
                )
            }
        }

        // Nền trắng bên dưới FAB (giống như "hốc")
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .align(Alignment.TopCenter)
        )

        // FAB trung tâm
        FloatingActionButton(
            onClick = { navController.navigate(Screen.Home.route) },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(6.dp),
            modifier = Modifier
                .size(72.dp)
                .offset(y = (4).dp)
                .align(Alignment.TopCenter)
        ) {
            Icon(Icons.Default.Home, contentDescription = "Trang chủ")
        }
    }

    // Dialog chưa đăng nhập
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Bạn chưa đăng nhập") },
            text = { Text("Bạn có muốn chuyển đến trang đăng nhập không?") },
            confirmButton = {
                Button(onClick = {
                    navController.navigate(Screen.Login.route)
                    showDialog = false
                }) { Text("Đăng nhập") }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDialog = false }) { Text("Hủy") }
            }
        )
    }
}

@Composable
fun BottomBarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Icon(icon, contentDescription = label, tint = color)
        Text(label, fontSize = 12.sp, color = color)
    }
}

