package com.example.facilitiesmanagementpj.ui.screen.admin

import android.R.attr.onClick
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestListViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestListScreen(navController: NavController) {
    val viewModel: AdminRequestListViewModel = hiltViewModel()
    val requestList by viewModel.yeuCauWithCount.collectAsState()
    val selectedTrangThai by viewModel.selectedTrangThai.collectAsState()
    val selectedDonVi by viewModel.selectedDonVi.collectAsState()
    val donViList by viewModel.donViList.collectAsState()
    var currentFilterSheet by remember { mutableStateOf(FilterSheetType.NONE) }

    val selectedTrangThaiLabel = selectedTrangThai ?: "Tất cả"
    val selectedDonViLabel = selectedDonVi?.let { id ->
        donViList.find { it.id == id }?.tenDonVi
    }
    val isRefreshing = remember { mutableStateOf(false) }



    ScaffoldLayout(
        title = "Danh sách yêu cầu",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .then(innerPadding)

        ) {
            AdminRequestFilterBar(
                selectedTrangThai = if (selectedTrangThai != null) selectedTrangThaiLabel else null,
                selectedDonVi = selectedDonViLabel,
                onChipClicked = { sheetType -> currentFilterSheet = sheetType },
                onReset = {
                    viewModel.setTrangThaiFilter(null)
                    viewModel.setDonViFilter(null)
                }
            )
            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
                onRefresh = {
                    isRefreshing.value = true
                    viewModel.reloadYeuCauList() // Gọi reload lại danh sách yêu cầu
                    isRefreshing.value = false
                }
            ){
                LazyColumn(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(requestList) { request ->
                        RequestCard(
                            moTa = request.yeuCau.moTa,
                            donVi = request.tenDonVi,
                            ngayYeuCau = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(request.yeuCau.ngayYeuCau)),
                            trangThai = request.yeuCau.trangThai,
                            soDaPhanCong = request.daPhanCong,
                            tongSoPhanCong = request.tongChiTiet,
                            onClick = {
                                navController.navigate(
                                    Screen.AdminRequestDetail.createRoute(request.yeuCau.id)
                                )
                            }
                        )
                    }

                }
            }


        }

        if (currentFilterSheet != FilterSheetType.NONE) {
            ModalBottomSheet(
                onDismissRequest = { currentFilterSheet = FilterSheetType.NONE },
            ) {
                val trangThaiHienThi = TrangThaiYeuCau.ALL.filter { it != TrangThaiYeuCau.NHAP }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    when (currentFilterSheet) {
                        FilterSheetType.TRANG_THAI -> {
                            Text("Chọn trạng thái", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            trangThaiHienThi.forEach {
                                FilterOptionTrangThai(it) {
                                    viewModel.setTrangThaiFilter(it)
                                    currentFilterSheet = FilterSheetType.NONE
                                }

                            }
                        }

                        FilterSheetType.DON_VI -> {
                            Text("Chọn đơn vị", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            donViList.forEach {
                                FilterOption(it.tenDonVi) {
                                    viewModel.setDonViFilter(it.id)
                                    currentFilterSheet = FilterSheetType.NONE
                                }
                            }
                        }

                        else -> {}
                    }
                }

            }
        }


    }
}

fun getTrangThaiColor(trangThai: String): Color {
    return when (trangThai) {
        TrangThaiYeuCau.CHO_XAC_NHAN -> Color(0xFFFFC107) // Xanh dương nhạt
        TrangThaiYeuCau.DA_XAC_NHAN -> Color(0xEB1976D2) // Xanh dương đậm
        TrangThaiYeuCau.DANG_XU_LY -> Color(0xF0FF9800) // Cam
        TrangThaiYeuCau.DA_XU_LY -> Color(0xFF4CAF50)   // Xanh lá
        TrangThaiYeuCau.TU_CHOI -> Color(0xE1F44336)    // Đỏ
        TrangThaiYeuCau.DA_HUY -> Color(0xFF9E9E9E)      // Xám
        TrangThaiYeuCau.DA_NGHIEM_THU -> Color(0xFF009688) // Teal
        TrangThaiYeuCau.NHAP -> Color(0xFFBDBDBD)        // Nháp
        else -> Color.Gray
    }
}

