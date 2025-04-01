package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVCreateYeuCauViewModel
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemYeuCauMoiScreen(
    navController: NavController,
    yeuCauId: Int?,
    viewModel: QLDVCreateYeuCauViewModel = hiltViewModel()
) {
    BackHandler { navController.popBackStack() }
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()

    val chiTietList by viewModel.chiTietYeuCauList.collectAsState()
    val yeuCau by viewModel.yeuCau.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    val taiKhoanId = currentUser?.id ?: 0
    val donViId = currentUser?.donViId ?: 0
    var moTa by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(yeuCauId == null) }
    var trangThai by remember { mutableStateOf("") }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val returnedYeuCauIdState = savedStateHandle?.getLiveData<Int>("yeuCauId")?.observeAsState()
    val returnedYeuCauId = returnedYeuCauIdState?.value

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(yeuCauId, returnedYeuCauId) {
        val id = returnedYeuCauId ?: yeuCauId
        if (id != null) {
            viewModel.setYeuCauId(id)
            viewModel.loadChiTietYeuCau(id)

            viewModel.getYeuCauById(id) { yc ->
                moTa = yc.moTa
                trangThai = yc.trangThai
                showDialog = false
            }
        }
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbar()
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(onClick = {
                    viewModel.createYeuCau(taiKhoanId, donViId, moTa)
                    showDialog = false
                    trangThai = TrangThaiYeuCau.NHAP
                }, enabled = (moTa != "")) {
                    Text("Tạo yêu cầu")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog = false
                    navController.popBackStack()
                }) {
                    Text("Hủy")
                }
            },
            text = {
                Column {
                    Text("Nhập mô tả cho yêu cầu")
                    OutlinedTextField(
                        value = moTa,
                        onValueChange = { moTa = it },
                        label = { Text("Mô tả yêu cầu") }
                    )
                }
            }
        )
    }

    ScaffoldLayout(
        title = moTa.ifBlank { "Chỉnh sửa yêu cầu" },
        navController = navController,
        showBottomBar = false,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { modifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .then(modifier)
        ) {
            Text("Chi tiết yêu cầu", style = MaterialTheme.typography.headlineMedium)

            Text(
                text = moTa.ifBlank { "Không có mô tả" },
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            if (yeuCau?.trangThai == TrangThaiYeuCau.TU_CHOI) {
                var showReasonSheet by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Yêu cầu này đã bị từ chối.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(4.dp))
                        IconButton(onClick = { showReasonSheet = true }) {
                            Icon(Icons.Default.Info, contentDescription = "Lý do từ chối")
                        }
                    }

                    Box {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Tuỳ chọn")
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            DropdownMenuItem(
                                text = { Text("Hủy bỏ yêu cầu") },
                                onClick = {
                                    expanded = false
                                    viewModel.capNhatTrangThaiYeuCau(TrangThaiYeuCau.DA_HUY)
                                    yeuCauId?.let {
                                        viewModel.getYeuCauById(it) { yc ->
                                            moTa = yc.moTa
                                            trangThai = yc.trangThai
                                        }
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Điều chỉnh lại") },
                                onClick = {
                                    expanded = false
                                    viewModel.capNhatTrangThaiYeuCau(TrangThaiYeuCau.NHAP)
                                    yeuCauId?.let {
                                        viewModel.getYeuCauById(it) { yc ->
                                            moTa = yc.moTa
                                            trangThai = yc.trangThai
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                if (showReasonSheet) {
                    ModalBottomSheet(onDismissRequest = { showReasonSheet = false }) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Lý do từ chối", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))
                            Text(yeuCau?.lyDoTuChoi ?: "Không có lý do được cung cấp.")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (trangThai == TrangThaiYeuCau.NHAP) {
                Button(onClick = {
                    viewModel.yeuCauId.value?.let {
                        showDialog = false
                        navController.navigate("chon_thiet_bi/$it")
                    }
                }) {
                    Text("Thêm thiết bị")
                }
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        viewModel.yeuCauId.value?.let {
                            viewModel.updateYeuCauStatus(it, TrangThaiYeuCau.CHO_XAC_NHAN)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = chiTietList.isNotEmpty()
                ) {
                    Text("Gửi yêu cầu")
                }
            } else {
                Text(
                    text = "Hiện không thể chỉnh sửa",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(chiTietList) { chiTiet ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Thiết bị: ${chiTiet.thietBiId}")
                            Text("Loại yêu cầu: ${chiTiet.loaiYeuCau}")
                            Text("Mô tả: ${chiTiet.moTa}")
                            if (trangThai == TrangThaiYeuCau.NHAP) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    IconButton(onClick = {
                                        chiTiet.thietBiId?.let { thietBiId ->
                                            navController.navigate(Screen.ThietBiDetail.createRoute(thietBiId, isEditMode = true, yeuCauId = yeuCauId))
                                        }
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                                    }
                                    IconButton(onClick = {
                                        viewModel.removeChiTietYeuCau(chiTiet.id)
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    }
                                }
                            } else {
                                IconButton(onClick = {
                                    chiTiet.thietBiId?.let { thietBiId ->
                                        navController.navigate(Screen.ThietBiDetail.createRoute(thietBiId, isEditMode = true, yeuCauId = yeuCauId))
                                    }
                                }) {
                                    Icon(Icons.Default.Search, contentDescription = "Review")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


