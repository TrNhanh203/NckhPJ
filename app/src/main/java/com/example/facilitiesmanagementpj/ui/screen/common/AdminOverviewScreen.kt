package com.example.facilitiesmanagementpj.ui.screen.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import androidx.compose.foundation.Canvas

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.facilitiesmanagementpj.ui.screen.admin.DashboardOption
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clip
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBiColor
import com.example.facilitiesmanagementpj.ui.screen.admin.getColorForTrangThaiPhanCong


@Composable
fun AdminOverviewScreen(navController: NavController) {
    ScaffoldLayout(title = "Tổng quan hệ thống", navController = navController, showBottomBar = true, isHomeScreen = false) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(16.dp)
                .then(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text("Admin Dashboard", style = MaterialTheme.typography.headlineMedium)
            }
            item {
//                YeuCauOverviewCard(
//                    modifier = Modifier.fillMaxWidth(),
//                    onClickDetail = { navController.navigate(Screen.AdminRequestList.route) }
//                )
            }
            item {
                DashboardOption(
                    title = "Quản lý tài khoản",
                    icon = Icons.Default.AccountCircle,
                    onClick = { navController.navigate(Screen.AdminAccount.route) }
                )
            }
            item {
                DashboardOption(
                    title = "Quản lý thiết bị",
                    icon = Icons.Default.Settings,
                    onClick = { navController.navigate(Screen.AdminDeviceList.route) }
                )
            }
            item {
                DashboardOption(
                    title = "Danh sách kỹ thuật viên",
                    icon = Icons.Default.AccountBox,
                    onClick = { navController.navigate(Screen.DanhSachKyThuatVien.route) }
                )
            }
            item {
                DashboardOption(
                    title = "Quản lý bài viết",
                    icon = Icons.Default.AccountBox,
                    onClick = { navController.navigate(Screen.AdminBaiViet.route) }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAdminOverviewScreen() {
    AdminOverviewScreen(navController = rememberNavController())
}


// ========================
// DeviceOverviewCard.kt
// ========================
private val fakeDeviceStatusCounts = mapOf(
    TrangThaiThietBi.DANG_HOAT_DONG to 120,
    TrangThaiThietBi.DANG_BAO_TRI to 20,
    TrangThaiThietBi.CHO_BAO_TRI to 0,
    TrangThaiThietBi.DA_NGUNG_SU_DUNG to 12
)

@Composable
fun DeviceOverviewCard(
    statusCounts: Map<String, Int>,
    modifier: Modifier = Modifier,
    onClickDetail: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 0.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp)) // Vuông bo nhẹ
                            .background(MaterialTheme.colorScheme.tertiaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DevicesOther,
                            contentDescription = null,
                            tint = Color.Unspecified // Giữ màu gốc của Icon
                        )
                    }
                    Text("Tổng quan thiết bị", style = MaterialTheme.typography.titleMedium)

                }

                IconButton(
                    onClick = onClickDetail,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Search, // kính lúp
                        contentDescription = "Xem chi tiết",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )


            Text(
                text = "Tổng số thiết bị: ${statusCounts.values.sum()}",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Normal)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TrangThaiThietBi.ALL.forEach { status ->
                    DeviceStatusBar(
                        label = status,
                        value = statusCounts[status] ?: 0,
                        total = statusCounts.values.sum()
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 0.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            DeviceRuler(totalDevices = statusCounts.values.sum())

        }
    }
}

@Composable
fun DeviceStatusBar(label: String, value: Int, total: Int) {
    if (value <= 0) return // Không render nếu không có dữ liệu

    val percent = if (total > 0) (value.toFloat() / total.toFloat()) else 0f
    val color = TrangThaiThietBiColor.getColor(label)

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        // Text Label
        Text(
            text = "$label ($value)",
            style = MaterialTheme.typography.bodySmall
        )

        // Thanh Bar
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 4.dp, // tăng nhẹ cho rõ nổi
            shadowElevation = 6.dp, // tăng shadow cho đậm hơn
            modifier = Modifier
                .fillMaxWidth(percent)
                .height(32.dp)
        )  {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(color, shape = RoundedCornerShape(8.dp))
            )
        }
    }
}



@Composable
fun DeviceRuler(totalDevices: Int) {
    val step = (totalDevices / 5).coerceAtLeast(1) // tối thiểu step = 1 tránh lỗi

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0..5) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .width(1.dp)
                        .height(6.dp)
                        .background(MaterialTheme.colorScheme.outline)
                )
                Text(
                    text = "${i * step}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewDeviceOverviewCard() {
    //DeviceOverviewCard(statusCounts = fakeDeviceStatusCounts)
}








