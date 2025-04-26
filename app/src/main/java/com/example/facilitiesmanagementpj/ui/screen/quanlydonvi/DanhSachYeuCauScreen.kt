package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVDanhSachYeuCauViewModel
import com.example.facilitiesmanagementpj.ui.component.DropdownMenuFilter
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.screen.admin.getTrangThaiColor
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch

@Composable
fun DanhSachYeuCauScreen(navController: NavController, viewModel: QLDVDanhSachYeuCauViewModel = hiltViewModel()) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()
    val yeuCauList by viewModel.filteredYeuCauList.collectAsState()
    val donViId = currentUser?.donViId ?: 0
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var selectedYeuCau by remember { mutableStateOf<YeuCau?>(null) }

//    LaunchedEffect(currentUser) {
//        viewModel.loadYeuCauList(donViId)
//    }

    LaunchedEffect(currentUser) {
        currentUser?.donViId?.let { donViId ->
            viewModel.observeYeuCauByDonVi(donViId)
        }
    }


    if (showDialog && selectedYeuCau != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc chắn muốn xóa yêu cầu này không?") },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        viewModel.deleteYeuCau(selectedYeuCau!!.id)
                        showDialog = false
                    }
                }) {
                    Text("Xóa")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout("Danh Sách Yêu Cầu", navController, showTopBar = true,showBottomBar = false,showDrawer = false)
    { modifier ->
        Column(modifier = Modifier.fillMaxSize().then(modifier)) {
            //Text("Danh sách yêu cầu", style = MaterialTheme.typography.headlineMedium)

//            DropdownMenuFilter(
//                label = "Trạng thái",
//                items = TrangThaiYeuCau.ALL,
//                selected = viewModel.selectedTrangThai.collectAsState().value,
//                onSelectedChange = { viewModel.setTrangThaiFilter(it) }
//            )
            TrangThaiFilterSection(viewModel)

            LazyColumn (
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(yeuCauList) { yeuCau ->
//                    Card(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(8.dp)
////                        .clickable {
////                            navController.navigate(Screen.ThemYeuCauMoi.createRoute(yeuCau.id))
////                        }
//                            .pointerInput(Unit) {
//                                detectTapGestures(
//                                    onLongPress = {
//                                        if (yeuCau.trangThai == TrangThaiYeuCau.NHAP) {
//                                            selectedYeuCau = yeuCau
//                                            showDialog = true
//                                        }
//                                    },
//                                    onTap = {
//                                        navController.navigate(Screen.ThemYeuCauMoi.createRoute(yeuCau.id))
//                                    }
//
//                                )
//                            }
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("Mô tả: ${yeuCau.moTa}")
//                            Text("Trạng thái: ${yeuCau.trangThai}")
//                            Text("Ngày yêu cầu: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(yeuCau.ngayYeuCau))}")
//                        }
//                    }

                    val statusColor = getTrangThaiColor(yeuCau.trangThai)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        if (yeuCau.trangThai == TrangThaiYeuCau.NHAP) {
                                            selectedYeuCau = yeuCau
                                            showDialog = true
                                        }
                                    },
                                    onTap = {
                                        navController.navigate(Screen.ThemYeuCauMoi.createRoute(yeuCau.id))
                                    }

                                )
                            }
                            .border(
                                width = 0.8.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            // 🔹 Thanh màu trạng thái bên trái
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .fillMaxHeight()
                                    .background(
                                        color = statusColor,
                                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                                    )
                            )

                            // 🔸 Nội dung chính
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    // 🔹 Mô tả yêu cầu (giới hạn width để không đụng chip)
                                    Text(
                                        text = yeuCau.moTa,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // 🔹 Chip trạng thái
                                    AssistChip(
                                        onClick = {},
                                        label = {
                                            Text(
                                                text = yeuCau.trangThai,
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        },
                                        colors = AssistChipDefaults.assistChipColors(
                                            containerColor = statusColor,
                                            labelColor = Color.White
                                        ),
                                        modifier = Modifier.height(26.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // 🔸 Ngày yêu cầu
                                Surface(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "Ngày yêu cầu: ${
                                            SimpleDateFormat(
                                                "dd/MM/yyyy",
                                                Locale.getDefault()
                                            ).format(Date(yeuCau.ngayYeuCau))
                                        }",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }


                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrangThaiFilterSection(viewModel: QLDVDanhSachYeuCauViewModel) {
    val selectedTrangThai by viewModel.selectedTrangThai.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val trangThaiOptions = listOf("Tất cả") + TrangThaiYeuCau.ALL

    Surface(
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            OutlinedButton(
                onClick = { showSheet = true },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(text = selectedTrangThai ?: "Chọn trạng thái")
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Chọn trạng thái yêu cầu",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                trangThaiOptions.forEach { trangThai ->
                    ListItem(
                        headlineContent = { Text(trangThai) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (trangThai == "Tất cả") {
                                    viewModel.setTrangThaiFilter(null)
                                } else {
                                    viewModel.setTrangThaiFilter(trangThai)
                                }
                                showSheet = false
                            }
                    )
                }
            }
        }
    }
}

