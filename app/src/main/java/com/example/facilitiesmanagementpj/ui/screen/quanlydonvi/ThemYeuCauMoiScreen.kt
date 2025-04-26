package com.example.facilitiesmanagementpj.ui.screen.quanlydonvi

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVCreateYeuCauViewModel
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.screen.admin.getIconResForDevice
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminRequestDetailViewModel.ChiTietYeuCauWithDisplayData
import com.example.facilitiesmanagementpj.ui.viewmodel.QLDVChiTietYeuCauWithDisplayData
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

//    val chiTietList by viewModel.chiTietYeuCauList.collectAsState()
    val chiTietList by viewModel.chiTietList.collectAsState()
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
            viewModel.loadChiTietYeuCauList(id)

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



    ScaffoldLayout(
        title = "Chi tiết yêu cầu",
        navController = navController,
        showBottomBar = false,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { modifier ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .then(modifier)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
//                Text(
//                    text = moTa.ifBlank { "Không có mô tả" },
//                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp),
//                    maxLines = 3,
//                    overflow = TextOverflow.Ellipsis
//                )

                MoTaCard(moTa.ifBlank { "Không có mô tả" })


                if (trangThai == TrangThaiYeuCau.TU_CHOI) {
                    var showReasonSheet by remember { mutableStateOf(false) }
                    var expanded by remember { mutableStateOf(false) }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        tonalElevation = 2.dp,
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
//                                Icon(
//                                    imageVector = Icons.Default.Warning,
//                                    contentDescription = null,
//                                    tint = MaterialTheme.colorScheme.error,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Yêu cầu này đã bị từ chối.",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                IconButton(onClick = { showReasonSheet = true }) {
                                    Icon(Icons.Default.Info, contentDescription = "Xem lý do", tint = MaterialTheme.colorScheme.error)
                                }
                            }

                            Box {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Tuỳ chọn", tint = MaterialTheme.colorScheme.error)
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
                    }

                    // Nếu showReasonSheet == true thì mở BottomSheet hiển thị lý do từ chối
                    if (showReasonSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showReasonSheet = false }
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Lý do từ chối:", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(8.dp))
                                Text("Lý do từ chối", style = MaterialTheme.typography.titleMedium)
                                Spacer(Modifier.height(8.dp))
                                Text(yeuCau?.lyDoTuChoi ?: "Không có lý do được cung cấp.")
                            }
                        }
                    }
                }


                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(chiTietList) { chiTiet ->
                        RequestDeviceItemQLDV(
                            item = chiTiet,
                            trangThai = trangThai,
                            onEdit = {
                                chiTiet.chiTiet.thietBiId?.let { thietBiId ->
                                    navController.navigate(
                                        Screen.ThietBiDetail.createRoute(
                                            thietBiId,
                                            isEditMode = true,
                                            yeuCauId = yeuCauId
                                        )
                                    )
                                }
                            },
                            onDelete = { viewModel.removeChiTietYeuCau(chiTiet.chiTiet.id) },
                            onReview = {
                                chiTiet.chiTiet.thietBiId?.let { thietBiId ->
                                    navController.navigate(
                                        Screen.ThietBiDetail.createRoute(
                                            thietBiId,
                                            isEditMode = true,
                                            yeuCauId = yeuCauId
                                        )
                                    )
                                }
                            }
                        )
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
            }
            if (trangThai == TrangThaiYeuCau.NHAP) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ExtendedFloatingActionButton(
                        text = { Text("Thêm thiết bị") },
                        icon = { Icon(Icons.Default.Add, contentDescription = "Thêm thiết bị") },
                        onClick = {
                            viewModel.yeuCauId.value?.let {
                                showDialog = false
                                navController.navigate("chon_thiet_bi/$it")
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    )

                    ExtendedFloatingActionButton(
                        text = { Text("Gửi yêu cầu") },
                        icon = { Icon(Icons.Default.Send, contentDescription = "Gửi yêu cầu") },
                        onClick = {
                            if (chiTietList.isNotEmpty()) {
                                viewModel.yeuCauId.value?.let {
                                    viewModel.updateYeuCauStatus(it, TrangThaiYeuCau.CHO_XAC_NHAN)
                                    navController.popBackStack()
                                }
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    )

                }
            } else {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Hiện không thể chỉnh sửa",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

        }

//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//                .then(modifier)
//        ) {
//            //Text("Chi tiết yêu cầu", style = MaterialTheme.typography.headlineMedium)
//
//            Text(
//                text = moTa.ifBlank { "Không có mô tả" },
//                style = MaterialTheme.typography.titleLarge,
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//
//            if (yeuCau?.trangThai == TrangThaiYeuCau.TU_CHOI) {
//                var showReasonSheet by remember { mutableStateOf(false) }
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        Text(
//                            "Yêu cầu này đã bị từ chối.",
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.error
//                        )
//                        Spacer(Modifier.width(4.dp))
//                        IconButton(onClick = { showReasonSheet = true }) {
//                            Icon(Icons.Default.Info, contentDescription = "Lý do từ chối")
//                        }
//                    }
//
//                    Box {
//                        IconButton(onClick = { expanded = true }) {
//                            Icon(Icons.Default.MoreVert, contentDescription = "Tuỳ chọn")
//                        }
//                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                            DropdownMenuItem(
//                                text = { Text("Hủy bỏ yêu cầu") },
//                                onClick = {
//                                    expanded = false
//                                    viewModel.capNhatTrangThaiYeuCau(TrangThaiYeuCau.DA_HUY)
//                                    yeuCauId?.let {
//                                        viewModel.getYeuCauById(it) { yc ->
//                                            moTa = yc.moTa
//                                            trangThai = yc.trangThai
//                                        }
//                                    }
//                                }
//                            )
//                            DropdownMenuItem(
//                                text = { Text("Điều chỉnh lại") },
//                                onClick = {
//                                    expanded = false
//                                    viewModel.capNhatTrangThaiYeuCau(TrangThaiYeuCau.NHAP)
//                                    yeuCauId?.let {
//                                        viewModel.getYeuCauById(it) { yc ->
//                                            moTa = yc.moTa
//                                            trangThai = yc.trangThai
//                                        }
//                                    }
//                                }
//                            )
//                        }
//                    }
//                }
//
//                if (showReasonSheet) {
//                    ModalBottomSheet(onDismissRequest = { showReasonSheet = false }) {
//                        Column(Modifier.padding(16.dp)) {
//                            Text("Lý do từ chối", style = MaterialTheme.typography.titleMedium)
//                            Spacer(Modifier.height(8.dp))
//                            Text(yeuCau?.lyDoTuChoi ?: "Không có lý do được cung cấp.")
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//            }
//
//            if (trangThai == TrangThaiYeuCau.NHAP) {
//                Button(onClick = {
//                    viewModel.yeuCauId.value?.let {
//                        showDialog = false
//                        navController.navigate("chon_thiet_bi/$it")
//                    }
//                }) {
//                    Text("Thêm thiết bị")
//                }
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Button(
//                    onClick = {
//                        viewModel.yeuCauId.value?.let {
//                            viewModel.updateYeuCauStatus(it, TrangThaiYeuCau.CHO_XAC_NHAN)
//                            navController.popBackStack()
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(),
//                    enabled = chiTietList.isNotEmpty()
//                ) {
//                    Text("Gửi yêu cầu")
//                }
//            } else {
//                Text(
//                    text = "Hiện không thể chỉnh sửa",
//                    color = Color.Red,
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.padding(vertical = 8.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            LazyColumn {
//                items(chiTietList) { chiTiet ->
//                    RequestDeviceItemQLDV(
//                        item = chiTiet,
//                        trangThai = trangThai,
//                        onEdit = {
//                            chiTiet.chiTiet.thietBiId?.let { thietBiId ->
//                                navController.navigate(
//                                    Screen.ThietBiDetail.createRoute(
//                                        thietBiId,
//                                        isEditMode = true,
//                                        yeuCauId = yeuCauId
//                                    )
//                                )
//                            }
//                        },
//                        onDelete = { viewModel.removeChiTietYeuCau(chiTiet.chiTiet.id) },
//                        onReview = {
//                            chiTiet.chiTiet.thietBiId?.let { thietBiId ->
//                                navController.navigate(
//                                    Screen.ThietBiDetail.createRoute(
//                                        thietBiId,
//                                        isEditMode = true,
//                                        yeuCauId = yeuCauId
//                                    )
//                                )
//                            }
//                        }
//                    )
////                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
////                        Column(modifier = Modifier.padding(16.dp)) {
////                            Text("Thiết bị: ${chiTiet.thietBiId}")
////                            Text("Loại yêu cầu: ${chiTiet.chiTiet.loaiYeuCau}")
////                            Text("Mô tả: ${chiTiet.chiTiet.moTa}")
////                            if (trangThai == TrangThaiYeuCau.NHAP) {
////                                Row(
////                                    modifier = Modifier.fillMaxWidth(),
////                                    horizontalArrangement = Arrangement.End
////                                ) {
////                                    IconButton(onClick = {
////                                        chiTiet.thietBiId?.let { thietBiId ->
////                                            navController.navigate(Screen.ThietBiDetail.createRoute(thietBiId, isEditMode = true, yeuCauId = yeuCauId))
////                                        }
////                                    }) {
////                                        Icon(Icons.Default.Edit, contentDescription = "Edit")
////                                    }
////                                    IconButton(onClick = {
////                                        viewModel.removeChiTietYeuCau(chiTiet.id)
////                                    }) {
////                                        Icon(Icons.Default.Delete, contentDescription = "Delete")
////                                    }
////                                }
////                            } else {
////                                IconButton(onClick = {
////                                    chiTiet.thietBiId?.let { thietBiId ->
////                                        navController.navigate(Screen.ThietBiDetail.createRoute(thietBiId, isEditMode = true, yeuCauId = yeuCauId))
////                                    }
////                                }) {
////                                    Icon(Icons.Default.Search, contentDescription = "Review")
////                                }
////                            }
////                        }
////                    }
//                }
//            }
//        }


    }
}


@Composable
fun RequestDeviceItemQLDV(
    item: QLDVChiTietYeuCauWithDisplayData,
    trangThai:   String,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onReview: () -> Unit
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
                Text(
                    text = item.chiTiet.loaiYeuCau,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            // 🔽 Body
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 📷 Hình ảnh đại diện
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
                                painter = painterResource(getIconResForDevice(item.chiTiet.loaiYeuCau ?: "")),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp).alpha(0.3f),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // 🖼️ Hiển thị số ảnh & video
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

                // 🔸 Nội dung bên phải
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
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if( trangThai == TrangThaiYeuCau.NHAP){
                                IconButton(onClick = { onEdit() }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Cập nhật thiết bị", tint = MaterialTheme.colorScheme.primary)
                                }
                                IconButton(onClick = { onDelete() }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Xóa thiết bị", tint = MaterialTheme.colorScheme.error)
                                }
                            } else {
                                IconButton(onClick = { onReview() }) {
                                    Icon(Icons.Default.Visibility, contentDescription = "Xem thiết bị", tint = MaterialTheme.colorScheme.primary)
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
fun MoTaCard(
    moTa: String?
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isOverflow by remember { mutableStateOf(false) }

    val textToDisplay = moTa?.trim().orEmpty().ifBlank { "Không có mô tả." }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            if (isOverflow) {
                isExpanded = !isExpanded
            }
        }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 64.dp)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!isOverflow) {
                    Text(
                        text = "Mô tả: $textToDisplay",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Mô tả",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = textToDisplay,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // 📏 Ẩn Text để kiểm tra overflow
            Text(
                text = textToDisplay,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Transparent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (!isOverflow) {
                        isOverflow = textLayoutResult.hasVisualOverflow
                    }
                }
            )
        }
    }
}


