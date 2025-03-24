package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminRequestDetailScreen(navController: NavController, yeuCauId: Int) {
    val viewModel: AdminRequestDetailViewModel = hiltViewModel()
    val tabIndex = remember { mutableIntStateOf(0) }
    val daPhanCong by viewModel.filteredDaPhanCongList.collectAsState()
    val chuaPhanCong by viewModel.filteredChuaPhanCongList.collectAsState()
    val selectedDeviceType by viewModel.selectedDeviceType.collectAsState()
    val selectedRequestType by viewModel.selectedRequestType.collectAsState()
    val deviceTypes by viewModel.deviceTypes.collectAsState()
    var filterSheetVisible by remember { mutableStateOf(false) }
    var filterType by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadChiTietYeuCau(yeuCauId)
        viewModel.loadDeviceTypes()
    }

    ScaffoldLayout(
        title = "Chi tiết yêu cầu",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false,
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().then(innerPadding)) {
            TabRow(selectedTabIndex = tabIndex.intValue) {
                Tab(selected = tabIndex.intValue == 0, onClick = { tabIndex.intValue = 0 }, text = { Text("Đã phân công") })
                Tab(selected = tabIndex.intValue == 1, onClick = { tabIndex.intValue = 1 }, text = { Text("Chưa phân công") })
            }

            // Filter chips below tab
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedDeviceType != null,
                    onClick = {
                        filterType = "device"
                        filterSheetVisible = true
                    },
                    label = { Text(selectedDeviceType ?: "Loại thiết bị") }
                )
                FilterChip(
                    selected = selectedRequestType != null,
                    onClick = {
                        filterType = "request"
                        filterSheetVisible = true
                    },
                    label = { Text(selectedRequestType ?: "Loại yêu cầu") }
                )
                FilterChip(
                    selected = false,
                    onClick = {
                        viewModel.setDeviceTypeFilter(null)
                        viewModel.setRequestTypeFilter(null)
                    },
                    label = { Text("Xóa lọc") }
                )
            }

            val currentList = if (tabIndex.intValue == 0) daPhanCong else chuaPhanCong

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(currentList) { item ->
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        thickness = 0.8.dp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val shape = MaterialTheme.shapes.medium

                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .border(1.dp, Color.Gray, shape = shape)
                                .clip(shape) //  đảm bảo ảnh được cắt gọn bên trong viền
                        ) {
                            if (item.anhDaiDien != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(item.anhDaiDien),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.LightGray),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No Image", color = Color.Gray)
                                }
                            }

                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(4.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), shape = MaterialTheme.shapes.small)
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                    Text(text = "${item.soAnh}", color = Color.White, style = MaterialTheme.typography.labelSmall)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                                    Text(text = "${item.soVideo}", color = Color.White, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Thiết bị: ${item.chiTiet.tenThietBi}", fontWeight = FontWeight.SemiBold)
                            Text("Loại thiết bị: ${item.chiTiet.tenLoaiThietBi}")
                            Text("Loại yêu cầu: ${item.chiTiet.loaiYeuCau}")
                        }
                    }
                }
            }

            if (filterSheetVisible) {
                ModalBottomSheet(
                    onDismissRequest = { filterSheetVisible = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Chọn ${if (filterType == "device") "loại thiết bị" else "loại yêu cầu"}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        val items = if (filterType == "device") deviceTypes.map { it.tenLoai } else viewModel.requestTypes

                        items.forEach { label ->
                            Text(
                                text = label,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (filterType == "device") viewModel.setDeviceTypeFilter(label)
                                        else viewModel.setRequestTypeFilter(label)
                                        filterSheetVisible = false
                                    }
                                    .padding(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        TextButton(onClick = {
                            if (filterType == "device") viewModel.setDeviceTypeFilter(null)
                            else viewModel.setRequestTypeFilter(null)
                            filterSheetVisible = false
                        }) {
                            Text("Tất cả")
                        }
                    }
                }
            }
        }
    }
}



