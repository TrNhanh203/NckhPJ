package com.example.facilitiesmanagementpj.ui.screen.kythuatvien

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtvWithTaiKhoan
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.screen.admin.TabMinhChung
import com.example.facilitiesmanagementpj.ui.screen.admin.TabThietBi
import com.example.facilitiesmanagementpj.ui.screen.admin.TabThongTin
import com.example.facilitiesmanagementpj.ui.viewmodel.PhanCongDetailViewModel
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.example.facilitiesmanagementpj.ui.navigation.Screen


@Composable
fun KtvXemChiTietPhanCongScreen(
    navController: NavController,
    phanCongId: Int
) {
    val viewModel: PhanCongDetailViewModel = hiltViewModel()
    val tabTitles = listOf("Thông tin", "Thiết bị", "Minh chứng", "KTV tham gia")

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadPhanCongChiTiet(phanCongId)
        viewModel.loadDsKtv(phanCongId)
    }

    val bottomBarHeight = 72.dp

    Box(modifier = Modifier.fillMaxSize()) {
        ScaffoldLayout(
            title = "Chi tiết công việc",
            navController = navController,
            showBottomBar = false,
            showDrawer = false
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = bottomBarHeight)
                    .then(padding)
            ) {
                ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title) }
                        )
                    }
                }

                when (selectedTabIndex) {
                    0 -> TabThongTin(viewModel)
                    1 -> TabThietBi(viewModel)
                    2 -> TabMinhChung(viewModel)
                    3 -> TabKtvThamGia(viewModel, navController)
                }
            }
        }

        BottomAppBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(bottomBarHeight),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            OutlinedButton(onClick = { /* TODO: Từ chối */ }) {
                Icon(Icons.Default.Close, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Từ chối")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(onClick = { /* TODO: Chấp nhận */ }) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Chấp nhận")
            }
        }
    }
}

@Composable
fun TabKtvThamGia(viewModel: PhanCongDetailViewModel, navController: NavController) {
    val list by viewModel.dsKtv.collectAsState()
    val currentUserId = SessionManager.currentUser?.id

    if (list.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.LightGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("Chưa có kỹ thuật viên tham gia", color = Color.Gray)
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(list) { item ->
                KtvThamGiaCard(item, currentUserId, navController = navController)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun KtvThamGiaCard(item: PhanCongKtvWithTaiKhoan, currentUserId: Int?, navController: NavController) {
    val borderColor = when (item.phanCongKtv.trangThai) {
        TrangThaiPhanCong.DA_CHAP_NHAN, TrangThaiPhanCong.HOAN_THANH -> Color(0xFF4CAF50)
        TrangThaiPhanCong.CHO_PHAN_HOI -> Color(0xFFFFC107)
        TrangThaiPhanCong.DANG_THUC_HIEN -> Color(0xFF2196F3)
        TrangThaiPhanCong.TAM_NGHI -> Color(0xFFFF9800)
        TrangThaiPhanCong.BI_HUY, TrangThaiPhanCong.THAY_NGUOI -> Color(0xFF9E9E9E)
        TrangThaiPhanCong.DA_TU_CHOI -> Color(0xFFF44336)
        else -> Color.LightGray
    }

    val isCurrentUser = item.taiKhoan.id == currentUserId
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Box {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (!isCurrentUser) Modifier.clickable { expanded = true } else Modifier),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(borderColor)
                )

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.taiKhoan.hoTen?.firstOrNull()?.toString() ?: "?",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.taiKhoan.hoTen.toString(),
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (isCurrentUser) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("(Bạn)", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                        }
                        Text(
                            text = item.phanCongKtv.trangThai,
                            style = MaterialTheme.typography.bodySmall,
                            color = borderColor
                        )
                    }
                }
            }
        }

        // DropdownMenu hiển thị khi nhấn vào card
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
//            DropdownMenuItem(
//                text = { Text("Xem thông tin cá nhân") },
//                onClick = {
//                    navController.navigate(Screen.AdminViewDetailProfile.createRoute(item.taiKhoan.id))
//                    expanded = false
//                }
//            )
            DropdownMenuItem(
                text = { Text("Gọi điện") },
                onClick = {
                    expanded = false
                    val soDienThoai = item.taiKhoan.soDienThoai
                    if (!soDienThoai.isNullOrBlank()) {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:$soDienThoai".toUri()
                        }
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                }
            )

        }
    }
}



