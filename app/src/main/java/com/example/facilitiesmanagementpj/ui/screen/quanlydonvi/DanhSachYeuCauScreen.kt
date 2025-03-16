package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVDanhSachYeuCauViewModel
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun DanhSachYeuCauScreen(navController: NavController, viewModel: QLDVDanhSachYeuCauViewModel = hiltViewModel()) {
    val yeuCauList by viewModel.filteredYeuCauList.collectAsState()
    val donViId = SessionManager.currentUser?.donViId ?: 0

    LaunchedEffect(Unit) {
        viewModel.loadYeuCauList(donViId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Danh sách yêu cầu", style = MaterialTheme.typography.headlineMedium)

        DropdownMenuFilter(
            label = "Trạng thái",
            items = listOf(TrangThaiYeuCau.NHAP, TrangThaiYeuCau.DANG_XU_LY, TrangThaiYeuCau.CHO_XU_LY, TrangThaiYeuCau.DA_XU_LY, TrangThaiYeuCau.DA_HUY),
            selected = viewModel.selectedTrangThai.collectAsState().value,
            onSelectedChange = { viewModel.setTrangThaiFilter(it) }
        )

        LazyColumn {
            items(yeuCauList) { yeuCau ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            if (yeuCau.trangThai == TrangThaiYeuCau.NHAP) {
                                navController.navigate(Screen.ThemYeuCauMoi.createRoute(yeuCau.id)) // ✅ Điều hướng đến màn hình chỉnh sửa yêu cầu nháp
                            } else {
                                navController.navigate("chi_tiet_yeu_cau/${yeuCau.id}") // ✅ Điều hướng đến màn hình xem chi tiết yêu cầu
                            }
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Mô tả: ${yeuCau.moTa}")
                        Text("Trạng thái: ${yeuCau.trangThai}")
                        Text("Ngày yêu cầu: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(yeuCau.ngayYeuCau))}")
                    }
                }
            }
        }
    }
}
