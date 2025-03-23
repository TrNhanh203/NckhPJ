package com.example.facilitiesmanagementpj.ui.screen.admin

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.ui.viewmodel.PhanCongDetailViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import com.example.facilitiesmanagementpj.ui.component.VideoPreviewAdmin


@Composable
fun PhanCongDetailScreen(
    navController: NavController,
    phanCongId: Int
) {
    val viewModel: PhanCongDetailViewModel = hiltViewModel()
    val tabTitles = listOf("Kỹ thuật viên", "Thông tin","Thiết bị", "Ảnh & video")
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadPhanCongChiTiet(phanCongId)
    }

    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout(
        title = "Chi tiết phân công",
        navController = navController,
        showBottomBar = false,

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(paddingValues)

        ) {

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 0.dp
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> TabKTV(viewModel)
                1 -> TabThongTin(viewModel)
                2 -> TabThietBi(viewModel)
                3 -> TabMinhChung(viewModel)
            }
        }
    }
}

@Composable
fun TabKTV(viewModel: PhanCongDetailViewModel) {
    // TODO: Hiển thị danh sách kỹ thuật viên phân công
    Text("Danh sách KTV")
}

@Composable
fun TabThongTin(viewModel: PhanCongDetailViewModel) {
    val phanCong = viewModel.phanCong.collectAsState().value
    val chiTiet = viewModel.chiTietYeuCau.collectAsState().value
    val tenDonVi = viewModel.tenDonVi.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Thông tin chi tiết yêu cầu", style = MaterialTheme.typography.titleMedium)

        chiTiet?.let {
            Text("Loại yêu cầu: ${it.loaiYeuCau}")
            Text("Mô tả: ${it.moTa}")
        }

        Text("Đơn vị yêu cầu: $tenDonVi")

        phanCong?.let {
            Text("\nThông tin phân công:", style = MaterialTheme.typography.titleMedium)
            Text("Loại phân công: ${it.loaiPhanCong}")
            Text("Ghi chú: ${it.ghiChu ?: "Không có"}")
            Text("Mức độ ưu tiên: ${it.mucDoUuTien}")
            Text("Trạng thái: ${it.trangThai}")
            Text("Thời gian tạo: ${it.thoiGianTaoPhanCong}")
            Text("Số lượng KTV: ${it.soLuongKTVThamGia ?: "Chưa xác định"}")
        }
    }
}

@Composable
fun TabThietBi(viewModel: PhanCongDetailViewModel) {
    val thietBi = viewModel.thietBi.collectAsState().value
    val viTri = viewModel.viTri.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Thông tin thiết bị", style = MaterialTheme.typography.titleMedium)

        thietBi?.let {
            Text("Tên thiết bị: ${it.tenThietBi}")
            Text("Loại thiết bị ID: ${it.loaiThietBiId}")
            Text("Ngày cài đặt: ${it.ngayDaCat ?: "Chưa rõ"}")
            Text("Bảo dưỡng định kỳ (ngày): ${it.baoDuongDinhKy ?: "Không xác định"}")
            Text("Ngày bảo dưỡng gần nhất: ${it.ngayBaoDuongGanNhat ?: "Không có"}")
            Text("Vị trí: $viTri")

            Spacer(modifier = Modifier.height(8.dp))
            Text("Mô tả:", style = MaterialTheme.typography.bodyLarge)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
                    .background(Color(0xFFF2F2F2))
            ) {
                BasicTextField(
                    value = it.moTa ?: "Không có mô tả",
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}

@Composable
fun TabMinhChung(viewModel: PhanCongDetailViewModel) {
    val imageUris = viewModel.imageUris.collectAsState().value
    val videoUri = viewModel.videoUri.collectAsState().value

    var showImagePreview by remember { mutableStateOf<Uri?>(null) }
    var showVideoPreview by remember { mutableStateOf<Uri?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Minh chứng đính kèm", style = MaterialTheme.typography.titleMedium)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(imageUris) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showImagePreview = uri },
                    contentScale = ContentScale.Crop
                )
            }

            item {
                videoUri?.let { uri ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .clickable { showVideoPreview = uri },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("▶", color = Color.White, fontSize = 32.sp)
                    }
                }
            }
        }
    }

    showImagePreview?.let { uri ->
        Dialog(onDismissRequest = { showImagePreview = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

    showVideoPreview?.let { uri ->
        Dialog(onDismissRequest = { showVideoPreview = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                VideoPreviewAdmin(uri = uri)
            }
        }
    }
}


