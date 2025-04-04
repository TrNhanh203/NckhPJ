package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import android.net.Uri
import android.util.Log
import android.widget.VideoView
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.ui.viewmodel.PhanCongDetailViewModel
import com.example.facilitiesmanagementpj.ui.screen.admin.TabThongTin
import com.example.facilitiesmanagementpj.ui.screen.admin.TabThietBi
import com.example.facilitiesmanagementpj.ui.screen.admin.TabMinhChung
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.KtvLamViecViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import java.io.File
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.navigation.NavBackStackEntry
import com.example.facilitiesmanagementpj.data.utils.LoaiAnhMinhChungLamViec
import com.example.facilitiesmanagementpj.ui.screen.kythuatvien.section.TacVuBottomSheet
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.KtvLamViecViewModel.ThongTinHoanThanh
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.LoaiTacVu
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.TienTrinhLamViecViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import java.text.SimpleDateFormat
import java.util.*





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KtvLamViecScreen(
    navController: NavController,
    phanCongId: Int,
    backStackEntry: NavBackStackEntry
) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()
    val viewModel: PhanCongDetailViewModel = hiltViewModel(backStackEntry)
    val dsKtv by viewModel.dsKtv.collectAsState()
    val currentPhanCongKtvState = remember(dsKtv, currentUser) {
        derivedStateOf {
            dsKtv.firstOrNull {
                it.taiKhoan.id == currentUser?.id && it.phanCongKtv.phanCongId == phanCongId
            }
        }
    }
    val currentPhanCongKtv = currentPhanCongKtvState.value
    val lamViecViewModel: KtvLamViecViewModel = hiltViewModel(backStackEntry)

    val isCurrentUserAllowed = currentPhanCongKtv != null
    var selectedTab by remember { mutableIntStateOf(1) } // tab giữa mặc định là "Thực hiện"
    val isLoading by lamViecViewModel.isGuiMinhChungLoading.collectAsState()


    LaunchedEffect(Unit) {
        viewModel.loadPhanCongChiTiet(phanCongId)
        viewModel.loadDsKtv(phanCongId)
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Công việc hiện tại", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (!isLoading) {
                                navController.popBackStack()
                            }
                        },

                        enabled = !isLoading
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back",  tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = if (selectedTab == 0)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            "Thông tin",
                            color = if (selectedTab == 0)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.Work,
                            contentDescription = null,
                            tint = if (selectedTab == 1)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            "Làm việc",
                            color = if (selectedTab == 1)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            Icons.Default.List,
                            contentDescription = null,
                            tint = if (selectedTab == 2)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            "Tiến trình",
                            color = if (selectedTab == 2)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }

    ) { padding ->
        if (!isCurrentUserAllowed) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Bạn không có quyền truy cập nội dung này")
            }
        } else {
            Column(modifier = Modifier.padding(padding)) {
                when (selectedTab) {
                    0 -> TabChiTietPhanCong(viewModel, navController)
                    1 -> TabCongViec(currentPhanCongKtv.phanCongKtv.id, currentPhanCongKtv.phanCongKtv.trangThai, phanCongId, lamViecViewModel)
                    2 -> TabTienTrinhLamViec(currentPhanCongKtv.phanCongKtv.id)
                }
            }
        }
    }
}

