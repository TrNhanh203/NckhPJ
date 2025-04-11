package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.KtvDanhSachCongViecViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.data.entity.KyThuatVienWithTaiKhoan
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.ui.component.getTrangThaiPcKtvColor
import com.example.facilitiesmanagementpj.ui.navigation.Screen


@Composable
fun KtvCongViecHienTaiScreen(
    tkKtvId: Int,
    hoTen: String,
    soDienThoai: String,
    phanCongId: Int,
    navController: NavController,
    viewModel: KtvDanhSachCongViecViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadTasksWithTime(tkKtvId)
    }
    val context = LocalContext.current
    val allTasks by viewModel.tasksWithTime.collectAsState()
    val dangLam = allTasks.filter {
        it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.DA_CHAP_NHAN ||
                it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.DANG_THUC_HIEN ||
                it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.TAM_NGHI ||
                it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.CHO_PHAN_HOI
    }
    val soTask = dangLam.size
    val tongPhutConLai = dangLam.sumOf { it.thoiGianConLaiThamKhao ?: 0 }
    val dsSapXepTheoUuTien = dangLam.sortedByDescending { it.pc.phanCong.phanCong.mucDoUuTien }

    ScaffoldLayout(
        title = "",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(padding)
        ) {
            val avatarIndex = (1..6).random()
            val avatarResId = when (avatarIndex) {
                1 -> R.drawable.engineer01
                2 -> R.drawable.engineer02
                3 -> R.drawable.engineer03
                4 -> R.drawable.engineer04
                5 -> R.drawable.engineer05
                else -> R.drawable.engineer06
            }
//            // 🟦 Header: tên KTV + ảnh đại diện + số điện thoại
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Công việc hiện tại của",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(12.dp))

                // ✅ Vùng avatar có viền, bóng
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(6.dp, CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .background(MaterialTheme.colorScheme.surface, CircleShape)
                        .padding(6.dp) // khoảng cách với ảnh
                ) {
                    Image(
                        painter = painterResource(id = avatarResId),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(CircleShape)
                            .fillMaxSize()
                    )
                }

                Spacer(Modifier.height(12.dp))

                // ✅ Họ tên nổi bật
                Text(
                    hoTen,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // ✅ Số điện thoại và icon gọi
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        soDienThoai,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(onClick = { goiDien(context, soDienThoai) }) {
                        Icon(
                            imageVector = Icons.Default.Call,
                            contentDescription = "Gọi",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }



            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column {

                    // 🟦 Header có màu chủ đạo
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tổng: $soTask",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Còn lại: ${tongPhutConLai}p",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    // 🟨 Danh sách task
                    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {
                        dsSapXepTheoUuTien.forEachIndexed { index, item ->
                            val isFirst = index == 0
                            val isLast = index == dsSapXepTheoUuTien.lastIndex

                            val shape = when {
                                isFirst && isLast -> RoundedCornerShape(0.dp, 0.dp, 0.dp, 0.dp)
                                isFirst -> RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                                isLast -> RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                                else -> RoundedCornerShape(0.dp)
                            }

                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainerLow,
                                shape = shape,
                                tonalElevation = 4.dp,  // Thêm shadow nhẹ cho viền
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                        shape = shape
                                    ),
                                shadowElevation = 4.dp
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                item.pc.phanCong.phanCong.loaiPhanCong,
                                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                                            )
                                            Spacer(Modifier.height(16.dp))
                                            Text(
                                                item.pc.phanCong.thietBi.loaiThietBi?.tenLoai ?: "",
                                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant,fontWeight = FontWeight.Medium)
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {

                                            // Thêm icon minh họa cho "Ưu tiên"
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                // Thanh ưu tiên (level bar) + icon minh họa
                                                PriorityLevelBarGameStyle(
                                                    level = item.pc.phanCong.phanCong.mucDoUuTien,
                                                    modifier = Modifier
                                                        //.padding(end = 8.dp)
                                                        .fillMaxWidth(0.5f)  // Chiếm 60% chiều rộng
                                                        .height(16.dp),
                                                    iconSize = 20.dp,
                                                )



                                            }

                                            Spacer(Modifier.height(16.dp))

                                            // Trạng thái + thời gian còn lại
                                            val trangThai = item.pc.phanCongKtv.trangThai
                                            val conLai = item.thoiGianConLaiThamKhao ?: 0

                                            if (trangThai == "Đang Thực Hiện" || trangThai == "Tạm Nghỉ") {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    if (conLai > 0) {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.remainingtime), // 👈 thay icon của bạn
                                                            contentDescription = "Time left",
                                                            modifier = Modifier
                                                                .size(18.dp)
                                                                .padding(end = 4.dp),
                                                            tint = Color.Unspecified // ✅ giữ màu PNG gốc
                                                        )
                                                    } else {
                                                        Icon(
                                                            painter = painterResource(id = R.drawable.expired),
                                                            contentDescription = "Late",
                                                            modifier = Modifier
                                                                .size(18.dp)
                                                                .padding(end = 4.dp),
                                                            tint = Color.Unspecified
                                                        )
                                                    }

                                                    Text(
                                                        text = if (conLai <= 0) "Đã trễ" else "${conLai}p",
                                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                                        color = if (conLai <= 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                                                    )
                                                }

                                            } else {
                                                AssistChip(
                                                    onClick = {},
                                                    label = {
                                                        Text(trangThai, style = MaterialTheme.typography.bodySmall)
                                                    },
                                                    colors = AssistChipDefaults.assistChipColors(
                                                        containerColor = getTrangThaiPcKtvColor(trangThai),
                                                        labelColor = Color.White
                                                    ),
                                                    shape = RoundedCornerShape(4.dp), // 👈 bo nhẹ hơn
                                                    modifier = Modifier
                                                        .defaultMinSize(minWidth = 0.dp)
                                                        .wrapContentWidth()
                                                        .height(18.dp)
                                                )

                                            }
                                        }
                                    }
                                }
                            }


                            if (!isLast) {
                                Divider(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                    thickness = 4.dp,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = { navController.popBackStack() }) {
                    Text("Quay lại")
                }

                Button(
                    onClick = { navController.navigate(Screen.XacNhanDeCuKtv.createRoute(phanCongId, tkKtvId)) }
                ) {
                    Text("Đề cử")
                }
            }

        }
    }
}


