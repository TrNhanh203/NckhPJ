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

@Composable
fun AdminAccountScreen(navController: NavController, viewModel: AdminAccountViewModel = hiltViewModel()) {
    val taiKhoanList by viewModel.taiKhoanList.collectAsState()
    val vaiTroList by viewModel.vaiTroList.collectAsState()
    val trangThaiList by viewModel.trangThaiList.collectAsState()

    var selectedVaiTro by remember { mutableStateOf<String?>(null) }
    var selectedTrangThai by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Quản lý tài khoản", style = MaterialTheme.typography.headlineMedium)

        // Bộ lọc
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DropdownMenuFilter(label = "Vai trò", items = vaiTroList.map { it.tenVaiTro }, selected = selectedVaiTro) {
                selectedVaiTro = it
            }
            DropdownMenuFilter(label = "Trạng thái", items = trangThaiList, selected = selectedTrangThai) {
                selectedTrangThai = it
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Danh sách tài khoản
        LazyColumn {
            items(taiKhoanList.filter {
                (selectedVaiTro == null || it.tenVaiTro == selectedVaiTro) &&
                        (selectedTrangThai == null || it.trangThai == selectedTrangThai)
            }) { taiKhoan ->
                AccountItem(taiKhoan, navController)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút thêm tài khoản mới
//        Button(onClick = { navController.navigate(Screen.AddAccount.route) }, modifier = Modifier.fillMaxWidth()) {
//            Text("Thêm tài khoản mới")
//        }
        Button(onClick = {  }, modifier = Modifier.fillMaxWidth()) {
            Text("Thêm tài khoản mới")
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

//            Button(onClick = { navController.navigate(Screen.AdminProfile.route + "/${taiKhoan.id}") }) {
//                Text("Xem chi tiết")
//            }
            Button(onClick = {  }) {
                Text("Xem chi tiết")
            }
        }
    }
}

@Composable
fun DropdownMenuFilter(
    label: String,
    items: List<String>,
    selected: String?,
    onSelectedChange: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selected ?: "Tất cả") }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Box {
            Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(selectedText)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("Tất cả") },
                    onClick = {
                        selectedText = "Tất cả"
                        onSelectedChange(null)
                        expanded = false
                    }
                )
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            selectedText = item
                            onSelectedChange(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
