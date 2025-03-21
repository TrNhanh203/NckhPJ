package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVDanhSachYeuCauViewModel
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

@Composable
fun DanhSachYeuCauScreen(navController: NavController, viewModel: QLDVDanhSachYeuCauViewModel = hiltViewModel()) {
    val yeuCauList by viewModel.filteredYeuCauList.collectAsState()
    val donViId = SessionManager.currentUser?.donViId ?: 0
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var selectedYeuCau by remember { mutableStateOf<YeuCau?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadYeuCauList(donViId)
    }

    if (showDialog && selectedYeuCau != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc chắn muốn xóa yêu cầu này không?") },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        viewModel.deleteYeuCau(selectedYeuCau!!.id)
                        showDialog = false
                    }
                }) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout("Danh Sách Yêu Cầu", navController, showTopBar = true,showBottomBar = false,showDrawer = false)
    { modifier ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(modifier)) {
            Text("Danh sách yêu cầu", style = MaterialTheme.typography.headlineMedium)

            DropdownMenuFilter(
                label = "Trạng thái",
                items = TrangThaiYeuCau.ALL,
                selected = viewModel.selectedTrangThai.collectAsState().value,
                onSelectedChange = { viewModel.setTrangThaiFilter(it) }
            )

            LazyColumn {
                items(yeuCauList) { yeuCau ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
//                        .clickable {
//                            navController.navigate(Screen.ThemYeuCauMoi.createRoute(yeuCau.id))
//                        }
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        if (yeuCau.trangThai == TrangThaiYeuCau.NHAP) {
                                            selectedYeuCau = yeuCau
                                            showDialog = true
                                        }
                                    },
                                    onTap = {
                                        navController.navigate(Screen.ThemYeuCauMoi.createRoute(yeuCau.id))
                                    }

                                )
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


}


//@Composable
//fun DanhSachYeuCauScreen(navController: NavController, viewModel: QLDVDanhSachYeuCauViewModel = hiltViewModel()) {
//    val yeuCauList by viewModel.filteredYeuCauList.collectAsState()
//    val donViId = SessionManager.currentUser?.donViId ?: 0
//
//    LaunchedEffect(Unit) {
//        viewModel.loadYeuCauList(donViId)
//    }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        Text("Danh sách yêu cầu", style = MaterialTheme.typography.headlineMedium)
//
//        DropdownMenuFilter(
//            label = "Trạng thái",
//            items = listOf(TrangThaiYeuCau.NHAP, TrangThaiYeuCau.DANG_XU_LY, TrangThaiYeuCau.CHO_XAC_NHAN, TrangThaiYeuCau.DA_XU_LY, TrangThaiYeuCau.DA_HUY),
//            selected = viewModel.selectedTrangThai.collectAsState().value,
//            onSelectedChange = { viewModel.setTrangThaiFilter(it) }
//        )
//
//        LazyColumn {
//            items(yeuCauList) { yeuCau ->
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                        .clickable {
//                            if (yeuCau.trangThai == TrangThaiYeuCau.NHAP) {
//                                navController.navigate(Screen.ThemYeuCauMoi.createRoute(yeuCau.id)) // ✅ Điều hướng đến màn hình chỉnh sửa yêu cầu nháp
//                            } else {
//                                navController.navigate("chi_tiet_yeu_cau/${yeuCau.id}") // ✅ Điều hướng đến màn hình xem chi tiết yêu cầu
//                            }
//                        }
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text("Mô tả: ${yeuCau.moTa}")
//                        Text("Trạng thái: ${yeuCau.trangThai}")
//                        Text("Ngày yêu cầu: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(yeuCau.ngayYeuCau))}")
//                    }
//                }
//            }
//        }
//    }
//}
