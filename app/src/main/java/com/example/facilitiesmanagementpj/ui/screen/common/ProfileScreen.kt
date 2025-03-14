package com.example.facilitiesmanagementpj.ui.screen.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.viewmodel.ProfileViewModel
import com.example.facilitiesmanagementpj.ui.navigation.Screen

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val taiKhoan by viewModel.taiKhoan.collectAsState()

    if (taiKhoan == null) {
        // Nếu chưa đăng nhập, điều hướng về màn hình đăng nhập
        LaunchedEffect(Unit) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hồ sơ cá nhân", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        ProfileInfoItem(label = "Tên tài khoản", value = taiKhoan!!.tenTaiKhoan)
        ProfileInfoItem(label = "Họ và Tên", value = taiKhoan!!.hoTen ?: "Chưa cập nhật")
        ProfileInfoItem(label = "Email", value = taiKhoan!!.email ?: "Chưa cập nhật")
        ProfileInfoItem(label = "Số điện thoại", value = taiKhoan!!.soDienThoai ?: "Chưa cập nhật")
        ProfileInfoItem(label = "Trạng thái", value = taiKhoan!!.trangThai)
        ProfileInfoItem(label = "Lần đăng nhập cuối", value = taiKhoan!!.lastLogin?.toString() ?: "Chưa đăng nhập")

        Spacer(modifier = Modifier.height(24.dp))

        // Nút cập nhật thông tin cá nhân
        Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cập nhật thông tin")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nút đổi mật khẩu
        Button(
            onClick = { navController.navigate(Screen.ChangePassword.route) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Đổi mật khẩu")
        }
    }
}

// Composable hiển thị từng mục thông tin cá nhân
@Composable
fun ProfileInfoItem(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}
