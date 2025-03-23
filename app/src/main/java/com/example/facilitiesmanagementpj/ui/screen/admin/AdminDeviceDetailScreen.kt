package com.example.facilitiesmanagementpj.ui.screen.admin

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
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
    val chiTietYeuCau by viewModel.chiTietYeuCau.collectAsState()
    val tenDonVi by viewModel.tenDonVi.collectAsState()
    val viTri by viewModel.viTri.collectAsState()
    val isYeuCau = yeuCauId != 0
    var showFullImage by remember { mutableStateOf<Uri?>(null) }
    var showFullVideo by remember { mutableStateOf<Uri?>(null) }

    val isLoading = thietBi == null

    LaunchedEffect(Unit) {
        viewModel.loadThietBi(thietBiId)
        if (isYeuCau) viewModel.loadChiTietYeuCau(yeuCauId, thietBiId)
    }

    ScaffoldLayout(
        title = if (isYeuCau) "Chi tiết yêu cầu" else "Chi tiết thiết bị",
        navController = navController,
        showBottomBar = false
    ) { modifier ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
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
                            Text("Vị trí: $viTri")
                        }
                    }

                    if (isYeuCau && chiTietYeuCau != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Thông tin yêu cầu", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                Text("Đơn vị yêu cầu: $tenDonVi")
                                Text("Loại yêu cầu: ${chiTietYeuCau!!.loaiYeuCau}")
                                Text("Mô tả: ${chiTietYeuCau!!.moTa}")
                            }
                        }
                    }

                    if (videoUri != null || imageUris.isNotEmpty()) {
                        Text("Minh chứng yêu cầu:", fontWeight = FontWeight.SemiBold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(imageUris) { uri ->
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { showFullImage = uri },
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
                                            .clickable { showFullVideo = uri },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("▶", color = Color.White, fontSize = 32.sp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { /* TODO: lịch sử bảo trì */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Xem lịch sử bảo trì")
                    }

                    if (isYeuCau) {
                        val trangThaiYeuCau = yeuCau?.trangThai ?: ""
                        Button(
                            onClick = { /* điều hướng */ },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = trangThaiYeuCau == TrangThaiYeuCau.DA_XAC_NHAN,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (trangThaiYeuCau == TrangThaiYeuCau.DA_XAC_NHAN) MaterialTheme.colorScheme.primary else Color.Gray
                            )
                        ) {
                            Text("Phân công kỹ thuật viên")
                        }
                    }
                }
            }
        }
    }

    showFullImage?.let { uri ->
        Dialog(onDismissRequest = { showFullImage = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                IconButton(
                    onClick = { showFullImage = null },
                    modifier = Modifier.padding(16.dp).size(36.dp).background(Color.DarkGray, CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Đóng", tint = Color.White)
                }
            }
        }
    }

    showFullVideo?.let { uri ->
        Dialog(onDismissRequest = { showFullVideo = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Video nằm giữa màn hình
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    VideoPreviewAdmin(uri = uri)
                }

                // Nút đóng nằm trên cùng bên phải
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = { showFullVideo = null },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.DarkGray, CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng", tint = Color.White)
                    }
                }
            }
        }

    }
}





//package com.example.facilitiesmanagementpj.ui.screen.admin
//
//import androidx.annotation.OptIn
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.media3.common.util.UnstableApi
//import androidx.navigation.NavController
//import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
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
//    val yeuCau by viewModel.yeuCau.collectAsState()
//    val chiTietYeuCau by viewModel.chiTietYeuCau.collectAsState()
//    val tenDonVi by viewModel.tenDonVi.collectAsState()
//    val viTri by viewModel.viTri.collectAsState()
//    val isYeuCau = yeuCauId != 0
//
//    LaunchedEffect(Unit) {
//        viewModel.loadThietBi(thietBiId)
//        if (isYeuCau) viewModel.loadChiTietYeuCau(yeuCauId, thietBiId)
//    }
//
//    ScaffoldLayout(
//        title = if (isYeuCau) "Chi tiết yêu cầu" else "Chi tiết thiết bị",
//        navController = navController,
//        showBottomBar = false
//    ) { modifier ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .then(modifier),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            thietBi?.let { tb ->
//                Card(
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
//                    shape = RoundedCornerShape(12.dp)
//                ) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        Text("Thông tin thiết bị", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                        Spacer(Modifier.height(8.dp))
//                        Text("Tên thiết bị: ${tb.tenThietBi}")
//                        Text("Loại thiết bị: ${tb.loaiThietBiId}")
//                        Text("Trạng thái: ${tb.trangThai}")
//                        Text("Ghi chú: ${tb.ghiChu ?: "Không có"}")
//                        Text("Vị trí: $viTri")
//                    }
//                }
//
//                if (isYeuCau && chiTietYeuCau != null) {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("Thông tin yêu cầu", fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                            Spacer(Modifier.height(8.dp))
//                            Text("Đơn vị yêu cầu: $tenDonVi")
//                            Text("Loại yêu cầu: ${chiTietYeuCau!!.loaiYeuCau}")
//                            Text("Mô tả: ${chiTietYeuCau!!.moTa}")
//                        }
//                    }
//                }
//
//                if (isYeuCau) {
//                    val trangThaiYeuCau = yeuCau?.trangThai ?: ""
//                    Button(
//                        onClick = { /* điều hướng */ },
//                        modifier = Modifier.fillMaxWidth(),
//                        enabled = trangThaiYeuCau == TrangThaiYeuCau.DA_XAC_NHAN,
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = if (trangThaiYeuCau == TrangThaiYeuCau.DA_XAC_NHAN) MaterialTheme.colorScheme.primary else Color.Gray
//                        )
//                    ) {
//                        Text("Phân công kỹ thuật viên")
//                    }
//                }
//
//                Button(
//                    onClick = { /* TODO: lịch sử bảo trì */ },
//                    modifier = Modifier.fillMaxWidth(),
//                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
//                ) {
//                    Text("Xem lịch sử bảo trì")
//                }
//
//                if (imageUris.isNotEmpty()) {
//                    Text("Ảnh thiết bị:", fontWeight = FontWeight.SemiBold)
//                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                        items(imageUris) { imageUri ->
//                            MediaPreviewAdmin(uri = imageUri)
//                        }
//                    }
//                }
//
//                videoUri?.let { uri ->
//                    Text("Video thiết bị:", fontWeight = FontWeight.SemiBold)
//                    VideoPreviewAdmin(uri = uri)
//                }
//            } ?: run {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    CircularProgressIndicator()
//                }
//            }
//        }
//    }
//}
//
//