// Mock trạng thái và số lượng
private val fakeStatusCounts = mapOf(
    "Chờ Xác Nhận" to 8,
    "Đã Xác Nhận" to 14,
    "Đang Xử Lý" to 6,
    "Đã Xử Lý" to 10,
    "Đã Từ Chối" to 2,
    "Đã Hủy Bỏ" to 1,
    "Đã Nghiệm Thu" to 5
)

@Composable
fun YeuCauOverviewCard(
    statusCounts: Map<String, Int>,
    modifier: Modifier = Modifier,
    onClickDetail: () -> Unit
) {


    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)


    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Phần trên
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 🟰 Cột trạng thái chuyển sang bên trái
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        val statusList = statusCounts.entries.toList()
                        statusList.forEach { (status, count) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier
                                        .size(10.dp)
                                        .background(getTrangThaiColor(status), shape = RoundedCornerShape(50))
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("$status: $count", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    // 🟰 DonutChart chuyển sang bên phải
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(140.dp) // tùy chỉnh kích thước
                    ) {
                        DonutChart(
                            data = statusCounts,
                            size = 120.dp,
                            thickness = 20.dp
                        )
                        Text(
                            text = "${statusCounts.values.sum()}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Normal
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

            }
            // Phần dưới
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .height(60.dp),
                contentAlignment = Alignment.CenterStart // căn trái cho title
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tình hình yêu cầu",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    Button(
                        onClick = onClickDetail,
                        modifier = Modifier
                            .offset(y = (-24).dp)
                            .padding(end = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Xem chi tiết")
                    }
                }
            }

        }
    }
}
@Composable
fun DonutChart(data: Map<String, Int>, size: Dp, thickness: Dp) {
    val total = data.values.sum().toFloat()
    val gapDegree = 2f // ➔ khoảng cách giữa các phần (độ)

    Canvas(
        modifier = Modifier.size(size)
    ) {
        var startAngle = -90f

        data.forEach { (status, count) ->
            val sweepAngle = (count / total) * 360f - gapDegree

            // Vẽ bóng mờ (như hướng dẫn trước)
            drawArc(
                color = Color.Black.copy(alpha = 0.08f),
                startAngle = startAngle + gapDegree / 2,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = thickness.toPx() + 4f)
            )

            // Vẽ phần chính
            drawArc(
                color = getTrangThaiColor(status),
                startAngle = startAngle + gapDegree / 2,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = thickness.toPx())
            )

            startAngle += (sweepAngle + gapDegree)
        }
    }
}
@Composable
fun StatusCard(label: String, count: Int, color: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(8.dp)
                        .background(color, shape = RoundedCornerShape(50))
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = color
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$count yêu cầu",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
fun getTrangThaiColor(trangThai: String): Color {
    return when (trangThai) {
        "Chờ Xác Nhận" -> Color(0xFFFFC107)
        "Đã Xác Nhận" -> Color(0xEB1976D2)
        "Đang Xử Lý" -> Color(0xF0FF9800)
        "Đã Xử Lý" -> Color(0xFF4CAF50)
        "Đã Từ Chối" -> Color(0xE1F44336)
        "Đã Hủy Bỏ" -> Color(0xFF9E9E9E)
        "Đã Nghiệm Thu" -> Color(0xFF009688)
        "Bản Nháp" -> Color(0xFFBDBDBD)
        else -> Color.Gray
    }
}


//@Composable
//fun KtvLamViecViewCard(
//    statusCounts: Map<String, Int>,
//    modifier: Modifier = Modifier,
//    onClickDetail: () -> Unit
//) {
//    Card(
//        modifier = modifier,
//        shape = RoundedCornerShape(12.dp),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
//        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primaryContainer),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//    ) {
//        Column(modifier = Modifier.fillMaxWidth()) {
//            // Phần trên
//            Column(
//                Modifier
//                    .fillMaxWidth()
//                    .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 32.dp),
//                verticalArrangement = Arrangement.spacedBy(12.dp)
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column(
//                        verticalArrangement = Arrangement.spacedBy(8.dp),
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        val statusList = statusCounts.entries.toList()
//                        statusList.forEach { (status, count) ->
//                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Box(
//                                    Modifier
//                                        .size(10.dp)
//                                        .background(getColorForTrangThaiPhanCong(status), shape = RoundedCornerShape(50))
//                                )
//                                Spacer(Modifier.width(4.dp))
//                                Text("$status: $count", style = MaterialTheme.typography.bodyMedium)
//                            }
//                        }
//                    }
//
//                    Box(
//                        contentAlignment = Alignment.Center,
//                        modifier = Modifier.size(140.dp)
//                    ) {
//                        DonutChartForPhanCong(
//                            data = statusCounts,
//                            size = 120.dp,
//                            thickness = 20.dp
//                        )
//                        Text(
//                            text = "${statusCounts.values.sum()}",
//                            style = MaterialTheme.typography.titleLarge.copy(
//                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
//                            ),
//                            color = MaterialTheme.colorScheme.onSurface
//                        )
//                    }
//                }
//            }
//
//            // Phần dưới
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(MaterialTheme.colorScheme.primaryContainer)
//                    .height(60.dp),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxSize(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Text(
//                        text = "Tình hình công việc",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = MaterialTheme.colorScheme.onPrimaryContainer,
//                        modifier = Modifier.padding(start = 16.dp)
//                    )
//                    Button(
//                        onClick = onClickDetail,
//                        modifier = Modifier
//                            .offset(y = (-24).dp)
//                            .padding(end = 16.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        border = BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//                            contentColor = MaterialTheme.colorScheme.primary
//                        )
//                    ) {
//                        Text("Xem chi tiết")
//                    }
//                }
//            }
//        }
//    }
//}
//@Composable
//fun DonutChartForPhanCong(data: Map<String, Int>, size: Dp, thickness: Dp) {
//    val total = data.values.sum().toFloat()
//    val gapDegree = 2f
//
//    Canvas(
//        modifier = Modifier.size(size)
//    ) {
//        var startAngle = -90f
//
//        data.forEach { (status, count) ->
//            val sweepAngle = (count / total) * 360f - gapDegree
//
//            drawArc(
//                color = Color.Black.copy(alpha = 0.08f),
//                startAngle = startAngle + gapDegree / 2,
//                sweepAngle = sweepAngle,
//                useCenter = false,
//                style = Stroke(width = thickness.toPx() + 4f)
//            )
//
//            drawArc(
//                color = getColorForTrangThaiPhanCong(status),
//                startAngle = startAngle + gapDegree / 2,
//                sweepAngle = sweepAngle,
//                useCenter = false,
//                style = Stroke(width = thickness.toPx())
//            )
//
//            startAngle += (sweepAngle + gapDegree)
//        }
//    }
//}
@Composable
fun KtvLamViecViewCard(
    statusCounts: Map<String, Int>,
    modifier: Modifier = Modifier,
    onClickDetail: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tình hình công việc",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Body
            Spacer(Modifier.height(12.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Donut chart ở trên
                Box(contentAlignment = Alignment.Center) {
                    DonutChartForPhanCong(
                        data = statusCounts,
                        size = 160.dp,
                        thickness = 28.dp
                    )
                    Text(
                        text = "${statusCounts.values.sum()}",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.height(8.dp))
                // Danh sách trạng thái bên dưới
                Column(
                    modifier = Modifier.fillMaxWidth(), // 👈 ép full chiều ngang luôn
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val statusList = statusCounts.entries.toList()
                    statusList.forEach { (status, count) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(), // 👈 từng dòng cũng kéo full width
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween // 👈 label bên trái, số bên phải
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    Modifier
                                        .size(16.dp)
                                        .background(getColorForTrangThaiPhanCong(status), shape = RoundedCornerShape(50))
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = status,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Đường gạch nối tự động expand
                            Text(
                                text = "........................................",
                                modifier = Modifier.weight(1f), // 👈 fill ra hết chỗ trống giữa
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "$count",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }
                }

            }
            Spacer(Modifier.height(12.dp))

            // Footer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onClickDetail,
                    modifier = Modifier
                        .offset(y = (-12).dp)
                        .width(240.dp)    // 👈 Tùy chỉnh rộng
                        .height(64.dp),   // 👈 Tùy chỉnh cao
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(4.dp, MaterialTheme.colorScheme.primaryContainer),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Xem chi tiết",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize, // 👈 To hơn bodyLarge
                            fontWeight = FontWeight.Bold // 👈 In đậm
                        )
                    )
                }

            }

        }
    }
}

@Composable
fun DonutChartForPhanCong(data: Map<String, Int>, size: Dp, thickness: Dp) {
    val total = data.values.sum().toFloat()
    val gapDegree = 2f

    Canvas(
        modifier = Modifier.size(size)
    ) {
        var startAngle = -90f

        data.forEach { (status, count) ->
            val sweepAngle = (count / total) * 360f - gapDegree

            drawArc(
                color = Color.Black.copy(alpha = 0.08f),
                startAngle = startAngle + gapDegree / 2,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = thickness.toPx() + 4f)
            )

            drawArc(
                color = getColorForTrangThaiPhanCong(status),
                startAngle = startAngle + gapDegree / 2,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = thickness.toPx())
            )

            startAngle += (sweepAngle + gapDegree)
        }
    }
}
