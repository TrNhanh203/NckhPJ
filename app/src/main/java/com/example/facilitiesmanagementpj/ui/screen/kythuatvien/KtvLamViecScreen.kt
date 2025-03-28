package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.viewmodel.PhanCongDetailViewModel
import com.example.facilitiesmanagementpj.ui.screen.admin.TabThongTin
import com.example.facilitiesmanagementpj.ui.screen.admin.TabThietBi
import com.example.facilitiesmanagementpj.ui.screen.admin.TabMinhChung
import com.example.facilitiesmanagementpj.ui.screen.kythuatvien.TabKtvThamGia
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KtvLamViecScreen(
    navController: NavController,
    phanCongId: Int
) {
    val viewModel: PhanCongDetailViewModel = hiltViewModel()
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
                    1 -> TabCongViec(viewModel)
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

@Composable
fun TabCongViec(viewModel: PhanCongDetailViewModel) {
    Text("[Công việc] - Các thao tác thực hiện công việc")
}

@Composable
fun TabTienTrinhLamViec(viewModel: PhanCongDetailViewModel) {
    Text("[Tiến trình] - Ảnh minh chứng theo thời gian")
}