@Composable
fun TabChiTietPhanCong(viewModel: PhanCongDetailViewModel, navController: NavController) {
    var isThongTinExpanded by remember { mutableStateOf(true) }
    var isThietBiExpanded by remember { mutableStateOf(false) }
    var isMinhChungExpanded by remember { mutableStateOf(false) }
    var isKtvExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        // Card: Thông tin yêu cầu
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    onClick = { isThongTinExpanded = !isThongTinExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "🔽 Thông tin yêu cầu",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isThongTinExpanded) {
                    Spacer(Modifier.height(8.dp))
                    TabThongTin(viewModel)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Card: Thiết bị liên quan
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            onClick = { isThietBiExpanded = !isThietBiExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "🔽 Thiết bị liên quan",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isThietBiExpanded) {
                    Spacer(Modifier.height(8.dp))
                    TabThietBi(viewModel)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Card: Minh chứng yêu cầu
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            onClick = { isMinhChungExpanded = !isMinhChungExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "🔽 Minh chứng yêu cầu",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isMinhChungExpanded) {
                    Spacer(Modifier.height(8.dp))
                    TabMinhChung(viewModel)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Card: KTV tham gia
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            onClick = { isKtvExpanded = !isKtvExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "🔽 KTV tham gia",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isKtvExpanded) {
                    Spacer(Modifier.height(8.dp))
                    TabKtvThamGia(viewModel, navController)
                }
            }
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabCongViec(
    phanCongKtvId: Int,
    trangThai: String,
    phanCongId: Int,
    viewModel: KtvLamViecViewModel
) {
    val pcdetailViewModel: PhanCongDetailViewModel = hiltViewModel()
    val imageUris by viewModel.imageUris.collectAsState()
    val videoUri by viewModel.videoUri.collectAsState()
    val thoiGianConLai by viewModel.thoiGianConLai.collectAsState()
    val thoiGianDuKien by viewModel.thoiGianDuKien.collectAsState()
    val dangXinGiaHan by viewModel.dangXinGiaHan.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val showBottomSheet = remember { mutableStateOf(false) }
    val selectedImage = remember { mutableStateOf<Uri?>(null) }
    val showVideoDialog = remember { mutableStateOf(false) }

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    val showTacVuSheet = remember { mutableStateOf(false) }
    val tacVuDangChon = remember { mutableStateOf<LoaiTacVu?>(null) }

    var showUploadErrorDialog by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }

    val thongTin by viewModel.thongTinHoanThanh.collectAsState()
    val isGuiMinhChungLoading by viewModel.isGuiMinhChungLoading.collectAsState()
    BackHandler(enabled = isGuiMinhChungLoading) {
        // ❌ Không làm gì cả => chặn thoát
    }

    LaunchedEffect(Unit) {
        viewModel.uiMessage.collect { message ->
            messageText = message
            showUploadErrorDialog = true
        }
    }

    LaunchedEffect(trangThai) {
        if (trangThai == TrangThaiPhanCong.HOAN_THANH || trangThai == TrangThaiPhanCong.BI_HUY) {
            viewModel.loadThongTinHoanThanh(phanCongKtvId)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            viewModel.addImage(cameraImageUri!!)
        }
    }

    LaunchedEffect(thoiGianConLai, thoiGianDuKien, trangThai) {
        val thoiGian = thoiGianConLai
        if (
            trangThai == TrangThaiPhanCong.DANG_THUC_HIEN &&
            thoiGian != null &&
            thoiGian in 1..(10 * 60 * 1000) &&
            !dangXinGiaHan
        ) {
            scope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Còn chưa tới 10 phút, bạn có muốn xin gia hạn?",
                    actionLabel = "Đã rõ",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    //TODO: xử lý nếu người dùng bấm "Đã rõ"
                }
            }
        }
    } // nhắc nhở gia hạn

    LaunchedEffect(Unit) {
        viewModel.loadThoiGianDuKien(phanCongKtvId)
    }

    LaunchedEffect(trangThai, thoiGianDuKien) {
        if (trangThai == TrangThaiPhanCong.DANG_THUC_HIEN && thoiGianDuKien != null) {
            viewModel.startCountdown(phanCongKtvId, thoiGianDuKien!!)
        } else {
            viewModel.stopCountdown()
        }
    }

    var showCheckInBlockedDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if (trangThai == TrangThaiPhanCong.DA_CHAP_NHAN || trangThai == TrangThaiPhanCong.TAM_NGHI) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Bạn cần xác minh để bắt đầu làm việc",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.kiemTraTruocCheckIn(
                            phanCongKtvId = phanCongKtvId,
                            onKhongDuoc = {
                                scope.launch {
                                    showCheckInBlockedDialog = true
                                }
                            },
                            onDuoc = {
                                tacVuDangChon.value = LoaiTacVu.CHECK_IN
                                showTacVuSheet.value = true
                            }
                        )
                    },
                    modifier = Modifier.size(96.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                }
            }
        }
        if (trangThai == TrangThaiPhanCong.DANG_THUC_HIEN && thoiGianConLai != null && thoiGianDuKien != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (thoiGianConLai!! <= 0) "Đã quá hạn:" else "Thời gian còn lại:",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(Modifier.height(16.dp))

                // Vòng tròn thời gian còn lại
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(240.dp)
                ) {
                    // Vòng tròn nền với tiến độ
                    CircularProgressIndicator(
                        progress = (
                                (thoiGianDuKien!! * 60_000L - thoiGianConLai!!.coerceAtMost(thoiGianDuKien!! * 60_000L)) /
                                        (thoiGianDuKien!! * 60_000f)
                                ).coerceIn(0f, 1f),
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFFFC107), // vàng chính
                        trackColor = Color(0xFFE7CBA3), // vàng nhạt nền
                        strokeWidth = 28.dp

                    )


                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            //text = formatMillis(thoiGianConLai!!),
                            text = formatMillis(kotlin.math.abs(thoiGianConLai!!)),
                            style = MaterialTheme.typography.headlineLarge,
                            color = if (thoiGianConLai!! < 5 * 60 * 1000) Color.Red else Color.Unspecified
                        )
                        Spacer(Modifier.height(4.dp))
                        if (dangXinGiaHan) {
                            var showPopup by remember { mutableStateOf(false) }

                            Box {
                                IconButton(
                                    onClick = { showPopup = !showPopup },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.HourglassTop,
                                        contentDescription = "Chờ duyệt",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }

                                if (showPopup) {
                                    Popup(
                                        alignment = Alignment.TopCenter,
                                        offset = IntOffset(0, -20), // đẩy lên phía trên icon
                                        onDismissRequest = { showPopup = false }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .background(Color(0xFF333333), shape = RoundedCornerShape(8.dp))
                                                .padding(8.dp)
                                        ) {
                                            Text(
                                                text = "Yêu cầu gia hạn của bạn đang chờ phê duyệt",
                                                color = Color.White,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                }
                            }


                        } else {
                            IconButton(onClick = {
                                tacVuDangChon.value = LoaiTacVu.XIN_GIA_HAN
                                showTacVuSheet.value = true
                            }) {
                                Icon(Icons.Default.AddCircle, contentDescription = "Gia hạn",tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(36.dp)    )
                            }
                        }



                    }
                }


                Spacer(Modifier.height(32.dp))

                // Nút tạm nghỉ & hoàn thành
                HanhDongRow(
                    onPauseClick = { tacVuDangChon.value = LoaiTacVu.TAM_NGHI
                                   showTacVuSheet.value = true },
                    onCaptureClick = { tacVuDangChon.value = LoaiTacVu.TAM_NGHI
                        showTacVuSheet.value = true},
                    onCompleteClick = { tacVuDangChon.value = LoaiTacVu.CHECK_OUT
                        showTacVuSheet.value = true}
                )