//package com.example.facilitiesmanagementpj.ui.screen.kythuatvien
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Check
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.PersonAdd
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.facilitiesmanagementpj.data.entity.PhanCongKtvWithTaiKhoan
//import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
//import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
//import com.example.facilitiesmanagementpj.ui.viewmodel.PhanCongDetailViewModel
//
//@Composable
//fun KtvXemChiTietPhanCongScreen(
//    navController: NavController,
//    phanCongId: Int
//) {
//    val viewModel: PhanCongDetailViewModel = hiltViewModel()
//    val tabTitles = listOf("KTV tham gia", "Thông tin", "Thiết bị", "Minh chứng")
//    var selectedTabIndex by remember { mutableIntStateOf(0) }
//
//    LaunchedEffect(Unit) {
//        viewModel.loadPhanCongChiTiet(phanCongId)
//        viewModel.loadDsKtv(phanCongId)
//    }
//
//    ScaffoldLayout(
//        title = "Chi tiết công việc",
//        navController = navController,
//        showBottomBar = false,
//        showDrawer = false
//    ) { padding ->
//        Column(modifier = Modifier.fillMaxSize().then(padding)) {
//            ScrollableTabRow(selectedTabIndex = selectedTabIndex) {
//                tabTitles.forEachIndexed { index, title ->
//                    Tab(
//                        selected = selectedTabIndex == index,
//                        onClick = { selectedTabIndex = index },
//                        text = { Text(title) }
//                    )
//                }
//            }
//
//            when (selectedTabIndex) {
//                0 -> TabKtvThamGia(viewModel)
//                1 -> com.example.facilitiesmanagementpj.ui.screen.admin.TabThongTin(viewModel)
//                2 -> com.example.facilitiesmanagementpj.ui.screen.admin.TabThietBi(viewModel)
//                3 -> com.example.facilitiesmanagementpj.ui.screen.admin.TabMinhChung(viewModel)
//            }
//
//            BottomAppBar(
//                actions = {
//                    OutlinedButton(onClick = { /* TODO: Từ chối */ }) {
//                        Icon(Icons.Default.Close, contentDescription = null)
//                        Spacer(Modifier.width(4.dp))
//                        Text("Từ chối")
//                    }
//
//                    Spacer(modifier = Modifier.weight(1f)) // đẩy nút chấp nhận về phải
//
//                    Button(onClick = { /* TODO: Chấp nhận */ }) {
//                        Icon(Icons.Default.Check, contentDescription = null)
//                        Spacer(Modifier.width(4.dp))
//                        Text("Chấp nhận")
//                    }
//                },
//                containerColor = MaterialTheme.colorScheme.surface,
//                tonalElevation = 4.dp
//            )
//
//        }
//    }
//
//
//
//}
//
//@Composable
//fun TabKtvThamGia(viewModel: PhanCongDetailViewModel) {
//    val list by viewModel.dsKtv.collectAsState()
//    if (list.isEmpty()) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Icon(
//                imageVector = Icons.Default.PersonAdd,
//                contentDescription = null,
//                modifier = Modifier.size(64.dp),
//                tint = Color.LightGray
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text("Chưa có kỹ thuật viên tham gia", color = Color.Gray)
//        }
//    } else {
//        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//            items(list) { item ->
//                KtvThamGiaCard(item)
//                Spacer(modifier = Modifier.height(12.dp))
//            }
//        }
//    }
//}
//
//@Composable
//fun KtvThamGiaCard(item: PhanCongKtvWithTaiKhoan) {
//    val borderColor = when (item.phanCongKtv.trangThai) {
//        TrangThaiPhanCong.DA_CHAP_NHAN, TrangThaiPhanCong.HOAN_THANH -> Color(0xFF4CAF50)
//        TrangThaiPhanCong.CHO_PHAN_HOI -> Color(0xFFFFC107)
//        TrangThaiPhanCong.DANG_THUC_HIEN -> Color(0xFF2196F3)
//        TrangThaiPhanCong.TAM_NGHI -> Color(0xFFFF9800)
//        TrangThaiPhanCong.BI_HUY, TrangThaiPhanCong.THAY_NGUOI -> Color(0xFF9E9E9E)
//        TrangThaiPhanCong.DA_TU_CHOI -> Color(0xFFF44336)
//        else -> Color.LightGray
//    }
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Column {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(6.dp)
//                    .background(borderColor)
//            )
//
//            Row(
//                modifier = Modifier.padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(48.dp)
//                        .clip(CircleShape)
//                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = item.taiKhoan.hoTen?.firstOrNull()?.toString() ?: "?",
//                        color = MaterialTheme.colorScheme.primary,
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                Column {
//                    Text(
//                        text = item.taiKhoan.hoTen.toString(),
//                        style = MaterialTheme.typography.titleMedium
//                    )
//                    Text(
//                        text = item.phanCongKtv.trangThai,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = borderColor
//                    )
//                }
//            }
//        }
//    }
//}
