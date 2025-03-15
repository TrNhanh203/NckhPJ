package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminAccountViewModel
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout

@Composable
fun AdminAccountScreen(navController: NavController, viewModel: AdminAccountViewModel = hiltViewModel()) {
    val taiKhoanList by viewModel.taiKhoanList.collectAsState()

    ScaffoldLayout(
        title = "Quản lý tài khoản",
        navController = navController,
        showBottomBar = false // ❌ Không hiển thị BottomAppBar
    ) { modifier ->
        Column(modifier = Modifier.fillMaxSize().then(modifier).padding(16.dp)) {
            Text("Quản lý tài khoản", style = MaterialTheme.typography.headlineMedium)

            Button(onClick = {  }, modifier = Modifier.fillMaxWidth()) {
                Text("Thêm tài khoản mới")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Danh sách tài khoản
            LazyColumn {
                items(taiKhoanList) { taiKhoan ->
                    AccountItem(taiKhoan, navController)
                }
            }
        }
    }
}

// Component hiển thị từng tài khoản
@Composable
fun AccountItem(taiKhoan: TaiKhoanWithRole, navController: NavController) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tên: ${taiKhoan.hoTen ?: "Chưa cập nhật"}")
            Text("Vai trò: ${taiKhoan.tenVaiTro}")
            Text("Trạng thái: ${taiKhoan.trangThai}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { }) { // Chưa xử lý điều hướng
                Text("Xem chi tiết")
            }
        }
    }
}
