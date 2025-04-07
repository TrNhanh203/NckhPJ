package com.example.facilitiesmanagementpj.ui.screen.admin

import com.example.facilitiesmanagementpj.R
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel.ChiTietYeuCauWithDisplayData
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
                .background(MaterialTheme.colorScheme.background)
                .then(innerPadding)
        ) {
            if (yeuCau?.trangThai != TrangThaiYeuCau.CHO_XAC_NHAN) {
                val selectedColor = MaterialTheme.colorScheme.primary
                val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant

                TabRow(
                    selectedTabIndex = tabIndex.intValue,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentColor = selectedColor,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[tabIndex.intValue]),
                            color = selectedColor,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = tabIndex.intValue == 0,
                        onClick = { tabIndex.intValue = 0 },
                        selectedContentColor = selectedColor,
                        unselectedContentColor = unselectedColor,
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Chưa phân công")
                                Badge(
                                    modifier = Modifier.padding(start = 8.dp),
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = Color.White
                                ) {
                                    Text(chuaPhanCong.size.toString())
                                }
                            }
                        }
                    )

                    Tab(
                        selected = tabIndex.intValue == 1,
                        onClick = { tabIndex.intValue = 1 },
                        selectedContentColor = selectedColor,
                        unselectedContentColor = unselectedColor,
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Đã phân công")
                                Badge(
                                    modifier = Modifier.padding(start = 8.dp),
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = Color.White
                                ) {
                                    Text(daPhanCong.size.toString())
                                }
                            }
                        }
                    )
                }

            }

            // Filter chips below tab
            FilterBarYeuCau(
                selectedDeviceType = selectedDeviceType,
                selectedRequestType = selectedRequestType,
                filterType = filterType,
                filterSheetVisible = filterSheetVisible,
                viewModel = viewModel,
                setFilterType = { filterType = it },
                setFilterSheetVisible = { filterSheetVisible = it }
            )

            val currentList = if (tabIndex.intValue == 0) chuaPhanCong else daPhanCong

            LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(8.dp)) {
                items(currentList) { item ->
                    RequestDeviceItem(item, onClick = {
                        if (tabIndex.intValue == 1) {
                            item.phanCongId?.let {
                                navController.navigate(Screen.PhanCongDetail.createRoute(it))
                            }
                        } else {
                            navController.navigate(
                                Screen.AdminDeviceDetail.createRoute(
                                    item.chiTiet.thietBiId!!,
                                    item.chiTiet.yeuCauId
                                )
                            )
                        }
                    })
                }
            }

            if (yeuCau?.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
                BottomAppBar(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .height(64.dp), // tăng chiều cao nhẹ để dễ nhấn
                    tonalElevation = 8.dp,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Surface(
                        tonalElevation = 4.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f), // nền nhẹ từ màu chủ đạo
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val buttonModifier = Modifier
                                .weight(1f)
                                .height(48.dp)

                            // ❌ Nút từ chối
                            OutlinedButton(
                                onClick = { showRejectDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .shadow(2.dp, RoundedCornerShape(10.dp)), // chiều sâu nhẹ
                                shape = RoundedCornerShape(10.dp), // bo góc mềm
                                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.onPrimary),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFC41D13),
                                    contentColor = Color(0xFFFFFFFF)
                                ),
                                elevation = ButtonDefaults.buttonElevation(2.dp) // không shadow
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Từ Chối", color = Color(0xFFF8F8F8))
                                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color(
                                        0xFFFFFFFF
                                    )
                                    )
                                }
                            }

                            // ✅ Nút xác nhận: AnimatedOutlinedButton được cập nhật như sau
                            AnimatedOutlinedButton(viewModel, modifier = buttonModifier)
                        }
                    }
                }

            }

            if (filterSheetVisible) {
                ModalBottomSheet(
                    onDismissRequest = { filterSheetVisible = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val items =
                        if (filterType == "device") deviceTypes.map { it.tenLoai } else viewModel.requestTypes

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Text(
                            "Chọn ${if (filterType == "device") "loại thiết bị" else "loại yêu cầu"}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        items.forEach { label ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (filterType == "device") viewModel.setDeviceTypeFilter(
                                            label
                                        )
                                        else viewModel.setRequestTypeFilter(label)
                                        filterSheetVisible = false
                                    }
                                    .padding(vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // 🔹 Hiển thị icon nếu là loại thiết bị
                                if (filterType == "device") {
                                    Icon(
                                        painter = painterResource(id = getIconResForDevice(label)),
                                        contentDescription = null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color.Unspecified
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                }

                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
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
fun RequestDeviceItem(
    item: ChiTietYeuCauWithDisplayData,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(12.dp)

    Surface(
        shape = shape,
        tonalElevation = 2.dp,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primaryContainer),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Column {
            // 🟦 Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.chiTiet.loaiYeuCau,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            "${item.totalDoingTechnicians}/${item.totalResponsibleTechnicians}",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

            // 🔽 Body content
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 📷 Hình ảnh
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(shape)
                        .border(1.dp, MaterialTheme.colorScheme.outline, shape)
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
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(getIconResForDevice(item.chiTiet.tenLoaiThietBi.toString())),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp).alpha(0.3f),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // 🖼️ Số ảnh & video
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                            .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Text("${item.soAnh}", color = Color.White, style = MaterialTheme.typography.labelSmall)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Text("${item.soVideo}", color = Color.White, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = item.chiTiet.tenThietBi ?: "",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Xem chi tiết",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


fun getIconResForDevice(tenLoai: String): Int {
    val lower = tenLoai.lowercase()

    return when {
        "máy chiếu" in lower -> R.drawable.projector
        "điều hòa" in lower -> R.drawable.airconditioner
        "máy tính" in lower || "pc" in lower -> R.drawable.computer
        "micro" in lower -> R.drawable.microphone
        "loa" in lower -> R.drawable.speaker
        "máy in" in lower -> R.drawable.printer
        "photocopy" in lower -> R.drawable.photocopy
        "tivi" in lower || "tv" in lower -> R.drawable.smarttv
        else -> R.drawable.question // fallback icon chung
    }
}

@Composable
fun FilterBarYeuCau(
    selectedDeviceType: String?,
    selectedRequestType: String?,
    filterType: String,
    filterSheetVisible: Boolean,
    viewModel: AdminRequestDetailViewModel,
    setFilterType: (String) -> Unit,
    setFilterSheetVisible: (Boolean) -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chipShape = RoundedCornerShape(12.dp)

            FilterChip(
                modifier = Modifier.width(120.dp),
                shape = chipShape,
                selected = selectedDeviceType != null,
                onClick = {
                    setFilterType("device")
                    setFilterSheetVisible(true)
                },
                label = {
                    Text(
                        selectedDeviceType ?: "Loại thiết bị",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            FilterChip(
                modifier = Modifier.width(120.dp),
                shape = chipShape,
                selected = selectedRequestType != null,
                onClick = {
                    setFilterType("request")
                    setFilterSheetVisible(true)
                },
                label = {
                    Text(
                        selectedRequestType ?: "Loại yêu cầu",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            FilterChip(
                selected = false,
                shape = chipShape,
                onClick = {
                    viewModel.setDeviceTypeFilter(null)
                    viewModel.setRequestTypeFilter(null)
                },
                label = {
                    Text(
                        "Xoá lọc",
                        style = MaterialTheme.typography.labelLarge
                    )
                },
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = MaterialTheme.colorScheme.outline),
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.primary
                )
            )
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
                targetValue = if (colorAnim.value == Color(0xFF4CAF50)) Color(0xFF66BB6A) else Color(0xFF2E7D32),
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
            delay(1000) // Wait for 3 seconds
            // Switch to show/hide the icon
            isIconVisible = !isIconVisible
        }
    }

    OutlinedButton(
        onClick = { viewModel.duyetYeuCau() },
        modifier = modifier
            .height(48.dp)
            .shadow(2.dp, RoundedCornerShape(10.dp)), // chiều sâu nhẹ
        shape = RoundedCornerShape(10.dp), // bo góc mềm
        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.onPrimary), // viền chủ đạo
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = colorAnim.value,
            contentColor = Color.White // chữ và icon màu trắng cho dễ nhìn
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Xác Nhận", modifier = Modifier.weight(1f))

            AnimatedVisibility(visible = isIconVisible) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Xác nhận")
            }
        }
    }
}