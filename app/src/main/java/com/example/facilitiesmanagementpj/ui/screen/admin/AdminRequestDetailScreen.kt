package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.animation.Animatable
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import kotlinx.coroutines.delay

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
    val yeuCau = viewModel.yeuCau.collectAsState().value
    var showRejectDialog by remember { mutableStateOf(false) }
    var rejectReason by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()


    LaunchedEffect(Unit) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(innerPadding)
        ) {
            if (yeuCau?.trangThai != TrangThaiYeuCau.CHO_XAC_NHAN) {
                TabRow(selectedTabIndex = tabIndex.intValue) {
                    Tab(
                        selected = tabIndex.intValue == 0,
                        onClick = { tabIndex.intValue = 0 },
                        text = {
                            Row{Text("Chưa phân công")
                                Badge(
                                    modifier = Modifier.padding(start = 8.dp),
                                    content = { Text(chuaPhanCong.size.toString()) }
                                )}

                        })
                    Tab(
                        selected = tabIndex.intValue == 1,
                        onClick = { tabIndex.intValue = 1 },
                        text = {
                            Row { Text("Đã phân công")
                                Badge(
                                    modifier = Modifier.padding(start = 8.dp),
                                    content = { Text(daPhanCong.size.toString()) }
                                ) }

                        })

                }
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

            val currentList = if (tabIndex.intValue == 0) chuaPhanCong else daPhanCong

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(currentList) { item ->
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        thickness = 0.8.dp,
                        color = Color.LightGray.copy(alpha = 0.5f)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                if (tabIndex.intValue == 1) {
                                    // Tab "Đã phân công" → Điều hướng đến chi tiết phân công
                                    item.phanCongId?.let {
                                        navController.navigate(Screen.PhanCongDetail.createRoute(it))
                                    }
                                } else {
                                    navController.navigate(
                                    Screen.AdminDeviceDetail.createRoute(
                                        item.chiTiet.thietBiId!!,
                                        item.chiTiet.yeuCauId
                                    )
                                )}

                            },
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
                                    .background(
                                        Color.Black.copy(alpha = 0.6f),
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "${item.soAnh}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.AddCircle,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "${item.soVideo}",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Thiết bị: ${item.chiTiet.tenThietBi}",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text("Loại thiết bị: ${item.chiTiet.tenLoaiThietBi}")
                            Text("Loại yêu cầu: ${item.chiTiet.loaiYeuCau}")

                            if (tabIndex.intValue == 1) {
                                // Hiển thị số lượng kỹ thuật viên đã chấp nhận và chờ phản hồi
                                Text("${item.totalDoingTechnicians}/${item.totalResponsibleTechnicians} KTV đang thực hiện")
                            }

                        }
                    }
                }
            }

            if (yeuCau?.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
                BottomAppBar(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(54.dp),
                    tonalElevation = 8.dp,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val buttonModifier = Modifier
                            .weight(1f)
                            .height(40.dp)

                        OutlinedButton(
                            onClick = { showRejectDialog = true },
                            modifier = buttonModifier,
                            shape = RectangleShape
                        ) {
                            Text("Từ Chối")
                            Icon(Icons.Default.Delete, contentDescription = "Action Icon")
                        }

                        AnimatedOutlinedButton(viewModel, buttonModifier)
//                        OutlinedButton(
//                            onClick = { viewModel.duyetYeuCau() },
//                            modifier = buttonModifier,
//                            shape = RectangleShape
//                        ) {
//                            Text("Xác Nhận")
//                        }


                    }
                }
            }

            if (filterSheetVisible) {
                ModalBottomSheet(
                    onDismissRequest = { filterSheetVisible = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Chọn ${if (filterType == "device") "loại thiết bị" else "loại yêu cầu"}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val items =
                            if (filterType == "device") deviceTypes.map { it.tenLoai } else viewModel.requestTypes

                        items.forEach { label ->
                            Text(
                                text = label,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (filterType == "device") viewModel.setDeviceTypeFilter(
                                            label
                                        )
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
    }
}

@Composable
fun AnimatedOutlinedButton(viewModel: AdminRequestDetailViewModel, modifier: Modifier = Modifier) {
    // Animatable color state
    val colorAnim = remember { Animatable(Green) }
    var isIconVisible by remember { mutableStateOf(false) }


    // The animation that changes color every 3 seconds
    LaunchedEffect(true) {
        while (true) {
            colorAnim.animateTo(
                targetValue = if (colorAnim.value == Color(0xFF03d500)) Color(0xFF08b405) else Color(
                    0xFF028900
                ),
                animationSpec = repeatable(
                    iterations = Int.MAX_VALUE,
                    animation = tween(1500)
                )
            )
            delay(3000) // Every 3 seconds, switch colors
        }
    }

    // Show the icon every 3 seconds
    LaunchedEffect(true) {
        while (true) {
            delay(3000) // Wait for 3 seconds
            // Switch to show/hide the icon
            isIconVisible = !isIconVisible
        }
    }

    OutlinedButton(
        onClick = { viewModel.duyetYeuCau() },
        modifier = modifier,
        shape = RectangleShape,
        colors = ButtonDefaults.outlinedButtonColors(containerColor = colorAnim.value)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Xác Nhận", modifier = Modifier.weight(1f))

            Icon(Icons.Default.CheckCircle, contentDescription = "Action Icon")

        }
    }
}