@Composable
fun PriorityLevelBarGameStyle(
    level: Int,
    modifier: Modifier = Modifier,
    height: Dp = 20.dp,
    iconSize: Dp = 24.dp,
    minFillPercent: Float = 0.35f
) {
    val maxLevel = 5
    val rawFraction = level.coerceIn(1, maxLevel) / maxLevel.toFloat()
    val fraction = rawFraction.coerceAtLeast(minFillPercent)

    val barHeight = height * (2f / 3f)
    val barCorner = barHeight / 2
    val adjustedFraction = maxOf(minFillPercent, minOf(fraction, 1f))


    val barColor = when (level) {
        5 -> Color(0xFFD32F2F) // đỏ đậm
        4 -> Color(0xFFFF7043) // cam đỏ
        3 -> Color(0xFFFFB300) // cam tươi
        2 -> Color(0xFFFFF176) // vàng sáng
        else -> Color(0xFFFFF9C4) // vàng nhạt
    }

    Box(
        modifier = modifier
            .height(height)
    ) {
        // Thanh nền
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterEnd)
                .height(barHeight)
                .shadow(2.dp, RoundedCornerShape(barCorner))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(barCorner))
                .clip(RoundedCornerShape(barCorner))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            // Phần đầy
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(adjustedFraction)
                    .clip(RoundedCornerShape(barCorner))
                    .background(barColor)
            )
        }

        // Icon đè lên đầu thanh
        Box(
            modifier = Modifier
                .requiredSize(iconSize) // ✅ đảm bảo vuông và không co lại
                .align(Alignment.CenterStart)
                .shadow(2.dp, CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.priority),
                contentDescription = "Priority Icon",
                tint = Color.Unspecified,
                modifier = Modifier.size(iconSize * 0.8f)
            )
        }

    }
}





