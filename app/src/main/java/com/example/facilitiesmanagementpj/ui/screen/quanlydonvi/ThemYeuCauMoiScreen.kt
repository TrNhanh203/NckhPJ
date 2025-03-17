package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVCreateYeuCauViewModel
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen

@Composable
fun ThemYeuCauMoiScreen(
    navController: NavController,
    yeuCauId: Int?, // ✅ Nhận tham số yêu cầu, nếu null thì tạo mới
    viewModel: QLDVCreateYeuCauViewModel = hiltViewModel()
) {
    val chiTietList by viewModel.chiTietYeuCauList.collectAsState()
    val taiKhoanId = SessionManager.currentUser?.id ?: 0
    val donViId = SessionManager.currentUser?.donViId ?: 0
    var moTa by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(yeuCauId == null) } // ✅ Nếu có `yeuCauId`, bỏ qua nhập mô tả

    LaunchedEffect(yeuCauId) {
        if (yeuCauId != null) {
            viewModel.setYeuCauId(yeuCauId)
            viewModel.loadChiTietYeuCau(yeuCauId)

            viewModel.getYeuCauById(yeuCauId) { yeuCau ->
                moTa = yeuCau.moTa // ✅ Lấy mô tả từ yêu cầu nháp
            }
        }
    }

    if (showDialog) { // ✅ Hiển thị `AlertDialog` để nhập mô tả nếu chưa có yêu cầu
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.createYeuCau(taiKhoanId, donViId, moTa)
                    showDialog = false
                }) {
                    Text("Tạo yêu cầu")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    navController.popBackStack()
                }) {
                    Text("Hủy")
                }
            },
            text = {
                Column {
                    Text("Nhập mô tả cho yêu cầu")
                    OutlinedTextField(
                        value = moTa,
                        onValueChange = { moTa = it },
                        label = { Text("Mô tả yêu cầu") }
                    )
                }
            }
        )
    }

    ScaffoldLayout(
        title = moTa.ifBlank { "Chỉnh sửa yêu cầu" }, // ✅ Hiển thị mô tả trên thanh tiêu đề
        navController = navController,
        showBottomBar = true
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(modifier)
        ) {
            Text("Chi tiết yêu cầu", style = MaterialTheme.typography.headlineMedium)

            Text(
                text = moTa.ifBlank { "Không có mô tả" }, // ✅ Hiển thị mô tả bên dưới tiêu đề
                style = MaterialTheme.typography.titleLarge, // ✅ Tiêu đề lớn
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn {
                items(chiTietList) { chiTiet ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Thiết bị: ${chiTiet.thietBiId}")
                            Text("Loại yêu cầu: ${chiTiet.loaiYeuCau}")
                            Text("Mô tả: ${chiTiet.moTa}")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    chiTiet.thietBiId?.let { thietBiId ->
                                        navController.navigate(Screen.ThietBiDetail.createRoute(thietBiId, isEditMode = true, yeuCauId = yeuCauId))
                                    }
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                                IconButton(onClick = {
                                    viewModel.removeChiTietYeuCau(chiTiet.id)
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }
                        }
                    }
                }
            }

            Button(onClick = {
                viewModel.yeuCauId.value?.let {
                    navController.navigate("chon_thiet_bi/$it")
                }
            }) {
                Text("Thêm thiết bị")
            }

        }
    }
}


//@Composable
//fun ThemYeuCauMoiScreen(
//    navController: NavController,
//    yeuCauId: Int?, // ✅ Nhận tham số yêu cầu, nếu null thì tạo mới
//    viewModel: QLDVCreateYeuCauViewModel = hiltViewModel()
//) {
//    val chiTietList by viewModel.chiTietYeuCauList.collectAsState()
//    val taiKhoanId = SessionManager.currentUser?.id ?: 0
//    val donViId = SessionManager.currentUser?.donViId ?: 0
//    var moTa by remember { mutableStateOf("") }
//    var showDialog by remember { mutableStateOf(yeuCauId == null) } // ✅ Nếu có `yeuCauId`, bỏ qua nhập mô tả
//
//    LaunchedEffect(yeuCauId) {
//        if (yeuCauId != null) {
//            viewModel.setYeuCauId(yeuCauId)
//            viewModel.loadChiTietYeuCau(yeuCauId)
//
//            viewModel.getYeuCauById(yeuCauId) { yeuCau ->
//                moTa = yeuCau.moTa // ✅ Lấy mô tả từ yêu cầu nháp
//            }
//        }
//    }
//
//    if (showDialog) { // ✅ Hiển thị `AlertDialog` để nhập mô tả nếu chưa có yêu cầu
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            confirmButton = {
//                Button(onClick = {
//                    viewModel.createYeuCau(taiKhoanId, donViId, moTa)
//                    showDialog = false
//                }) {
//                    Text("Tạo yêu cầu")
//                }
//            },
//            dismissButton = {
//                Button(onClick = {
//                    showDialog = false
//                    navController.popBackStack()
//                }) {
//                    Text("Hủy")
//                }
//            },
//            text = {
//                Column {
//                    Text("Nhập mô tả cho yêu cầu")
//                    OutlinedTextField(
//                        value = moTa,
//                        onValueChange = { moTa = it },
//                        label = { Text("Mô tả yêu cầu") }
//                    )
//                }
//            }
//        )
//    }
//
//    ScaffoldLayout(
//        title = moTa.ifBlank { "Chỉnh sửa yêu cầu" }, // ✅ Hiển thị mô tả trên thanh tiêu đề
//        navController = navController,
//        showBottomBar = true
//    ) { modifier ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .then(modifier)
//        ) {
//            Text("Chi tiết yêu cầu", style = MaterialTheme.typography.headlineMedium)
//
//            Text(
//                text = moTa.ifBlank { "Không có mô tả" }, // ✅ Hiển thị mô tả bên dưới tiêu đề
//                style = MaterialTheme.typography.titleLarge, // ✅ Tiêu đề lớn
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//
//            LazyColumn {
//                items(chiTietList) { chiTiet ->
//                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("Thiết bị: ${chiTiet.thietBiId}")
//                            Text("Loại yêu cầu: ${chiTiet.loaiYeuCau}")
//                            Text("Mô tả: ${chiTiet.moTa}")
//                        }
//                    }
//                }
//            }
//
//            Button(onClick = {
//                viewModel.yeuCauId.value?.let {
//                    navController.navigate("chon_thiet_bi/$it")
//                }
//            }) {
//                Text("Thêm thiết bị")
//            }
//
//        }
//    }
//}



