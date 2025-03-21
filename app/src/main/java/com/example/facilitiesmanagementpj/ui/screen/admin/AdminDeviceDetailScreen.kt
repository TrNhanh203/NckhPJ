package com.example.facilitiesmanagementpj.ui.screen.admin

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
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

    LaunchedEffect(Unit) {
        viewModel.loadThietBi(thietBiId)
        viewModel.loadChiTietYeuCau(yeuCauId, thietBiId)
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
                .then(modifier)
        ) {
            thietBi?.let { thietBiItem ->
                Text("Tên thiết bị: ${thietBiItem.tenThietBi}")
                Text("Loại: ${thietBiItem.loaiThietBiId}")
                Text("Trạng thái: ${thietBiItem.trangThai}")
                Text("Ghi chú: ${thietBiItem.ghiChu ?: "Không có"}")

                Spacer(modifier = Modifier.height(16.dp))

                // Approval Button
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Xem lịch sử bảo trì")
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Approval Button
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Phân công")
                }

                // Hiển thị ảnh nếu có
                if (imageUris.isNotEmpty()) {
                    Text("Ảnh thiết bị:")
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(imageUris) { imageUri ->
                            MediaPreviewAdmin(uri = imageUri)
                        }
                    }
                }

                // Hiển thị video nếu có
                videoUri?.let { uri ->
                    Text("Video thiết bị:")
                    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        VideoPreviewAdmin(uri = uri)
                    }
                }
            } ?: run {
                Text("Đang tải dữ liệu...", modifier = Modifier.fillMaxSize().padding(16.dp))
            }


        }


    }
}

