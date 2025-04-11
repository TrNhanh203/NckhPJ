package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.relation.KyThuatVienWithSoTaskWithTrangThaiPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiKtv
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.ChonKyThuatVienViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChonKyThuatVienScreen(
    phanCongId: Int,
    navController: NavController
) {
    val trangThaiOptions = listOf("Tất cả") + TrangThaiKtv.ALL
    var showFilterSheet by remember { mutableStateOf(false) }
    val viewModel: ChonKyThuatVienViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState()
    var selectedKtvWithTrangThai by remember { mutableStateOf<KyThuatVienWithSoTaskWithTrangThaiPhanCong?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadDanhSachKTV(phanCongId)
    }

    val list by viewModel.danhSachKTV.collectAsState()

    ScaffoldLayout(
        title = "Chọn kỹ thuật viên",
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
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(trangThaiOptions) { tt ->
                            val isSelected = viewModel.selectedTrangThai == tt || (tt == "Tất cả" && viewModel.selectedTrangThai == null)

                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    viewModel.filterByTrangThai(if (tt == "Tất cả") null else tt)
                                },
                                label = {
                                    Text(
                                        text = tt,
                                        color = if (isSelected)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .height(40.dp)
                            .wrapContentWidth()
                            .clickable { showFilterSheet = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            Divider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.searchText,
                onValueChange = {
                    viewModel.searchText = it
                    viewModel.applyFilters()
                },
                label = { Text("Tìm theo tên") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            if (list.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.LightGray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Chưa có kỹ thuật viên phù hợp", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(list) { item ->
                        val stateColor = getColorForTrangThaiPhanCong(item.trangThaiPhanCong)
                        val circleColor = getColorForTrangThaiHoatDong(item.ktv.kyThuatVien.trangThaiHienTai)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (item.trangThaiPhanCong != null || item.soTaskDangLam > 0) {
                                        selectedKtvWithTrangThai = item
                                    } else {
                                        navController.navigate(Screen.XacNhanDeCuKtv.createRoute(phanCongId, item.ktv.taiKhoan.id))
                                    }
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(Modifier.fillMaxWidth().height(4.dp).background(stateColor))
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = item.ktv.taiKhoan.hoTen?.firstOrNull()?.toString() ?: "?",
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(item.ktv.taiKhoan.hoTen ?: "", style = MaterialTheme.typography.titleMedium)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(circleColor)
                                        )
                                        Spacer(Modifier.width(6.dp))
                                        Text(
                                            text = item.ktv.kyThuatVien.trangThaiHienTai,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Text(
                                        text = "Đang có ${item.soTaskDangLam} công việc",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                    item.trangThaiPhanCong?.let {
                                        Text("Phân công: $it", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }


    selectedKtvWithTrangThai?.let { selected ->
        ModalBottomSheet(
            onDismissRequest = { selectedKtvWithTrangThai = null }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Thao tác với KTV: ${selected.ktv.taiKhoan.hoTen}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(12.dp))

                val options = mutableListOf<String>()

                if (selected.trangThaiPhanCong == null) {
                    options.add("Đề cử")
                    options.add("Xem tất cả công việc")
                } else {
                    when (selected.trangThaiPhanCong) {
                        TrangThaiPhanCong.CHO_PHAN_HOI -> options.addAll(listOf("Hủy bỏ", "Xem thông tin cá nhân", "Gọi điện"))
                        TrangThaiPhanCong.DA_CHAP_NHAN -> options.addAll(listOf("Hủy bỏ", "Xem thông tin cá nhân", "Gọi điện"))
                        TrangThaiPhanCong.DA_TU_CHOI -> options.addAll(listOf("Xem lý do từ chối", "Thay người", "Hủy bỏ", "Xem thông tin cá nhân", "Gọi điện"))
                        TrangThaiPhanCong.DANG_THUC_HIEN -> options.addAll(listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện"))
                        TrangThaiPhanCong.TAM_NGHI -> options.addAll(listOf("Xem tiến độ", "Thay người", "Hủy bỏ", "Xem thông tin cá nhân", "Gọi điện"))
                        TrangThaiPhanCong.HOAN_THANH, TrangThaiPhanCong.BI_HUY -> options.addAll(listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện"))
                    }

                    // Thêm lựa chọn xem toàn bộ công việc
                    options.add("Xem tất cả công việc") // tận dụng lại mh ktv xem ds công việc
                }


                options.forEach { option ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ListItem(
                            headlineContent = { Text(option) },
                            modifier = Modifier.clickable {
                                when (option) {
                                    "Đề cử" -> {
                                        navController.navigate(Screen.XacNhanDeCuKtv.createRoute(phanCongId, selected.ktv.taiKhoan.id))
                                    }
                                    "Xem tất cả công việc" -> {
                                        // TODO: chuyển sang màn danh sách công việc của KTV này
                                    }
                                    // Các case còn lại bạn giữ nguyên xử lý như cũ
                                }

                                selectedKtvWithTrangThai = null
                            }
                        )
                    }

                }
            }
        }
    }


    if (showFilterSheet) {
        ModalBottomSheet(onDismissRequest = { showFilterSheet = false }, sheetState = sheetState) {
            Column(Modifier.padding(16.dp)) {
                Text("Lọc theo chuyên môn", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = {
                        viewModel.resetFilters()
                        showFilterSheet = false
                    }) { Text("Xóa lọc") }
                    Button(onClick = {
                        viewModel.applyFilters()
                        showFilterSheet = false
                    }) { Text("Áp dụng") }
                }
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    items(viewModel.allChuyenMon) { cm ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = viewModel.selectedChuyenMonIds.contains(cm.id),
                                onCheckedChange = { viewModel.toggleChuyenMon(cm.id, it) }
                            )
                            Text(cm.tenChuyenMon)
                        }
                    }
                }


            }
        }
    }
}

@Composable
fun getColorForTrangThaiPhanCong(trangThai: String?): Color {
    return when (trangThai) {
        TrangThaiPhanCong.DA_CHAP_NHAN, TrangThaiPhanCong.HOAN_THANH -> Color(0xFF4CAF50)
        TrangThaiPhanCong.CHO_PHAN_HOI -> Color(0xFFFFC107)
        TrangThaiPhanCong.DANG_THUC_HIEN -> Color(0xFF2196F3)
        TrangThaiPhanCong.TAM_NGHI -> Color(0xFFFF9800)
        TrangThaiPhanCong.BI_HUY -> Color(0xFF9E9E9E)
        TrangThaiPhanCong.DA_TU_CHOI -> Color(0xFFF44336)
        else -> Color.LightGray
    }
}

@Composable
fun getColorForTrangThaiHoatDong(trangThai: String): Color {
    return when (trangThai) {
        TrangThaiKtv.DANG_LAM_VIEC -> Color(0xFF4CAF50)
        TrangThaiKtv.DANG_NGHI -> Color(0xFFF44336)
        else -> Color.Gray
    }
}




//package com.example.facilitiesmanagementpj.ui.screen.admin.phieuphancong
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material.icons.filled.PersonAdd
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.facilitiesmanagementpj.data.utils.TrangThaiKtv
//import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
//import com.example.facilitiesmanagementpj.ui.navigation.Screen
//import com.example.facilitiesmanagementpj.ui.viewmodel.ChonKyThuatVienViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ChonKyThuatVienScreen(
//    phanCongId: Int,
//    navController: NavController
//) {
//    val trangThaiOptions = listOf("Tất cả") + TrangThaiKtv.ALL
//    var showFilterSheet by remember { mutableStateOf(false) }
//    val viewModel: ChonKyThuatVienViewModel = hiltViewModel()
//    val sheetState = rememberModalBottomSheetState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadDanhSachKTV()
//    }
//
//    val list by viewModel.danhSachKTV.collectAsState()
//
//    ScaffoldLayout(
//        title = "Chọn kỹ thuật viên",
//        navController = navController,
//        showTopBar = true,
//        showBottomBar = false,
//        showDrawer = false,
//        isHomeScreen = false
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .then(padding)
//        ) {
//            // Bộ lọc trạng thái + mở bottom sheet
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .horizontalScroll(rememberScrollState())
//                    .padding(8.dp)
//            ) {
//                trangThaiOptions.forEach { tt ->
//                    FilterChip(
//                        selected = viewModel.selectedTrangThai == tt || (tt == "Tất cả" && viewModel.selectedTrangThai == null),
//                        onClick = {
//                            viewModel.filterByTrangThai(if (tt == "Tất cả") null else tt)
//                        },
//                        label = { Text(tt) }
//                    )
//                    Spacer(Modifier.width(8.dp))
//                }
//
//                Spacer(Modifier.weight(1f))
//                Button(onClick = { showFilterSheet = true }) {
//                    Icon(Icons.Default.MoreVert, contentDescription = null)
//                }
//            }
//
//            Spacer(Modifier.height(12.dp))
//
//            OutlinedTextField(
//                value = viewModel.searchText,
//                onValueChange = {
//                    viewModel.searchText = it
//                    viewModel.applyFilters()
//                },
//                label = { Text("Tìm theo tên") },
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Spacer(Modifier.height(16.dp))
//
//            if (list.isEmpty()) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Icon(
//                            imageVector = Icons.Default.PersonAdd,
//                            contentDescription = null,
//                            modifier = Modifier.size(64.dp),
//                            tint = Color.LightGray
//                        )
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text("Chưa có kỹ thuật viên phù hợp", color = Color.Gray)
//                    }
//                }
//            } else {
//                LazyColumn {
//                    items(list) { item ->
//                        val ktv = item.ktv
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .clickable {
//                                    navController.navigate(Screen.XacNhanDeCuKtv.createRoute(phanCongId, ktv.taiKhoan.id))
//                                },
//                            shape = RoundedCornerShape(12.dp),
//                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
//                        ) {
//                            Row(
//                                modifier = Modifier.padding(16.dp),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Box(
//                                    modifier = Modifier
//                                        .size(48.dp)
//                                        .clip(CircleShape)
//                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Text(
//                                        text = ktv.taiKhoan.hoTen?.firstOrNull()?.toString() ?: "?",
//                                        color = MaterialTheme.colorScheme.primary,
//                                        style = MaterialTheme.typography.titleMedium
//                                    )
//                                }
//
//                                Spacer(Modifier.width(12.dp))
//
//                                Column(Modifier.weight(1f)) {
//                                    Text(ktv.taiKhoan.hoTen.toString(), style = MaterialTheme.typography.titleMedium)
//                                    Row(verticalAlignment = Alignment.CenterVertically) {
//                                        Box(
//                                            Modifier
//                                                .size(8.dp)
//                                                .clip(CircleShape)
//                                                .background(getColorForTrangThai(ktv.kyThuatVien.trangThaiHienTai))
//                                        )
//                                        Spacer(Modifier.width(6.dp))
//                                        Text(
//                                            text = ktv.kyThuatVien.trangThaiHienTai,
//                                            style = MaterialTheme.typography.bodySmall
//                                        )
//                                    }
//                                    Text(
//                                        text = "${item.soTaskDangLam} công việc đang thực hiện",
//                                        style = MaterialTheme.typography.labelSmall
//                                    )
//                                }
//                            }
//                        }
//                        Spacer(modifier = Modifier.height(12.dp))
//                    }
//                }
//            }
//        }
//    }
//
//    if (showFilterSheet) {
//        ModalBottomSheet(onDismissRequest = { showFilterSheet = false }, sheetState = sheetState) {
//            Column(Modifier.padding(16.dp)) {
//                Text("Lọc theo chuyên môn", style = MaterialTheme.typography.titleMedium)
//                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
//                    TextButton(onClick = {
//                        viewModel.resetFilters()
//                        showFilterSheet = false
//                    }) { Text("Xóa lọc") }
//                    Button(onClick = {
//                        viewModel.applyFilters()
//                        showFilterSheet = false
//                    }) { Text("Áp dụng") }
//                }
//                Spacer(Modifier.height(8.dp))
//                LazyColumn {
//                    items(viewModel.allChuyenMon) { cm ->
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Checkbox(
//                                checked = viewModel.selectedChuyenMonIds.contains(cm.id),
//                                onCheckedChange = { viewModel.toggleChuyenMon(cm.id, it) }
//                            )
//                            Text(cm.tenChuyenMon)
//                        }
//                    }
//                }
//
//
//            }
//        }
//    }
//}
//
//@Composable
//fun getColorForTrangThai(trangThai: String): Color {
//    return when (trangThai) {
//        "Trực Tuyến" -> Color(0xFF4CAF50)
//        "Ngoại Tuyến" -> Color(0xFF9E9E9E)
//        "Bị Khóa" -> Color(0xFFF44336)
//        "Chờ Xác Thực" -> Color(0xFFFFC107)
//        else -> Color.LightGray
//    }
//}
