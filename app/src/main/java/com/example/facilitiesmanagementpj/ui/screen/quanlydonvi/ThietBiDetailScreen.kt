package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.facilitiesmanagementpj.ui.screen.admin.CardThongKeBaoDuong
import com.example.facilitiesmanagementpj.ui.screen.admin.ThongTinThietBiCard
import com.example.facilitiesmanagementpj.ui.screen.admin.ViTriThietBiCard
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVCreateYeuCauViewModel


// In ThietBiDetailScreen.kt

@OptIn(UnstableApi::class)
@Composable
fun ThietBiDetailScreen(
    navController: NavController,
    thietBiId: Int,
    isEditMode: Boolean = false,
    yeuCauId: Int? = null,
    viewModel: ThietBiDetailViewModel = hiltViewModel(),
    viewModelYC: QLDVCreateYeuCauViewModel = hiltViewModel()
) {
    val thietBi by viewModel.thietBi.collectAsState()
    val imageUris by viewModel.imageUris.collectAsState()
    val videoUri by viewModel.videoUri.collectAsState()
//    var moTa by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
//    var selectedLoaiYeuCau by remember { mutableStateOf<String?>(null) }
    var isExistingDetail by remember { mutableStateOf(false) }
//    var selectedImages by remember { mutableStateOf<List<Uri>>(imageUris) }
//    var selectedVideo by remember { mutableStateOf<Uri?>(videoUri) }
    var trangThai by remember { mutableStateOf("") }
    val viTri = viewModel.viTri.collectAsState().value
    val selectedTabIndex = remember { mutableStateOf(0) }
    val selectedLoaiYeuCau by viewModel.selectedLoaiYeuCau.collectAsState()
    val moTa by viewModel.moTa.collectAsState()
    val selectedImages by viewModel.selectedImages.collectAsState()
    val selectedVideo by viewModel.selectedVideo.collectAsState()





    LaunchedEffect(Unit) {
        viewModel.loadThietBi(thietBiId)

        if (yeuCauId != null) {
            // Lấy thông tin yêu cầu (để lấy trạng thái yêu cầu)
            viewModelYC.getYeuCauById(yeuCauId) { yeuCau ->
                trangThai = yeuCau.trangThai
            }

            // Lấy chi tiết yêu cầu
            viewModel.loadChiTietYeuCau(yeuCauId, thietBiId) { chiTietYeuCau ->
                isExistingDetail = true

                // Cập nhật dữ liệu mô tả và loại yêu cầu vào ViewModel để UI thấy
                viewModel.updateMoTa(chiTietYeuCau.moTa)
                viewModel.updateSelectedLoaiYeuCau(chiTietYeuCau.loaiYeuCau)

                // Sau khi load chi tiết yêu cầu, load luôn media
                viewModel.loadMedia(chiTietYeuCau.id) { images, video ->
                    viewModel.updateSelectedImages(images)
                    viewModel.updateSelectedVideo(video)
                }
            }
        }
    }


//    LaunchedEffect(imageUris, videoUri) {
//        selectedImages = imageUris
//        selectedVideo = videoUri
//    }

    ScaffoldLayout(
        title = "Chi tiết thiết bị",
        navController = navController,
        showBottomBar = false
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .then(modifier)
        ) {
            // Nếu có edit mode thì mới hiện TabRow
            if (isEditMode) {
                TabRow(
                    selectedTabIndex = selectedTabIndex.value,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Tab(
                        selected = selectedTabIndex.value == 0,
                        onClick = { selectedTabIndex.value = 0 },
                        text = { Text("Thông tin thiết bị") }
                    )
                    Tab(
                        selected = selectedTabIndex.value == 1,
                        onClick = { selectedTabIndex.value = 1 },
                        text = { Text("Chi tiết yêu cầu") }
                    )
                }
            }

            // Nội dung theo tab
            when (selectedTabIndex.value) {
                0 -> {  // Tab Thông tin thiết bị
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        thietBi?.let { thietBiItem ->
                            ThongTinThietBiCard(thietBi = thietBiItem)
                            ViTriThietBiCard(viTri)
                            CardThongKeBaoDuong(thietBiItem)
                        } ?: run {
                            Text(
                                "Đang tải dữ liệu...",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            )
                        }
                    }
                }

                1 -> { // Tab Chi tiết yêu cầu
                    if (isEditMode) {
                        ChiTietYeuCauEditSection(
                            isExistingDetail = isExistingDetail,
                            isEditMode = isEditMode,
                            yeuCauId = yeuCauId,
                            thietBiId = thietBiId,
                            trangThai = trangThai,
                            selectedLoaiYeuCau = selectedLoaiYeuCau,
                            moTa = moTa,
                            selectedImages = selectedImages,
                            selectedVideo = selectedVideo,
                            navController = navController,
                            viewModel = viewModel
                        )
                    } else {
                        // Bảo vệ trường hợp không được chỉnh sửa
                        Text(
                            "Bạn không có quyền chỉnh sửa yêu cầu này.",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.background)
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//                .then(modifier)
//        ) {
//
//            thietBi?.let { thietBiItem ->
//                ThongTinThietBiCard(thietBi = thietBiItem)
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                ViTriThietBiCard(viTri)
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                CardThongKeBaoDuong(thietBiItem)
//
//                if (isEditMode) {
//                    val selectedLoaiYeuCau by viewModel.selectedLoaiYeuCau.collectAsState()
//                    val moTa by viewModel.moTa.collectAsState()
//                    val selectedImages by viewModel.selectedImages.collectAsState()
//                    val selectedVideo by viewModel.selectedVideo.collectAsState()
//
//                    ChiTietYeuCauEditSection(
//                        isExistingDetail = isExistingDetail,
//                        isEditMode = isEditMode,
//                        yeuCauId = yeuCauId,
//                        thietBiId = thietBiId,
//                        trangThai = trangThai, // hoặc trạng thái từ thietBi chi tiết
//                        selectedLoaiYeuCau = selectedLoaiYeuCau,
//                        moTa = moTa,
//                        selectedImages = selectedImages,
//                        selectedVideo = selectedVideo,
//                        navController = navController,
//                        viewModel = viewModel
//                    )
//                }
//
////                if (isEditMode) {
////                    Spacer(modifier = Modifier.height(16.dp))
////                    Text("Nhập chi tiết yêu cầu", style = MaterialTheme.typography.headlineMedium)
////
////                    Text("Loại yêu cầu:")
////                    Box(modifier = Modifier.fillMaxWidth()) {
////                        Button(onClick = { expanded = true }) {
////                            Text(selectedLoaiYeuCau ?: "Chọn loại yêu cầu")
////                        }
////                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
////                            LoaiYeuCau.ALL.forEach { loai ->
////                                DropdownMenuItem(
////                                    text = { Text(loai) },
////                                    onClick = {
////                                        selectedLoaiYeuCau = loai
////                                        expanded = false
////                                    }
////                                )
////                            }
////                        }
////                    }
////
////                    OutlinedTextField(
////                        value = moTa,
////                        onValueChange = { moTa = it },
////                        label = { Text("Mô tả chi tiết") }
////                    )
////
////                    if (trangThai == TrangThaiYeuCau.NHAP) {
////                        Button(
////                            onClick = {
////                                if (yeuCauId != null && selectedLoaiYeuCau != null) {
////                                    if (isExistingDetail) {
////                                        viewModel.updateChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa, selectedImages, selectedVideo)
////                                    } else {
////                                        viewModel.addChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa, selectedImages, selectedVideo)
////                                    }
////                                    navController.popBackStack()
////                                }
////                            },
////                            modifier = Modifier.fillMaxWidth(),
////                            enabled = selectedLoaiYeuCau != null && moTa.isNotBlank()
////                        ) {
////                            Text(if (isExistingDetail) "Cập nhật yêu cầu" else "Thêm vào yêu cầu")
////                        }
////                    } else {
////                        Text(
////                            text = "Không thể chỉnh sửa yêu cầu này",
////                            color = Color.Red,
////                            style = MaterialTheme.typography.bodyLarge,
////                            modifier = Modifier.padding(vertical = 8.dp)
////                        )
////                    }
////
////                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
////                        ImagePickerSection(selectedImages = selectedImages, onImagesSelected = { images -> selectedImages = images })
////                        VideoPickerSection(selectedVideo = selectedVideo, onVideoSelected = { video -> selectedVideo = video })
////                    }
////                }
//
//            } ?: run {
//                Text(
//                    "Đang tải dữ liệu...",
//                    modifier = Modifier.fillMaxSize().padding(16.dp)
//                )
//            }
//        }

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
//                    if (trangThai == TrangThaiYeuCau.NHAP) {
//                        Button(
//                            onClick = {
//                                if (yeuCauId != null && selectedLoaiYeuCau != null) {
//                                    if (isExistingDetail) {
//                                        viewModel.updateChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa, selectedImages, selectedVideo)
//                                    } else {
//                                        viewModel.addChiTietYeuCau(yeuCauId, thietBiId, selectedLoaiYeuCau!!, moTa, selectedImages, selectedVideo)
//                                    }
//                                    navController.popBackStack()
//                                }
//                            },
//                            modifier = Modifier.fillMaxWidth(),
//                            enabled = selectedLoaiYeuCau != null && moTa.isNotBlank()
//                        ) {
//                            Text(if (isExistingDetail) "Cập nhật yêu cầu" else "Thêm vào yêu cầu")
//                        }
//                    } else {
//                        Text(
//                            text = "Không thể chỉnh sửa yêu cầu này",
//                            color = Color.Red,
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = Modifier.padding(vertical = 8.dp)
//                        )
//                    }
//
//                    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//                        ImagePickerSection(selectedImages = selectedImages, onImagesSelected = { images -> selectedImages = images })
//                        VideoPickerSection(selectedVideo = selectedVideo, onVideoSelected = { video -> selectedVideo = video })
//                    }
//                }
//            } ?: run {
//                Text("Đang tải dữ liệu...", modifier = Modifier.fillMaxSize().padding(16.dp))
//            }
//        }
    }
}



@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChiTietYeuCauEditSection(
    isExistingDetail: Boolean,
    isEditMode: Boolean,
    yeuCauId: Int?,
    thietBiId: Int,
    trangThai: String,
    selectedLoaiYeuCau: String?,
    moTa: String,
    selectedImages: List<Uri>,
    selectedVideo: Uri?,
    navController: NavController,
    viewModel: ThietBiDetailViewModel
) {
    if (!isEditMode) return

    var showLoaiYeuCauSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isExistingDetail) "Cập nhật chi tiết yêu cầu" else "Thêm chi tiết yêu cầu",
            style = MaterialTheme.typography.headlineSmall
        )

        // Button chọn Loại yêu cầu
        OutlinedButton(
            onClick = { showLoaiYeuCauSheet = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(selectedLoaiYeuCau ?: "Chọn loại yêu cầu")
        }

        // Sheet chọn loại yêu cầu
        if (showLoaiYeuCauSheet) {
            ModalBottomSheet(
                onDismissRequest = { showLoaiYeuCauSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        "Chọn loại yêu cầu",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LoaiYeuCau.ALL.forEach { loai ->
                        ListItem(
                            headlineContent = { Text(loai) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.updateSelectedLoaiYeuCau(loai)
                                    showLoaiYeuCauSheet = false
                                }
                        )
                    }
                }
            }
        }

        // Mô tả chi tiết
        OutlinedTextField(
            value = moTa,
            onValueChange = { viewModel.updateMoTa(it) },
            label = { Text("Mô tả chi tiết yêu cầu") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            maxLines = 6,
            singleLine = false,
            isError = moTa.isBlank()
        )

        if (trangThai == TrangThaiYeuCau.NHAP) {
            Button(
                onClick = {
                    if (yeuCauId != null && selectedLoaiYeuCau != null) {
                        if (isExistingDetail) {
                            viewModel.updateChiTietYeuCau(
                                yeuCauId,
                                thietBiId,
                                selectedLoaiYeuCau,
                                moTa,
                                selectedImages,
                                selectedVideo
                            )
                        } else {
                            viewModel.addChiTietYeuCau(
                                yeuCauId,
                                thietBiId,
                                selectedLoaiYeuCau,
                                moTa,
                                selectedImages,
                                selectedVideo
                            )
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !selectedLoaiYeuCau.isNullOrBlank() && moTa.isNotBlank()
            ) {
                Text(if (isExistingDetail) "Cập nhật chi tiết" else "Thêm vào yêu cầu")
            }
        } else {
            Text(
                text = "Không thể chỉnh sửa yêu cầu này.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        // Ảnh và Video picker
        Text(
            text = "Ảnh/Video minh chứng",
            style = MaterialTheme.typography.titleMedium
        )

        ImagePickerSection(
            selectedImages = selectedImages,
            onImagesSelected = { images -> viewModel.updateSelectedImages(images) }
        )

        Spacer(Modifier.height(8.dp))

        VideoPickerSection(
            selectedVideo = selectedVideo,
            onVideoSelected = { video -> viewModel.updateSelectedVideo(video) }
        )
    }
}




