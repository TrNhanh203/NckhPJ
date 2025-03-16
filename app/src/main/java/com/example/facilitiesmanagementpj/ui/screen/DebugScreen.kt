package com.example.facilitiesmanagementpj.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.DebugViewModel

@Composable
fun DebugScreen(viewModel: DebugViewModel = hiltViewModel()) {
    val vaiTroList by viewModel.vaiTroList.collectAsState()
    val taiKhoanList by viewModel.taiKhoanList.collectAsState()
    val trangThaiList by viewModel.trangThaiList.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Debug: Kiểm tra dữ liệu từ Database", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị danh sách Vai Trò
        Text("Danh sách Vai Trò:", style = MaterialTheme.typography.bodyLarge)
        LazyColumn {
            items(vaiTroList) { vaiTro ->
                Text("- ${vaiTro.tenVaiTro}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị danh sách Tài Khoản
        Text("Danh sách Tài Khoản:", style = MaterialTheme.typography.bodyLarge)
        LazyColumn {
//            items(taiKhoanList) { taiKhoan ->
//                Text("- ${taiKhoan.tenTaiKhoan} (${taiKhoan.trangThai})")
//            }
            items(taiKhoanList) { taiKhoan ->
                Text("- ${taiKhoan.tenTaiKhoan} (${taiKhoan.tenVaiTro ?: "Không có vai trò"})")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hiển thị danh sách Trạng Thái
        Text("Danh sách Trạng Thái:", style = MaterialTheme.typography.bodyLarge)
        LazyColumn {
            items(trangThaiList) { trangThai ->
                Text("- $trangThai")
            }
        }
    }
}
