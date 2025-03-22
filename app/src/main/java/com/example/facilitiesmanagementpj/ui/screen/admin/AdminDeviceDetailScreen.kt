package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.MediaPreviewAdmin
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.component.VideoPreviewAdmin
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminDeviceDetailViewModel

@OptIn(UnstableApi::class)
@Composable
fun AdminDeviceDetailScreen(
    navController: NavController,
    thietBiId: Int,
    yeuCauId: Int
) {
    val viewModel: AdminDeviceDetailViewModel = hiltViewModel()
    val thietBi by viewModel.thietBi.collectAsState()
    val imageUris by viewModel.imageUris.collectAsState()
    val videoUri by viewModel.videoUri.collectAsState()
    val yeuCau by viewModel.yeuCau.collectAsState()
    val isYeuCau = yeuCauId != 0

    LaunchedEffect(Unit) {
        viewModel.loadThietBi(thietBiId)
        if (isYeuCau) viewModel.loadChiTietYeuCau(yeuCauId, thietBiId)
    }

    ScaffoldLayout(
        title = if (isYeuCau) "Chi tiết yêu cầu" else "Chi tiết thiết bị",
        navController = navController,
        showBottomBar = false
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(modifier),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            thietBi?.let { tb ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Thông tin thiết bị", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text("Tên thiết bị: ${tb.tenThietBi}")
                        Text("Loại thiết bị: ${tb.loaiThietBiId}")
                        Text("Trạng thái: ${tb.trangThai}")
                        Text("Ghi chú: ${tb.ghiChu ?: "Không có"}")
                    }
                }

                if (isYeuCau) {
                    val trangThaiYeuCau = yeuCau?.trangThai ?: ""
                    Button(
                        onClick = { /* TODO: điều hướng đến màn phân công */ },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = trangThaiYeuCau == TrangThaiYeuCau.DA_XAC_NHAN,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (trangThaiYeuCau == TrangThaiYeuCau.DA_XAC_NHAN) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text("Phân công kỹ thuật viên")
                    }
                }

                Button(
                    onClick = { /* TODO: lịch sử bảo trì */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Xem lịch sử bảo trì")
                }

                if (imageUris.isNotEmpty()) {
                    Text("Ảnh thiết bị:", fontWeight = FontWeight.SemiBold)
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(imageUris) { imageUri ->
                            MediaPreviewAdmin(uri = imageUri)
                        }
                    }
                }

                videoUri?.let { uri ->
                    Text("Video thiết bị:", fontWeight = FontWeight.SemiBold)
                    VideoPreviewAdmin(uri = uri)
                }
            } ?: run {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}



//package com.example.facilitiesmanagementpj.ui.screen.admin
//
//import android.net.Uri
//import androidx.annotation.OptIn
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.ui.PlayerView
//import androidx.navigation.NavController
//import coil.compose.rememberImagePainter
//import com.example.facilitiesmanagementpj.ui.component.MediaPreviewAdmin
//import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
//import com.example.facilitiesmanagementpj.ui.component.VideoPreviewAdmin
//import com.example.facilitiesmanagementpj.ui.viewmodel.AdminDeviceDetailViewModel
//
//@OptIn(UnstableApi::class)
//@Composable
//fun AdminDeviceDetailScreen(
//    navController: NavController,
//    thietBiId: Int,
//    yeuCauId: Int
//) {
//    val viewModel: AdminDeviceDetailViewModel = hiltViewModel()
//    val thietBi by viewModel.thietBi.collectAsState()
//    val imageUris by viewModel.imageUris.collectAsState()
//    val videoUri by viewModel.videoUri.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadThietBi(thietBiId)
//        viewModel.loadChiTietYeuCau(yeuCauId, thietBiId)
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
//                .then(modifier)
//        ) {
//            thietBi?.let { thietBiItem ->
//                Text("Tên thiết bị: ${thietBiItem.tenThietBi}")
//                Text("Loại: ${thietBiItem.loaiThietBiId}")
//                Text("Trạng thái: ${thietBiItem.trangThai}")
//                Text("Ghi chú: ${thietBiItem.ghiChu ?: "Không có"}")
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//
//                Button(
//                    onClick = { },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Xem lịch sử bảo trì")
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//
//
//
//                // Hiển thị ảnh nếu có
//                if (imageUris.isNotEmpty()) {
//                    Text("Ảnh thiết bị:")
//                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
//                        items(imageUris) { imageUri ->
//                            MediaPreviewAdmin(uri = imageUri)
//                        }
//                    }
//                }
//
//                // Hiển thị video nếu có
//                videoUri?.let { uri ->
//                    Text("Video thiết bị:")
//                    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//                        VideoPreviewAdmin(uri = uri)
//                    }
//                }
//            } ?: run {
//                Text("Đang tải dữ liệu...", modifier = Modifier.fillMaxSize().padding(16.dp))
//            }
//
//
//        }
//
//
//    }
//}
//
