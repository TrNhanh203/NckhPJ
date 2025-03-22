package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel

@Composable
fun AdminRequestDetailScreen(navController: NavController, yeuCauId: Int) {
    val viewModel: AdminRequestDetailViewModel = hiltViewModel()
    val chiTietList by viewModel.filteredChiTietYeuCauList.collectAsState()
    val deviceTypes by viewModel.deviceTypes.collectAsState()
    val yeuCau by viewModel.yeuCau.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    var showRejectDialog by remember { mutableStateOf(false) }
    var rejectReason by remember { mutableStateOf("") }

    LaunchedEffect(yeuCauId) {
        android.util.Log.d("AdminRequestScreen", "Gọi loadChiTietYeuCau với ID: $yeuCauId")
        viewModel.loadChiTietYeuCau(yeuCauId)
        viewModel.loadDeviceTypes()
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    ScaffoldLayout(
        title = "Chi tiết yêu cầu",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp).then(innerPadding)) {
            Text("Chi tiết yêu cầu", style = MaterialTheme.typography.headlineSmall)

            Text("Trạng thái: ${yeuCau?.trangThai ?: "Đang tải..."}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Filters Section
            DropdownMenuFilter(
                label = "Loại thiết bị",
                items = deviceTypes.map { it.tenLoai },
                selected = viewModel.selectedDeviceType.collectAsState().value,
                onSelectedChange = { viewModel.setDeviceTypeFilter(it) }
            )

            DropdownMenuFilter(
                label = "Loại yêu cầu",
                items = viewModel.requestTypes,
                selected = viewModel.selectedRequestType.collectAsState().value,
                onSelectedChange = { viewModel.setRequestTypeFilter(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (yeuCau?.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { viewModel.duyetYeuCau() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Duyệt yêu cầu")
                    }
                    OutlinedButton(
                        onClick = { showRejectDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Từ chối")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Request Details List
            LazyColumn {
                items(chiTietList) { chiTiet ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navController.navigate(Screen.AdminDeviceDetail.createRoute(chiTiet.thietBiId!!, chiTiet.yeuCauId))
                        }) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ID: ${chiTiet.id}")
                            Text("Yêu cầu ID: ${chiTiet.yeuCauId}")
                            Text("Thiết bị ID: ${chiTiet.thietBiId}")
                            Text("Loại yêu cầu: ${chiTiet.loaiYeuCau}")
                            Text("Mô tả: ${chiTiet.moTa}")
                            Text("Loại thiết bị ID: ${chiTiet.loaiThietBiId}")
                            Text("Tên loại thiết bị: ${chiTiet.tenLoaiThietBi}")
                            Text("Tên thiết bị: ${chiTiet.tenThietBi}")
                        }
                    }
                }
            }
        }
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Lý do từ chối") },
            text = {
                OutlinedTextField(
                    value = rejectReason,
                    onValueChange = { rejectReason = it },
                    label = { Text("Nhập lý do từ chối") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (rejectReason.isNotBlank()) {
                            viewModel.tuChoiYeuCau(rejectReason)
                            showRejectDialog = false
                            rejectReason = ""
                        }
                    }
                ) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialog = false }) {
                    Text("Hủy")
                }
            }
        )
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
//            // Approval Button (chỉ hiển thị khi trạng thái là "Chờ xác nhận")
//            if (yeuCau?.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
//                Button(
//                    onClick = { viewModel.duyetYeuCau() },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Duyệt yêu cầu")
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
//}
//
//
