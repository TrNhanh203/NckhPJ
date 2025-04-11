package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.component.getTrangThaiPcKtvColor
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.KtvDanhSachCongViecViewModel
import com.google.gson.internal.bind.util.ISO8601Utils.format
import java.text.DateFormat
import java.time.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KtvDanhSachCongViecScreen(
    tkKtvId: Int,
    navController: NavController,
    viewModel: KtvDanhSachCongViecViewModel = hiltViewModel()
) {
    val tabTitles = listOf("Việc mới", "Đang làm", "Đã hoàn thành", "Thay người", "Bị hủy")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var sortOption by remember { mutableStateOf("Ưu tiên") }

    LaunchedEffect(Unit) {
        viewModel.loadTasksWithTime(tkKtvId)
    }

    val allTasks by viewModel.tasksWithTime.collectAsState()
    val viecMoi = allTasks.filter {
        it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.CHO_PHAN_HOI ||
                it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.DA_TU_CHOI
    }
    val dangLam = allTasks.filter {
        it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.DA_CHAP_NHAN ||
                it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.DANG_THUC_HIEN ||
                it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.TAM_NGHI
    }
    val hoanThanh = allTasks.filter { it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.HOAN_THANH }
    val biHuy = allTasks.filter { it.pc.phanCongKtv.trangThai == TrangThaiPhanCong.BI_HUY }

    val tabData = listOf(
        viecMoi to "Việc mới",
        dangLam to "Đang làm",
        hoanThanh to "Đã hoàn thành",
        biHuy to "Bị hủy"
    )

    val tasks = when (selectedTabIndex) {
        0 -> viecMoi
        1 -> dangLam
        2 -> hoanThanh
        3 -> biHuy
        else -> emptyList()
    }.sortedWith(
        when (sortOption) {
            "Mức Độ" -> compareByDescending { it.pc.phanCong.phanCong.mucDoUuTien }
            "Thời Lượng" -> compareBy { it.pc.phanCongKtv.thoiGianDuKien }
            "Mới Nhất" -> compareByDescending { it.pc.phanCong.phanCong.thoiGianTaoPhanCong }
            else -> compareBy { it.pc.phanCongKtv.id }
        }
    )

    ScaffoldLayout(
        title = "Danh sách công việc",
        navController = navController,
        showBottomBar = true,
        showTopBar = true,
        isHomeScreen = false
    ) { padding ->
        Column(modifier = Modifier.then(padding)) {
            ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
                tabData.forEachIndexed { index, (list, title) ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            if (list.isNotEmpty()) {
                                Row {
                                    Text(title)
                                    Badge(
                                        modifier = Modifier.padding(start = 8.dp),
                                        content = { Text(list.size.toString()) }
                                    )
                                }

                            } else {
                                Text(title)
                            }
                        }
                    )
                }
            }



            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Không có công việc nào", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                Surface(
                    tonalElevation = 2.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val filterOptions = listOf("Mức Độ", "Thời Lượng", "Mới Nhất")
                            filterOptions.forEach { option ->
                                FilterChip(
                                    selected = sortOption == option,
                                    onClick = { sortOption = option },
                                    label = {
                                        Text(
                                            option,
                                            style = MaterialTheme.typography.labelLarge
                                        )
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 4.dp),
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                                        selectedLabelColor = Color.White,
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                        labelColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }

                var selectedPhanCongId by remember { mutableStateOf<Int?>(null) }
                var showSheet by remember { mutableStateOf(false) }

                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(tasks) { item ->
                        val phanCong = item.pc.phanCong.phanCong
                        val thietBi = item.pc.phanCong.thietBi.thietBi
                        val loaiThietBi = item.pc.phanCong.thietBi.loaiThietBi
                        val trangThai = item.pc.phanCongKtv.trangThai

                        val iconPrefix = when (phanCong.loaiPhanCong) {
                            "Sửa Chữa" -> "🛠"
                            "Kiểm Tra" -> "🔍"
                            "Tháo Dỡ" -> "📦"
                            else -> "📌"
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(
                                ) {
                                    selectedPhanCongId = phanCong.id
                                    showSheet = true
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Tiêu đề + trạng thái
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "$iconPrefix ${phanCong.loaiPhanCong}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    AssistChip(
                                        onClick = {},
                                        label = { Text(trangThai) },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = getTrangThaiPcKtvColor(item.pc.phanCongKtv.trangThai),
                                            labelColor = Color.White
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Thiết bị: ${thietBi.tenThietBi}",
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text("Loại thiết bị: ${loaiThietBi?.tenLoai ?: "Không rõ"}")
                                Spacer(modifier = Modifier.height(8.dp))

                                PriorityLevelBar(level = phanCong.mucDoUuTien)

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                                    val ngayTaoFormatted = formatter.format(Date(phanCong.thoiGianTaoPhanCong))
                                    val hours = item.pc.phanCongKtv.thoiGianDuKien / 60
                                    val minutes = item.pc.phanCongKtv.thoiGianDuKien % 60
                                    val thoiGianDuKienFormatted = if (hours > 0)
                                        "${hours}h ${minutes}p"
                                    else
                                        "${minutes}p"


                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            text = "Tạo: $ngayTaoFormatted",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }

                                    val conLai = item.thoiGianConLaiThamKhao
                                    val isTre = conLai != null && conLai <= 0
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Timer,
                                            contentDescription = null,
                                            tint = if (conLai != null && conLai <= 0)
                                                MaterialTheme.colorScheme.error
                                            else
                                                MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Spacer(Modifier.width(4.dp))

                                        val textColor = if (conLai != null && conLai <= 0)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.primary

                                        val timeText = when {
                                            conLai != null && conLai <= 0 -> "Đã trễ"
                                            conLai != null -> "Còn lại: ${conLai}p"
                                            else -> "Hạn: $thoiGianDuKienFormatted"
                                        }

                                        Text(
                                            text = timeText,
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            color = textColor
                                        )
                                    }

                                }


                            }
                        }
                    }
                }
                if (showSheet && selectedPhanCongId != null) {
                    val selectedItem = tasks.find { it.pc.phanCong.phanCong.id == selectedPhanCongId }
                    val trangThai = selectedItem?.pc?.phanCongKtv?.trangThai

                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        dragHandle = null
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            ListItem(
                                headlineContent = { Text("Xem chi tiết") },
                                leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
                                colors = ListItemDefaults.colors(
                                    containerColor = Color.Transparent
                                ),
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        Screen.KtvXemChiTietPhanCong.createRoute(selectedPhanCongId!!)
                                    )
                                    showSheet = false
                                }
                            )

                            if (trangThai != TrangThaiPhanCong.CHO_PHAN_HOI) {
                                ListItem(
                                    headlineContent = { Text("Vào phiên làm việc") },
                                    leadingContent = { Icon(Icons.Default.Work, contentDescription = null) },
                                    colors = ListItemDefaults.colors(
                                        containerColor = Color.Transparent
                                    ),
                                    modifier = Modifier.clickable {
                                        navController.navigate(
                                            Screen.KtvLamViec.createRoute(selectedPhanCongId!!)
                                        )
                                        showSheet = false
                                    }
                                )
                            }


                        }
                    }
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