@Composable
fun FilterOptionTrangThai(
    trangThai: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 🔵 Chấm màu trạng thái
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(getTrangThaiColor(trangThai))
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = trangThai,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Composable
fun RequestCard(
    moTa: String,
    donVi: String,
    ngayYeuCau: String,
    trangThai: String,
    soDaPhanCong: Int,
    tongSoPhanCong: Int,
    onClick: () -> Unit
) {
    val statusColor = getTrangThaiColor(trangThai)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline, // Viền dịu mắt theo theme
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer // Nền chính của card
        )
    ) {
        Column {

            // 🔷 Header bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer) // header dịu hơn
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        DotLevelBar(
                            filled = soDaPhanCong,
                            total = tongSoPhanCong,
                            filledColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            emptyColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
                        )
                        Spacer(Modifier.width(6.dp))
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = statusColor,
                        contentColor = Color.White // Đảm bảo text trắng rõ
                    ) {
                        Text(
                            text = trangThai,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 🔽 Nội dung chính
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(16.dp)
            ) {
                InfoRow(
                    icon = Icons.Default.LocationCity,
                    text = donVi,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Spacer(Modifier.height(8.dp))

                InfoRow(
                    icon = Icons.Default.CalendarToday,
                    text = "Ngày yêu cầu: $ngayYeuCau",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(Modifier.height(8.dp))

                InfoRow(
                    icon = Icons.Default.Description,
                    text = moTa,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}



// Composable tái sử dụng cho từng dòng
@Composable
fun InfoRow(icon: ImageVector, text: String, style: TextStyle, textColor: Color = Color.Unspecified) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = style,
            color = textColor,
            maxLines = 1
        )
    }
}




@Composable
fun DotLevelBar(
    filled: Int,
    total: Int = 5,
    modifier: Modifier = Modifier,
    dotSize: Dp = 10.dp,
    spacing: Dp = 4.dp,
    filledColor: Color = Color.White,
    emptyColor: Color = Color.White.copy(alpha = 0.4f),
    textColor: Color = Color.White
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Dãy chấm tròn
        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            repeat(filled.coerceAtMost(total)) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(filledColor)
                )
            }
            repeat((total - filled).coerceAtLeast(0)) {
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(emptyColor)
                )
            }
        }

        // 👥 Icon + x/y text
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PersonSearch,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "$filled/$total",
                style = MaterialTheme.typography.labelSmall,
                color = textColor
            )
        }
    }
}





enum class FilterSheetType {
    TRANG_THAI, DON_VI, NONE
}

@Composable
fun FilterOption(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    )
}


@Composable
fun AdminRequestFilterBar(
    selectedTrangThai: String?,
    selectedDonVi: String?,
    onChipClicked: (FilterSheetType) -> Unit,
    onReset: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 🔹 Filter Trạng thái
            FilterChip(
                selected = selectedTrangThai != null,
                onClick = { onChipClicked(FilterSheetType.TRANG_THAI) },
                label = {
                    Text(
                        selectedTrangThai ?: "Trạng thái",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            // 🔹 Filter Đơn vị
            FilterChip(
                selected = selectedDonVi != null,
                onClick = { onChipClicked(FilterSheetType.DON_VI) },
                label = {
                    Text(
                        selectedDonVi ?: "Đơn vị",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                modifier = Modifier.weight(1f),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            // 🔹 Nút Xoá lọc
            FilterChip(
                selected = false,
                onClick = onReset,
                label = {
                    Text(
                        "Xoá lọc",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = MaterialTheme.colorScheme.primary
                ),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                    labelColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}