//package com.example.facilitiesmanagementpj.ui.screen.admin
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
//import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
//import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
//import com.example.facilitiesmanagementpj.ui.navigation.Screen
//import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel
//
//@Composable
//fun AdminRequestDetailScreen(navController: NavController, yeuCauId: Int) {
//    val viewModel: AdminRequestDetailViewModel = hiltViewModel()
//    val chiTietList by viewModel.filteredChiTietYeuCauList.collectAsState()
//    val deviceTypes by viewModel.deviceTypes.collectAsState()
//    val yeuCau by viewModel.yeuCau.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
//    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
//    var showRejectDialog by remember { mutableStateOf(false) }
//    var rejectReason by remember { mutableStateOf("") }
//
//    LaunchedEffect(yeuCauId) {
//        android.util.Log.d("AdminRequestScreen", "Gọi loadChiTietYeuCau với ID: $yeuCauId")
//        viewModel.loadChiTietYeuCau(yeuCauId)
//        viewModel.loadDeviceTypes()
//    }
//
//    LaunchedEffect(snackbarMessage) {
//        snackbarMessage?.let {
//            snackbarHostState.showSnackbar(it)
//            viewModel.clearSnackbar()
//        }
//    }
//
//    ScaffoldLayout(
//        title = "Chi tiết yêu cầu",
//        navController = navController,
//        showTopBar = true,
//        showBottomBar = false,
//        showDrawer = false,
//        isHomeScreen = false,
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) { innerPadding ->
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(innerPadding)) {
//            Text("Chi tiết yêu cầu", style = MaterialTheme.typography.headlineSmall)
//
//            Text("Trạng thái: ${yeuCau?.trangThai ?: "Đang tải..."}", style = MaterialTheme.typography.bodyLarge)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Filters Section
//            DropdownMenuFilter(
//                label = "Loại thiết bị",
//                items = deviceTypes.map { it.tenLoai },
//                selected = viewModel.selectedDeviceType.collectAsState().value,
//                onSelectedChange = { viewModel.setDeviceTypeFilter(it) }
//            )
//
//            DropdownMenuFilter(
//                label = "Loại yêu cầu",
//                items = viewModel.requestTypes,
//                selected = viewModel.selectedRequestType.collectAsState().value,
//                onSelectedChange = { viewModel.setRequestTypeFilter(it) }
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            if (yeuCau?.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
//                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                    Button(
//                        onClick = { viewModel.duyetYeuCau() },
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text("Duyệt yêu cầu")
//                    }
//                    OutlinedButton(
//                        onClick = { showRejectDialog = true },
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text("Từ chối")
//                    }
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            // Request Details List
//            LazyColumn {
//                items(chiTietList) { chiTiet ->
//                    Card(modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                        .clickable {
//                            navController.navigate(Screen.AdminDeviceDetail.createRoute(chiTiet.thietBiId!!, chiTiet.yeuCauId))
//                        }) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("ID: ${chiTiet.id}")
//                            Text("Yêu cầu ID: ${chiTiet.yeuCauId}")
//                            Text("Thiết bị ID: ${chiTiet.thietBiId}")
//                            Text("Loại yêu cầu: ${chiTiet.loaiYeuCau}")
//                            Text("Mô tả: ${chiTiet.moTa}")
//                            Text("Loại thiết bị ID: ${chiTiet.loaiThietBiId}")
//                            Text("Tên loại thiết bị: ${chiTiet.tenLoaiThietBi}")
//                            Text("Tên thiết bị: ${chiTiet.tenThietBi}")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    if (showRejectDialog) {
//        AlertDialog(
//            onDismissRequest = { showRejectDialog = false },
//            title = { Text("Lý do từ chối") },
//            text = {
//                OutlinedTextField(
//                    value = rejectReason,
//                    onValueChange = { rejectReason = it },
//                    label = { Text("Nhập lý do từ chối") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//            },
//            confirmButton = {
//                TextButton(
//                    onClick = {
//                        if (rejectReason.isNotBlank()) {
//                            viewModel.tuChoiYeuCau(rejectReason)
//                            showRejectDialog = false
//                            rejectReason = ""
//                        }
//                    }
//                ) {
//                    Text("Xác nhận")
//                }
//            },
//            dismissButton = {
//                TextButton(onClick = { showRejectDialog = false }) {
//                    Text("Hủy")
//                }
//            }
//        )
//    }
//}
//
//
//
