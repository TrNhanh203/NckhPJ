package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.ImageVideoPickerScreen
import com.example.facilitiesmanagementpj.ui.viewmodel.ThietBiDetailViewModel
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.component.picker.ImagePickerSection
import com.example.facilitiesmanagementpj.ui.component.picker.VideoPickerSection

@Composable
fun ThietBiDetailScreen(
    navController: NavController,
    thietBiId: Int,
    isEditMode: Boolean = false,
    yeuCauId: Int? = null,
    viewModel: ThietBiDetailViewModel = hiltViewModel()
) {
    val thietBi by viewModel.thietBi.collectAsState()
    var moTa by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedLoaiYeuCau by remember { mutableStateOf<String?>(null) } // ✅ Lưu lựa chọn loại yêu cầu
    var isExistingDetail by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.loadThietBi(thietBiId)
        if (yeuCauId != null) {
            viewModel.loadChiTietYeuCau(yeuCauId, thietBiId) { chiTietYeuCau ->
                moTa = chiTietYeuCau.moTa
                selectedLoaiYeuCau = chiTietYeuCau.loaiYeuCau
                isExistingDetail = true
            }
        }
    }

    ScaffoldLayout(
        title = thietBi?.tenThietBi ?: "Chi tiết thiết bị",
        navController = navController,
        showBottomBar = true
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(modifier)
        ) {
            thietBi?.let {
                Text("Tên thiết bị: ${it.tenThietBi}")
                Text("Loại: ${it.loaiThietBiId}")
                Text("Trạng thái: ${it.trangThai}")
                Text("Ghi chú: ${it.ghiChu ?: "Không có"}")

                if (isEditMode) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Nhập chi tiết yêu cầu", style = MaterialTheme.typography.headlineMedium)

                    // ✅ Dropdown chọn loại yêu cầu
                    Text("Loại yêu cầu:")
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Button(onClick = { expanded = true }) {
                            Text(selectedLoaiYeuCau ?: "Chọn loại yêu cầu")
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            LoaiYeuCau.ALL.forEach { loai ->
                                DropdownMenuItem(
                                    text = { Text(loai) },
                                    onClick = {
                                        selectedLoaiYeuCau = loai
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // ✅ Mô tả chi tiết
                    OutlinedTextField(
                        value = moTa,
                        onValueChange = { moTa = it },
                        label = { Text("Mô tả chi tiết") }
                    )

                    //ImageVideoPickerScreen()
                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        ImagePickerSection()  // Hiển thị ảnh
                        VideoPickerSection()  // Hiển thị video
                    }

                    Button(
                        onClick = {
                            if (yeuCauId != null && selectedLoaiYeuCau != null) {
                                if (isExistingDetail) {
                                    viewModel.updateChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa)
                                } else {
                                    viewModel.addChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa)
                                }
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedLoaiYeuCau != null && moTa.isNotBlank()
                    ) {
                        Text(if (isExistingDetail) "Cập nhật yêu cầu" else "Thêm vào yêu cầu")
                    }
                }
            } ?: run {
                Text("Đang tải dữ liệu...", modifier = Modifier.fillMaxSize().padding(16.dp))
            }
        }
    }
}

//@Composable
//fun ThietBiDetailScreen(
//    navController: NavController,
//    thietBiId: Int,
//    yeuCauId: Int? = null,
//    viewModel: ThietBiDetailViewModel = hiltViewModel()
//) {
//    val thietBi by viewModel.thietBi.collectAsState()
//    var moTa by remember { mutableStateOf("") }
//    var expanded by remember { mutableStateOf(false) }
//    var selectedLoaiYeuCau by remember { mutableStateOf<String?>(null) }
//    var isExistingDetail by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        viewModel.loadThietBi(thietBiId)
//        if (yeuCauId != null) {
//            viewModel.loadChiTietYeuCau(yeuCauId, thietBiId) { chiTietYeuCau ->
//                moTa = chiTietYeuCau.moTa
//                selectedLoaiYeuCau = chiTietYeuCau.loaiYeuCau
//                isExistingDetail = true
//            }
//        }
//    }
//
//    ScaffoldLayout(
//        title = thietBi?.tenThietBi ?: "Chi tiết thiết bị",
//        navController = navController,
//        showBottomBar = true
//    ) { modifier ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .then(modifier)
//        ) {
//            thietBi?.let {
//                Text("Tên thiết bị: ${it.tenThietBi}")
//                Text("Loại: ${it.loaiThietBiId}")
//                Text("Trạng thái: ${it.trangThai}")
//                Text("Ghi chú: ${it.ghiChu ?: "Không có"}")
//
//                if (yeuCauId != null) {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Text("Nhập chi tiết yêu cầu", style = MaterialTheme.typography.headlineMedium)
//
//                    Text("Loại yêu cầu:")
//                    Box(modifier = Modifier.fillMaxWidth()) {
//                        Button(onClick = { expanded = true }) {
//                            Text(selectedLoaiYeuCau ?: "Chọn loại yêu cầu")
//                        }
//                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                            LoaiYeuCau.ALL.forEach { loai ->
//                                DropdownMenuItem(
//                                    text = { Text(loai) },
//                                    onClick = {
//                                        selectedLoaiYeuCau = loai
//                                        expanded = false
//                                    }
//                                )
//                            }
//                        }
//                    }
//
//                    OutlinedTextField(
//                        value = moTa,
//                        onValueChange = { moTa = it },
//                        label = { Text("Mô tả chi tiết") }
//                    )
//
//                    Button(
//                        onClick = {
//                            if (yeuCauId != null && selectedLoaiYeuCau != null) {
//                                if (isExistingDetail) {
//                                    viewModel.updateChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa)
//                                } else {
//                                    viewModel.addChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa)
//                                }
//                                navController.popBackStack()
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        enabled = selectedLoaiYeuCau != null && moTa.isNotBlank()
//                    ) {
//                        Text(if (isExistingDetail) "Cập nhật yêu cầu" else "Thêm vào yêu cầu")
//                    }
//                }
//            } ?: run {
//                Text("Đang tải dữ liệu...", modifier = Modifier.fillMaxSize().padding(16.dp))
//            }
//        }
//    }
//}



