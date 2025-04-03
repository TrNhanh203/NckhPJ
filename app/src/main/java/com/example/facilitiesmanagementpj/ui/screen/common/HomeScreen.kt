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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight



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
//        LazyColumn(
//            modifier = modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            item {
//                PhanCongItemCard(
//                    loaiPhanCong = "Sửa Chữa",
//                    tenThietBi = "Máy lạnh LG Inverter 1HP",
//                    loaiThietBi = "Điều hòa",
//                    uuTien = 1,
//                    trangThaiPhanCong = "Đã Chấp Nhận"
//                )
//            }
//
//            item {
//                PhanCongItemCard(
//                    loaiPhanCong = "Kiểm Tra",
//                    tenThietBi = "Camera hành lang Tầng 2",
//                    loaiThietBi = "Giám sát",
//                    uuTien = 2,
//                    trangThaiPhanCong = "Tạm Nghỉ"
//                )
//            }
//
//            item {
//                PhanCongItemCard(
//                    loaiPhanCong = "Tháo Dỡ",
//                    tenThietBi = "Máy chiếu Epson EB-X41",
//                    loaiThietBi = "Trình chiếu",
//                    uuTien = 3,
//                    trangThaiPhanCong = "Đang Thực Hiện"
//                )
//            }
//        }
//    }
}

@Composable
fun PhanCongItemCard(
    loaiPhanCong: String,
    tenThietBi: String,
    loaiThietBi: String,
    uuTien: Int,
    trangThaiPhanCong: String,
    thoiGianDuKien: String = "15/04 - 10:30", // giả lập
    onClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    val statusColor = when (trangThaiPhanCong) {
        "Tạm Nghỉ" -> Color(0xFFFF9800)
        "Đã Chấp Nhận" -> Color(0xFF4CAF50)
        "Đang Thực Hiện" -> Color(0xFF2196F3)
        else -> Color.Gray
    }

    val iconPrefix = when (loaiPhanCong) {
        "Sửa Chữa" -> "🛠"
        "Kiểm Tra" -> "🔍"
        "Tháo Dỡ" -> "📦"
        else -> "📌"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable(

            ) { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Dòng 1: loại phân công + trạng thái
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$iconPrefix $loaiPhanCong",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                AssistChip(
                    onClick = {},
                    label = { Text(trangThaiPhanCong) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = statusColor,
                        labelColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Thiết bị: $tenThietBi", style = MaterialTheme.typography.bodyMedium)
            Text("Loại thiết bị: $loaiThietBi", style = MaterialTheme.typography.bodyMedium)
            Text(
                "Hạn: $thoiGianDuKien",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                PriorityLevelBar(level = uuTien)

                IconButton(onClick = onMoreClick) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Tùy chọn")
                }
            }
        }
    }
}


@Composable
fun PriorityLevelBar(
    level: Int, // từ 1 đến 5
    modifier: Modifier = Modifier
) {
    val maxLevel = 5
    val filledFraction = level.coerceIn(1, maxLevel) / maxLevel.toFloat()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(24.dp)
    ) {
        // Icon lửa 🔥
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(Color(0xFF6A1B9A), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("🔥", fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        }

        Spacer(Modifier.width(8.dp))

        // Thanh ưu tiên
        Box(
            modifier = Modifier
                .height(16.dp)
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFD1C4E9)) // màu nền chưa đầy
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(filledFraction)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFA000)) // màu đầy (cam sáng)
            )
        }

        Spacer(Modifier.width(8.dp))

        Text("Lv $level", style = MaterialTheme.typography.labelMedium)
    }
}

