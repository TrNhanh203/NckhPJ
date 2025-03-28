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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong


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
                    2 -> TabTienTrinhLamViec(viewModel)
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
            modifier = Modifier.fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Ảnh/Video đã chọn:", style = MaterialTheme.typography.titleMedium)

                    imageUris.forEach { uri ->
                        Box(contentAlignment = Alignment.TopEnd) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp)
                                    .clickable { selectedImage.value = uri }
                            )
                            IconButton(onClick = { viewModel.removeImage(uri) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Xóa ảnh", tint = Color.Black)
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
                    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = { pickImageLauncher.launch("image/*") }) {
                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Chọn ảnh")
                        }
                        OutlinedButton(onClick = { pickVideoLauncher.launch("video/*") }) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Chọn video")
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.checkIn(phanCongKtvId)
                                showBottomSheet.value = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Xác nhận CHECK-IN")
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
    } // điều chỉnh hiển thị lại
}



@Composable
fun TabTienTrinhLamViec(viewModel: PhanCongDetailViewModel) {
    Text("[Tiến trình] - Ảnh minh chứng theo thời gian")
}