//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp),
//                    horizontalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    FilledTonalButton(
//                        onClick = {
//                            tacVuDangChon.value = LoaiTacVu.TAM_NGHI
//                            showTacVuSheet.value = true
//                        }
//                        ,
//                        modifier = Modifier.weight(1f),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Icon(Icons.Default.Pause, contentDescription = null)
//                        Spacer(Modifier.width(6.dp))
//                        //Text("Tạm nghỉ")
//                    }
//
//                    FilledTonalButton(
//                        onClick = {
//                            tacVuDangChon.value = LoaiTacVu.CHECK_OUT
//                            showTacVuSheet.value = true
//                        }
//                        ,
//                        modifier = Modifier.weight(1f),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Icon(Icons.Default.CheckCircle, contentDescription = null)
//                        Spacer(Modifier.width(6.dp))
//                        //Text("Hoàn thành")
//                    }
//                }

            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            )
        }
        if (trangThai == TrangThaiPhanCong.HOAN_THANH) {
            // ✅ Biểu tượng lớn ở giữa đầu màn hình
            Column(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(72.dp))
                Spacer(Modifier.height(8.dp))
                Text("Công việc đã hoàn tất", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.tertiary)
            }

            // ✅ Card báo cáo bên dưới
            thongTin?.let {
                BaoCaoHoanThanhCard(it, modifier = Modifier.align(Alignment.Center))
            }
        }
        if (trangThai == TrangThaiPhanCong.BI_HUY) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Block,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text("Phân công đã bị hủy", style = MaterialTheme.typography.titleMedium, color = Color.Red)

                if (thongTin?.thoiGianBatDau != null) {
                    Text(
                        "Mọi nỗ lực trước đó của bạn vẫn được ghi nhận.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                } else {
                    Text(
                        "Bạn chưa bắt đầu công việc trước khi phân công bị hủy.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            thongTin?.thoiGianBatDau?.let {
                BaoCaoHoanThanhCard(info = thongTin!!, modifier = Modifier.align(Alignment.Center))
            }
        }



        if (showBottomSheet.value) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet.value = false },
                modifier = Modifier.fillMaxWidth().heightIn(min = 600.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Ảnh: ${imageUris.size}/5",
                                style = MaterialTheme.typography.titleMedium
                            )

                            IconButton(
                                onClick = {
                                    val photoFile = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")
                                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
                                    cameraImageUri = uri
                                    cameraLauncher.launch(uri)
                                }
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = "Chụp ảnh")
                            }
                        }

                        imageUris.forEach { uri ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clickable { selectedImage.value = uri }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                OutlinedTextField(
                                    value = viewModel.getNoteForImage(uri) ?: "",
                                    onValueChange = { viewModel.updateNoteForImage(uri, it) },
                                    modifier = Modifier.weight(1f),
                                    placeholder = { Text("Nhập ghi chú...") },
                                    maxLines = 2,
                                    singleLine = false
                                )
                                IconButton(onClick = { viewModel.removeImage(uri) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Xóa ảnh", tint = Color.White)
                                }
                            }
                        }

                        videoUri?.let { uri ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .background(Color.Black.copy(alpha = 0.1f))
                                    .clickable { showVideoDialog.value = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🎥", style = MaterialTheme.typography.headlineLarge)
                                IconButton(
                                    onClick = { viewModel.clearVideo() },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Xóa video", tint = Color.Black)
                                }
                            }
                        }
                    }

                    Column {
                        Spacer(Modifier.height(12.dp))
                        Button(
                            onClick = {
                                val photoFile = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")
                                val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
                                cameraImageUri = uri
                                cameraLauncher.launch(uri)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Chụp ảnh")
                        }

                        val canCheckIn = imageUris.isNotEmpty()

                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.checkIn(phanCongKtvId)
                                    showBottomSheet.value = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = canCheckIn
                        ) {
                            if (!canCheckIn) {
                                Text(
                                    text = "Bạn cần chụp ít nhất 1 ảnh để xác minh",
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                            } else {
                                Text("Xác nhận CHECK-IN")
                            }
                        }
                    }
                }
            }
        }

        selectedImage.value?.let { uri ->
            Dialog(onDismissRequest = { selectedImage.value = null }) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )
            }
        }

        if (showVideoDialog.value && videoUri != null) {
            Dialog(
                onDismissRequest = { showVideoDialog.value = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                AndroidView(
                    factory = { context ->
                        VideoView(context).apply {
                            setVideoURI(videoUri)
                            setOnPreparedListener { start() }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(16.dp)
                )
            }
        }

        tacVuDangChon.value?.let { tacVu ->
            if (showTacVuSheet.value) {
                TacVuBottomSheet(
                    tacVu = tacVu,
                    imageUris = imageUris,
                    videoUri = videoUri,
                    onDismiss = { showTacVuSheet.value = false },
                    onChupAnh = {
                        val photoFile = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")
                        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", photoFile)
                        cameraImageUri = uri
                        cameraLauncher.launch(uri)
                    },
                    onXoaAnh = { viewModel.removeImage(it) },
                    onXoaVideo = { viewModel.clearVideo() },
                    getNoteForImage = viewModel::getNoteForImage,
                    updateNoteForImage = viewModel::updateNoteForImage,
                    onSubmit = { soPhut ->
                        scope.launch {
                            showTacVuSheet.value = false
                            val success = viewModel.guiMinhChungTacVu(
                                phanCongKtvId = phanCongKtvId,
                                tacVu = tacVu,
                                soPhut = soPhut
                            )
                            if (success) {
                                pcdetailViewModel.loadDsKtv(phanCongId)
                                showTacVuSheet.value = false
                            }
                        }
                    }



                )
            }
        }

        if (showCheckInBlockedDialog) {
            AlertDialog(
                onDismissRequest = { showCheckInBlockedDialog = false },
                title = { Text("Không thể bắt đầu công việc") },
                text = {
                    Text("Bạn đang thực hiện một công việc khác. Vui lòng hoàn thành trước khi bắt đầu công việc mới.")
                },
                confirmButton = {
                    TextButton(onClick = { showCheckInBlockedDialog = false }) {
                        Text("Đã hiểu")
                    }
                }
            )
        }

        if (showUploadErrorDialog) {
            AlertDialog(
                onDismissRequest = { showUploadErrorDialog = false },
                title = { Text("Thông báo") },
                text = { Text(messageText) },
                confirmButton = {
                    TextButton(onClick = { showUploadErrorDialog = false }) {
                        Text("Đã hiểu")
                    }
                }
            )
        }

        if (isGuiMinhChungLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .clickable(enabled = false) {}, // Chặn mọi tương tác
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }

}




@Composable
fun TabTienTrinhLamViec(
    phanCongKtvId: Int,
    viewModel: TienTrinhLamViecViewModel = hiltViewModel()
) {
    val danhSachNhom by viewModel.danhSachNhom.collectAsState()
    val loaiLoc by viewModel.loaiLoc.collectAsState()
    val sapXepGiam by viewModel.sapXepGiam.collectAsState()
    var nhomDuocChon by remember { mutableStateOf<TienTrinhLamViecViewModel.NhomAnhLamViec?>(null) }

    LaunchedEffect(phanCongKtvId) {
        viewModel.loadTienTrinh(phanCongKtvId)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Dropdown bộ lọc loại ảnh
            var expanded by remember { mutableStateOf(false) }
            val nhanHienThi = loaiLoc ?: "Tất cả"
            val danhSachLoaiLoc = listOf("Tất cả") + LoaiAnhMinhChungLamViec.ALL

            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(nhanHienThi)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    danhSachLoaiLoc.forEach { loai ->
                        DropdownMenuItem(
                            text = { Text(loai) },
                            onClick = {
                                viewModel.setLoaiLoc(if (loai == "Tất cả") null else loai)
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedButton(onClick = { viewModel.toggleSapXep() }) {
                Icon(Icons.Default.Schedule, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (sapXepGiam) "Mới nhất" else "Cũ nhất")
            }
        }

        if (danhSachNhom.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chưa có dữ liệu nào")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                items(danhSachNhom) { nhom ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        .clickable { nhomDuocChon = nhom },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant // hoặc Color.White
                        )

                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(56.dp)) {
                                Image(
                                    painter = rememberAsyncImagePainter(nhom.danhSachAnh.first().urlAnh),
                                    contentDescription = null,
                                    modifier = Modifier.matchParentSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(2.dp)
                                        .size(18.dp)
                                        .background(Color.Black.copy(alpha = 0.7f), shape = MaterialTheme.shapes.small),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = nhom.danhSachAnh.size.toString(),
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = loaiAnhToLabel(nhom.loaiAnh), fontWeight = FontWeight.Bold)
                                Text(text = formatTime(nhom.thoiGian), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
    nhomDuocChon?.let { nhom ->
        Dialog(onDismissRequest = { nhomDuocChon = null }) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = loaiAnhToLabel(nhom.loaiAnh),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp) // Tuỳ chỉnh chiều cao hiển thị ảnh
                    ) {
                        items(nhom.danhSachAnh) { anh ->
                            Card(
                                modifier = Modifier
                                    .width(300.dp)
                                    .fillMaxHeight()
                                ,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface // hoặc Color.White
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp)
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(anh.urlAnh),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    )
                                    anh.ghiChu?.takeIf { it.isNotBlank() }?.let { chu ->
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = chu,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
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
}



@Composable
fun RowItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun BaoCaoHoanThanhCard(info: ThongTinHoanThanh, modifier: Modifier) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface // hoặc Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📋 Báo cáo công việc", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            RowItem("🔹 Thời gian bắt đầu:", formatTime(info.thoiGianBatDau))
            RowItem("✅ Thời gian hoàn thành:", formatTime(info.thoiGianHoanThien))
            RowItem("⏱️ Thời gian làm việc:", "${info.thoiGianLamViec} phút")
            RowItem("🕒 Thời gian phát sinh:", "${info.thoiGianPhatSinh} phút")

            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = (info.thoiGianLamViec / (info.thoiGianLamViec + info.thoiGianPhatSinh).toFloat())
                    .coerceIn(0f, 1f),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(4.dp))
            Text("Tỷ lệ thời gian làm việc", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}



private fun formatTime(millis: Long?): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return millis?.let { sdf.format(Date(it)) } ?: "Không rõ"
}


fun formatMillis(millis: Long): String {
    val minutes = (millis / 1000 / 60) % 60
    val hours = (millis / 1000 / 60 / 60)
    return "%02d:%02d".format(hours, minutes)
}


private fun loaiAnhToLabel(loai: String): String {
    return when (loai) {
        LoaiAnhMinhChungLamViec.CHECK_IN -> "Bắt đầu làm việc"
        LoaiAnhMinhChungLamViec.CHECK_OUT -> "Kết thúc làm việc"
        LoaiAnhMinhChungLamViec.TAM_NGHI -> "Tạm nghỉ"
        LoaiAnhMinhChungLamViec.XIN_GIA_HAN -> "Yêu cầu gia hạn"
        else -> loai
    }
}

@Composable
fun HanhDongRow(
    onPauseClick: () -> Unit,
    onCaptureClick: () -> Unit,
    onCompleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconActionButton(
            icon = Icons.Default.Pause,
            backgroundColor = Color(0xFFFFCDD2), // đỏ nhạt
            contentColor = Color(0xFFD32F2F),
            onClick = onPauseClick
        )

        IconActionButton(
            icon = Icons.Default.PhotoCamera,
            backgroundColor = Color(0xFFE0E0E0), // xám nhạt
            contentColor = Color.DarkGray,
            onClick = onCaptureClick
        )

        IconActionButton(
            icon = Icons.Default.Check,
            backgroundColor = Color(0xFFC8E6C9), // xanh lá nhạt
            contentColor = Color(0xFF388E3C),
            onClick = onCompleteClick
        )
    }
}

@Composable
fun IconActionButton(
    icon: ImageVector,
    backgroundColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .clip(CircleShape) // 👈 Bo tròn đều đẹp
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = backgroundColor.copy(alpha = 0.5f),
                shape = CircleShape
            )
            .shadow(
                elevation = 4.dp, // 👈 Nhẹ hơn
                shape = CircleShape,
                ambientColor = Color.LightGray, // 👈 Bóng sáng, không bị đen
                spotColor = Color.Gray.copy(alpha = 0.2f)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(30.dp) // 👈 Đủ lớn, nổi bật
        )
    }
}

