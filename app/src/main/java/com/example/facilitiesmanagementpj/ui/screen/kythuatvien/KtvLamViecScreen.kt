package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.facilitiesmanagementpj.ui.screen.kythuatvien.TabKtvThamGia
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.KtvLamViecViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import java.io.File
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.text.font.FontWeight
import com.example.facilitiesmanagementpj.data.utils.LoaiAnhMinhChungLamViec
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.TienTrinhLamViecViewModel
import java.text.SimpleDateFormat
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KtvLamViecScreen(
    navController: NavController,
    phanCongId: Int
) {
    val viewModel: PhanCongDetailViewModel = hiltViewModel()
    val ktvLamVieciewModel: KtvLamViecViewModel = hiltViewModel()
    val currentUserId = SessionManager.currentUser?.id
    val currentPhanCongKtv = viewModel.dsKtv.collectAsState().value
        .firstOrNull { it.taiKhoan.id == currentUserId && it.phanCongKtv.phanCongId == phanCongId }

    val isCurrentUserAllowed = currentPhanCongKtv != null
    var selectedTab by remember { mutableIntStateOf(1) } // tab giữa mặc định là "Thực hiện"

    LaunchedEffect(Unit) {
        viewModel.loadPhanCongChiTiet(phanCongId)
        viewModel.loadDsKtv(phanCongId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Công việc hiện tại") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    label = { Text("Thông tin") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Work, contentDescription = null) },
                    label = { Text("Làm việc") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = null) },
                    label = { Text("Tiến trình") },
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
                    1 -> TabCongViec(currentPhanCongKtv.phanCongKtv.id,currentPhanCongKtv.phanCongKtv.trangThai,ktvLamVieciewModel)
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
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { isThongTinExpanded = !isThongTinExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🔽 Thông tin yêu cầu", style = MaterialTheme.typography.titleMedium)
                if (isThongTinExpanded) {
                    Spacer(Modifier.height(8.dp))
                    TabThongTin(viewModel)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { isThietBiExpanded = !isThietBiExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🔽 Thiết bị liên quan", style = MaterialTheme.typography.titleMedium)
                if (isThietBiExpanded) {
                    Spacer(Modifier.height(8.dp))
                    TabThietBi(viewModel)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { isMinhChungExpanded = !isMinhChungExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🔽 Minh chứng yêu cầu", style = MaterialTheme.typography.titleMedium)
                if (isMinhChungExpanded) {
                    Spacer(Modifier.height(8.dp))
                    TabMinhChung(viewModel)
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { isKtvExpanded = !isKtvExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🔽 KTV tham gia", style = MaterialTheme.typography.titleMedium)
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
    viewModel: KtvLamViecViewModel = hiltViewModel()
) {
    val imageUris by viewModel.imageUris.collectAsState()
    val videoUri by viewModel.videoUri.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val showBottomSheet = remember { mutableStateOf(false) }
    val selectedImage = remember { mutableStateOf<Uri?>(null) }
    val showVideoDialog = remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.addImage(it) }
    }

    val pickVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.setVideo(it) }
    }

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            viewModel.addImage(cameraImageUri!!)
        }
    }


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
                    onClick = { showBottomSheet.value = true },
                    modifier = Modifier.size(96.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                }
            }
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
                        .weight(1f) // 👈 giúp phần này co giãn
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
                } // nút bấm trên cùng bottom sheet

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
                        enabled = canCheckIn // 👈 chỉ bật khi có ít nhất 1 ảnh
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

                } // nút bấm
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
    } // điều chỉnh hiển thị lại
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
            Box {
                OutlinedButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(loaiLoc ?: "Tất cả")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    LoaiAnhMinhChungLamViec.ALL.forEach { loai ->
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
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    Text(text = loaiAnhToLabel(nhom.loaiAnh), style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    nhom.danhSachAnh.forEach { anh ->
                        Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Image(
                                painter = rememberAsyncImagePainter(anh.urlAnh),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth()
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



private fun formatTime(millis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(millis))
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
