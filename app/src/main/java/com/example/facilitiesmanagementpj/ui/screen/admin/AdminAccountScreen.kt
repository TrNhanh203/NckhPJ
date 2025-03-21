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
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminAccountViewModel
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.VaiTroViewModel

@Composable
fun AdminAccountScreen(navController: NavController, viewModel: AdminAccountViewModel = hiltViewModel()) {
    val filteredTaiKhoanList by viewModel.filteredTaiKhoanList.collectAsState()
    val vaiTroList by viewModel.vaiTroList.collectAsState()
    val trangThaiList by viewModel.trangThaiList.collectAsState()

    ScaffoldLayout(
        title = "Quản lý tài khoản",
        navController = navController,
        showBottomBar = false
    ) { modifier ->
        Column(modifier = Modifier.fillMaxSize().then(modifier).padding(16.dp)) {
            Text("Quản lý tài khoản", style = MaterialTheme.typography.headlineMedium)



            Column(modifier = Modifier.fillMaxWidth()) {
                DropdownMenuFilter(
                    label = "Vai trò",
                    items = vaiTroList.map { it.tenVaiTro },
                    selected = viewModel.selectedVaiTro.collectAsState().value,
                    onSelectedChange = { viewModel.setVaiTroFilter(it) }
                )

                DropdownMenuFilter(
                    label = "Trạng thái",
                    items = trangThaiList,
                    selected = viewModel.selectedTrangThai.collectAsState().value,
                    onSelectedChange = { viewModel.setTrangThaiFilter(it) }
                )
            }

            //Spacer(modifier = Modifier.height(16.dp))

            // Danh sách tài khoản
            LazyColumn {
                items(filteredTaiKhoanList) { taiKhoan ->
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

            Button(onClick = { navController.navigate(Screen.AdminViewDetailProfile.createRoute(taiKhoan.id))}) { // Chưa xử lý điều hướng
                Text("Xem chi tiết")
            }
        }
    }
}
