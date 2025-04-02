package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.component.getTrangThaiColor
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.KtvDanhSachCongViecViewModel

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
        viewModel.loadTasks(tkKtvId)
    }

    val allTasks by viewModel.allTasks.collectAsState()
    val viecMoi = allTasks.filter {
        it.phanCongKtv.trangThai == TrangThaiPhanCong.CHO_PHAN_HOI ||
                it.phanCongKtv.trangThai == TrangThaiPhanCong.DA_TU_CHOI
    }
    val dangLam = allTasks.filter {
        it.phanCongKtv.trangThai == TrangThaiPhanCong.DA_CHAP_NHAN ||
                it.phanCongKtv.trangThai == TrangThaiPhanCong.DANG_THUC_HIEN ||
                it.phanCongKtv.trangThai == TrangThaiPhanCong.TAM_NGHI
    }
    val hoanThanh = allTasks.filter { it.phanCongKtv.trangThai == TrangThaiPhanCong.HOAN_THANH }
    val biHuy = allTasks.filter { it.phanCongKtv.trangThai == TrangThaiPhanCong.BI_HUY }

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
            "Ưu tiên" -> compareByDescending { it.phanCong.phanCong.mucDoUuTien }
            "Thời gian dự kiến" -> compareBy { it.phanCongKtv.thoiGianDuKien }
            "Ngày tạo" -> compareByDescending { it.phanCong.phanCong.thoiGianTaoPhanCong }
            else -> compareBy { it.phanCongKtv.id }
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sắp xếp theo:")
                    Spacer(Modifier.width(8.dp))
                    DropdownMenuBox(
                        options = listOf("Ưu tiên", "Thời gian dự kiến", "Ngày tạo"),
                        selectedOption = sortOption,
                        onOptionSelected = { sortOption = it }
                    )
                }
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(tasks) { item ->
                        var expanded by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Loại phân công: ${item.phanCong.phanCong.loaiPhanCong}")
                                Text("Thiết bị: ${item.phanCong.thietBi.thietBi.tenThietBi}")
                                Text("Loại thiết bị: ${item.phanCong.thietBi.loaiThietBi?.tenLoai}")
                                Text("Ưu tiên: ${item.phanCong.phanCong.mucDoUuTien}")
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.phanCongKtv.trangThai,
                                        color = getTrangThaiColor(item.phanCongKtv.trangThai),
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                    if (selectedTabIndex == 1) {
                                        Box {
                                            IconButton(onClick = { expanded = true }) {
                                                Icon(
                                                    imageVector = Icons.Default.MoreVert,
                                                    contentDescription = "Tùy chọn"
                                                )
                                            }
                                            DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { expanded = false }
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text("Xem chi tiết") },
                                                    onClick = {
                                                        navController.navigate(
                                                            Screen.KtvXemChiTietPhanCong.createRoute(
                                                                item.phanCong.phanCong.id
                                                            )
                                                        )
                                                        expanded = false
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text("Vào phiên làm việc") },
                                                    onClick = {
                                                        navController.navigate(
                                                            Screen.KtvLamViec.createRoute(item.phanCong.phanCong.id)
                                                        )
                                                        expanded = false
                                                    }
                                                )
                                            }
                                        }
                                    }else if(selectedTabIndex == 2){
                                        Button(
                                            onClick = {
                                                navController.navigate(
                                                    Screen.KtvLamViec.createRoute(item.phanCong.phanCong.id)
                                                )
                                            }
                                        ) {
                                            Text("Xem chi tiết")
                                        }
                                    }else if(selectedTabIndex == 3){
                                        Button(
                                            onClick = {
                                                navController.navigate(
                                                    Screen.KtvLamViec.createRoute(item.phanCong.phanCong.id)
                                                )
                                            }
                                        ) {
                                            Text("Xem chi tiết")
                                        }
                                    }
                                    else {
                                        Button(
                                            onClick = {
                                                navController.navigate(
                                                    Screen.KtvXemChiTietPhanCong.createRoute(item.phanCong.phanCong.id)
                                                )
                                            }
                                        ) {
                                            Text("Xem chi tiết")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuBox(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selectedOption)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
