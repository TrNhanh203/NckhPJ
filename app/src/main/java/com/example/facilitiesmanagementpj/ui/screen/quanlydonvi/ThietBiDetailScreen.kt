package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.ImageVideoPickerScreen
import com.example.facilitiesmanagementpj.ui.viewmodel.ThietBiDetailViewModel
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.component.picker.ImagePickerSection
import com.example.facilitiesmanagementpj.ui.component.picker.VideoPickerSection


// In ThietBiDetailScreen.kt

@OptIn(UnstableApi::class)
@Composable
fun ThietBiDetailScreen(
    navController: NavController,
    thietBiId: Int,
    isEditMode: Boolean = false,
    yeuCauId: Int? = null,
    viewModel: ThietBiDetailViewModel = hiltViewModel()
) {
    val thietBi by viewModel.thietBi.collectAsState()
    val imageUris by viewModel.imageUris.collectAsState()
    val videoUri by viewModel.videoUri.collectAsState()
    var moTa by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedLoaiYeuCau by remember { mutableStateOf<String?>(null) }
    var isExistingDetail by remember { mutableStateOf(false) }
    var selectedImages by remember { mutableStateOf<List<Uri>>(imageUris) }
    var selectedVideo by remember { mutableStateOf<Uri?>(videoUri) }



    LaunchedEffect(Unit) {
        viewModel.loadThietBi(thietBiId)
        if (yeuCauId != null) {
            viewModel.loadChiTietYeuCau(yeuCauId, thietBiId) { chiTietYeuCau ->
                moTa = chiTietYeuCau.moTa
                selectedLoaiYeuCau = chiTietYeuCau.loaiYeuCau
                isExistingDetail = true
                viewModel.loadMedia(chiTietYeuCau.id)
            }
        }
    }

    LaunchedEffect(imageUris, videoUri) {
        selectedImages = imageUris
        selectedVideo = videoUri
    }

    ScaffoldLayout(
        title = thietBi?.tenThietBi ?: "Chi tiết thiết bị",
        navController = navController,
        showBottomBar = false
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .height(1000.dp)
                .then(modifier)
        ) {
            thietBi?.let { thietBiItem ->
                Text("Tên thiết bị: ${thietBiItem.tenThietBi}")
                Text("Loại: ${thietBiItem.loaiThietBiId}")
                Text("Trạng thái: ${thietBiItem.trangThai}")
                Text("Ghi chú: ${thietBiItem.ghiChu ?: "Không có"}")

                if (isEditMode) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Nhập chi tiết yêu cầu", style = MaterialTheme.typography.headlineMedium)

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

                    OutlinedTextField(
                        value = moTa,
                        onValueChange = { moTa = it },
                        label = { Text("Mô tả chi tiết") }
                    )

                    Button(
                        onClick = {
                            if (yeuCauId != null && selectedLoaiYeuCau != null) {
                                if (isExistingDetail) {
                                    viewModel.updateChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa, selectedImages, selectedVideo)
                                } else {
                                    viewModel.addChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa, selectedImages, selectedVideo)
                                }
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedLoaiYeuCau != null && moTa.isNotBlank()
                    ) {
                        Text(if (isExistingDetail) "Cập nhật yêu cầu" else "Thêm vào yêu cầu")
                    }

                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        ImagePickerSection(selectedImages = selectedImages, onImagesSelected = { images -> selectedImages = images })
                        VideoPickerSection(selectedVideo = selectedVideo, onVideoSelected = { video -> selectedVideo = video })
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
//    isEditMode: Boolean = false,
//    yeuCauId: Int? = null,
//    viewModel: ThietBiDetailViewModel = hiltViewModel()
//) {
//    val thietBi by viewModel.thietBi.collectAsState()
//    var moTa by remember { mutableStateOf("") }
//    var expanded by remember { mutableStateOf(false) }
//    var selectedLoaiYeuCau by remember { mutableStateOf<String?>(null) }
//    var isExistingDetail by remember { mutableStateOf(false) }
//    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
//    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
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
//        showBottomBar = false
//    ) { modifier ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .height(1000.dp)
//                .then(modifier)
//        ) {
//            thietBi?.let { thietBiItem ->
//                Text("Tên thiết bị: ${thietBiItem.tenThietBi}")
//                Text("Loại: ${thietBiItem.loaiThietBiId}")
//                Text("Trạng thái: ${thietBiItem.trangThai}")
//                Text("Ghi chú: ${thietBiItem.ghiChu ?: "Không có"}")
//
//                if (isEditMode) {
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
//                                    viewModel.updateChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa, selectedImages, selectedVideo)
//                                } else {
//                                    viewModel.addChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa, selectedImages, selectedVideo)
//                                }
//                                navController.popBackStack()
//                            }
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        enabled = selectedLoaiYeuCau != null && moTa.isNotBlank()
//                    ) {
//                        Text(if (isExistingDetail) "Cập nhật yêu cầu" else "Thêm vào yêu cầu")
//                    }
//
//                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//                        ImagePickerSection(selectedImages, onImagesSelected = { images -> selectedImages = images })
//                        VideoPickerSection(selectedVideo, onVideoSelected = { video -> selectedVideo = video })
//                    }
//                }
//            } ?: run {
//                Text("Đang tải dữ liệu...", modifier = Modifier.fillMaxSize().padding(16.dp))
//            }
//        }
//    }
//}





