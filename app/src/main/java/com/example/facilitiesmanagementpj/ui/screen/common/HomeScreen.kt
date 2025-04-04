package com.example.facilitiesmanagementpj.ui.screen.common

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.ui.screen.kythuatvien.formatMillis
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.LoaiTacVu


@Composable
fun HomeScreen(navController: NavController) {
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Xác nhận thoát") },
            text = { Text("Bạn có chắc chắn muốn thoát không?") },
            confirmButton = {
                TextButton(onClick = {
                    showExitDialog = false
                    // Close the app
                    (navController.context as Activity).finish()
                    //navController.popBackStack(navController.graph.startDestinationId, true)
                }) {
                    Text("Thoát")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    ScaffoldLayout(title = "Trang chủ", navController = navController, isHomeScreen = true) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(modifier)
        ) {
            Text("Nội dung màn hình chính", style = MaterialTheme.typography.headlineMedium)
        }
    }
//    ScaffoldLayout(
//        title = "Xem thử phân công",
//        navController = rememberNavController(), // hoặc truyền navController nếu cần
//        showBottomBar = false
//    ) { modifier ->
//        DemoDangLamViecScreen()
//
//    }
}


@Composable
fun DemoDangLamViecScreen() {
    val tacVuDangChon = remember { mutableStateOf<LoaiTacVu?>(null) }
    val showTacVuSheet = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val trangThai = TrangThaiPhanCong.DANG_THUC_HIEN
    val thoiGianDuKien = 30 // phút
    val thoiGianConLai = 10 * 60 * 1000L // còn 10 phút
    val dangXinGiaHan = false
    val soLanGiaHan = 2
    val tongThoiGianDaXinGiaHan = 15

    if (trangThai == TrangThaiPhanCong.DANG_THUC_HIEN) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (thoiGianConLai <= 0) "Đã quá hạn:" else "Thời gian còn lại:",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(12.dp))

            // Đồng hồ thời gian
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(240.dp)
            ) {
                CircularProgressIndicator(
                    progress = (
                            (thoiGianDuKien * 60_000L - thoiGianConLai.coerceAtMost(thoiGianDuKien * 60_000L)) /
                                    (thoiGianDuKien * 60_000f)
                            ).coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.secondaryContainer,
                    strokeWidth = 28.dp
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formatMillis(kotlin.math.abs(thoiGianConLai)),
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (thoiGianConLai < 5 * 60 * 1000)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(Modifier.height(4.dp))

                    IconButton(onClick = {
                        tacVuDangChon.value = LoaiTacVu.XIN_GIA_HAN
                        showTacVuSheet.value = true
                    }) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = "Gia hạn",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Thống kê gia hạn
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = "Đã gia hạn: $soLanGiaHan lần",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tổng cộng: $tongThoiGianDaXinGiaHan phút",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilledTonalButton(
                    onClick = { /* Fake Tạm nghỉ */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Pause, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Tạm nghỉ")
                }

                FilledTonalButton(
                    onClick = { /* Fake hoàn thành */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Hoàn thành")
                }
            }

            Spacer(Modifier.height(16.dp))

            FilledTonalButton(
                onClick = { /* Fake chụp ảnh */ },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Minh chứng")
            }

            Spacer(Modifier.weight(1f))

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
            )
        }
    }
}
