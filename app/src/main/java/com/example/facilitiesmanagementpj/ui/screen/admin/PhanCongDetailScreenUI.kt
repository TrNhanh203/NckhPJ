package com.example.facilitiesmanagementpj.ui.screen.admin

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.ui.viewmodel.PhanCongDetailViewModel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtv
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtvWithTaiKhoan
import com.example.facilitiesmanagementpj.data.entity.TaiKhoan
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.ui.component.VideoPreviewAdmin
import com.example.facilitiesmanagementpj.ui.navigation.Screen


@Composable
fun PhanCongDetailScreen(
    navController: NavController,
    phanCongId: Int
) {
    val viewModel: PhanCongDetailViewModel = hiltViewModel()
    val tabTitles = listOf("Kỹ thuật viên", "Thông tin","Thiết bị", "Ảnh & video")
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadPhanCongChiTiet(phanCongId)
        viewModel.loadDsKtv(phanCongId)
    }

    com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout(
        title = "Chi tiết phân công",
        navController = navController,
        showBottomBar = false,

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(paddingValues)

        ) {

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 0.dp
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> TabKTV(viewModel, navController, phanCongId)
                1 -> TabThongTin(viewModel)
                2 -> TabThietBi(viewModel)
                3 -> TabMinhChung(viewModel)
            }
        }
    }
}




@Composable
fun TabKTV(viewModel: PhanCongDetailViewModel, navController: NavController, phanCongId: Int) {
    val trangThaiOrder = mapOf(
        TrangThaiPhanCong.DA_TU_CHOI to 0,
        TrangThaiPhanCong.CHO_PHAN_HOI to 1,
        TrangThaiPhanCong.DA_CHAP_NHAN to 2,
        TrangThaiPhanCong.DANG_THUC_HIEN to 3,
        TrangThaiPhanCong.TAM_NGHI to 4,
        TrangThaiPhanCong.HOAN_THANH to 5,
        TrangThaiPhanCong.THAY_NGUOI to 6,
        TrangThaiPhanCong.BI_HUY to 7
    )
    val list by viewModel.dsKtv.collectAsState()
    val sortedList = list.sortedBy { trangThaiOrder[it.phanCongKtv.trangThai] ?: Int.MAX_VALUE }

    var selectedKtv: PhanCongKtvWithTaiKhoan? by remember { mutableStateOf(null) }

    selectedKtv?.let { ktv ->
        KtvOptionsBottomSheet( item = ktv,
            navController = navController, // truyền vào
            onDismiss = { selectedKtv = null })
    }

    Box(Modifier.fillMaxSize()) {
        if (sortedList.isEmpty()) {
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
                items(sortedList) { item ->
                    KtvCard(item) {
                        selectedKtv = item
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        LargeFloatingActionButton(
            onClick = { navController.navigate(Screen.ChonKyThuatVien.createRoute(phanCongId = phanCongId)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(Icons.Default.Add, contentDescription = "Thêm KTV", tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun KtvCard(item: PhanCongKtvWithTaiKhoan, onClick: () -> Unit) {
    val borderColor = when (item.phanCongKtv.trangThai) {
        TrangThaiPhanCong.DA_CHAP_NHAN, TrangThaiPhanCong.HOAN_THANH -> Color(0xFF4CAF50)
        TrangThaiPhanCong.CHO_PHAN_HOI -> Color(0xFFFFC107)
        TrangThaiPhanCong.DANG_THUC_HIEN -> Color(0xFF2196F3)
        TrangThaiPhanCong.TAM_NGHI -> Color(0xFFFF9800)
        TrangThaiPhanCong.BI_HUY, TrangThaiPhanCong.THAY_NGUOI -> Color(0xFF9E9E9E)
        TrangThaiPhanCong.DA_TU_CHOI -> Color(0xFFF44336)
        else -> Color.LightGray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                        text = item.taiKhoan.hoTen?.first().toString(),
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

                        if (item.phanCongKtv.dangXinGiaHan) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFF3E0), shape = RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "Đang xin gia hạn",
                                    color = Color(0xFFFF9800),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KtvOptionsBottomSheet(item: PhanCongKtvWithTaiKhoan, onDismiss: () -> Unit, navController: NavController) {
    val viewModel: PhanCongDetailViewModel = hiltViewModel()

    var showRejectReasonDialog by remember { mutableStateOf(false) }
    var showConfirmCancelDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tùy chọn cho trạng thái: ${item.phanCongKtv.trangThai}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            val baseOptions = when (item.phanCongKtv.trangThai) {
                TrangThaiPhanCong.CHO_PHAN_HOI -> listOf("Thay người", "Hủy bỏ", "Xem thông tin cá nhân", "Gọi điện")
                TrangThaiPhanCong.DA_CHAP_NHAN -> listOf("Thay người", "Hủy bỏ", "Xem thông tin cá nhân", "Gọi điện")
                TrangThaiPhanCong.DA_TU_CHOI -> listOf("Xem lý do từ chối", "Thay người", "Hủy bỏ", "Xem thông tin cá nhân", "Gọi điện")
                TrangThaiPhanCong.DANG_THUC_HIEN -> listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện")
                TrangThaiPhanCong.TAM_NGHI -> listOf("Xem tiến độ", "Thay người", "Hủy bỏ", "Xem thông tin cá nhân", "Gọi điện")
                TrangThaiPhanCong.HOAN_THANH -> listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện")
                TrangThaiPhanCong.THAY_NGUOI, TrangThaiPhanCong.BI_HUY -> listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện")
                else -> emptyList()
            }

            val options = baseOptions.toMutableList()

            if (
                item.phanCongKtv.trangThai == TrangThaiPhanCong.DANG_THUC_HIEN &&
                item.phanCongKtv.dangXinGiaHan
            ) {
                options.add(0, "Xem yêu cầu gia hạn") // hoặc thêm ở cuối tùy bạn
            }


            options.forEach { option ->
                ListItem(
                    headlineContent = { Text(option) },
                    modifier = Modifier.clickable {
                        when (option) {
                            "Xem yêu cầu gia hạn" -> {
                                navController.navigate(Screen.XemYeuCauGiaHan.createRoute(item.phanCongKtv.id))
                            }
                            "Xem lý do từ chối" -> showRejectReasonDialog = true
                            "Hủy bỏ" -> showConfirmCancelDialog = true
                            else -> {} // các option khác xử lý sau
                        }
                    }

                )
            }

            if (showRejectReasonDialog) {
                AlertDialog(
                    onDismissRequest = { showRejectReasonDialog = false },
                    title = { Text("Lý do từ chối") },
                    text = {
                        Text(item.phanCongKtv.lyDoTuChoi ?: "Không có lý do được cung cấp.")
                    },
                    confirmButton = {
                        TextButton(onClick = { showRejectReasonDialog = false }) {
                            Text("Đóng")
                        }
                    }
                )
            }

            if (showConfirmCancelDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmCancelDialog = false },
                    title = { Text("Xác nhận huỷ bỏ") },
                    text = {
                        Text("Bạn có chắc chắn muốn huỷ phân công của kỹ thuật viên này không?")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.huyPhanCongChoKtv(item.phanCongKtv.id)
                            showConfirmCancelDialog = false
                            onDismiss() // đóng bottom sheet
                        }) {
                            Text("Xác nhận")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showConfirmCancelDialog = false }) {
                            Text("Huỷ")
                        }
                    }
                )
            }
        }
    }




}


@Composable
fun TabThongTin(viewModel: PhanCongDetailViewModel) {
    val phanCong = viewModel.phanCong.collectAsState().value
    val chiTiet = viewModel.chiTietYeuCau.collectAsState().value
    val tenDonVi = viewModel.tenDonVi.collectAsState().value
    val taiKhoanYeuCau = viewModel.taiKhoanYeuCau.collectAsState().value
    val taiKhoanTaoPhanCong = viewModel.taiKhoanTaoPhanCong.collectAsState().value

    val currentUserId = SessionManager.currentUser?.id

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Thông tin người yêu cầu
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Người yêu cầu", style = MaterialTheme.typography.titleMedium)
                Text("Loại yêu cầu: ${chiTiet?.loaiYeuCau}")
                Text("Mô tả: ${chiTiet?.moTa}")
                Text("Họ tên: ${taiKhoanYeuCau?.hoTen ?: "Không rõ"}")
                Text("SĐT: ${taiKhoanYeuCau?.soDienThoai ?: "Không rõ"}")
                Text("Đơn vị: $tenDonVi")
            }
        }


        // Thông tin phân công chung
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Thông tin phân công", style = MaterialTheme.typography.titleMedium)
                phanCong?.let {
                    Text("Loại phân công: ${it.loaiPhanCong}")
                    Text("Mức độ ưu tiên: ${it.mucDoUuTien}")
                    Text("Ghi chú: ${it.ghiChu ?: "Không có"}")
                    Text("Thời gian tạo: ${it.thoiGianTaoPhanCong}")
                    Text("Số lượng KTV: ${it.soLuongKTVThamGia ?: "Chưa xác định"}")
                    Text("Người tạo: ${taiKhoanTaoPhanCong?.hoTen ?: "Không rõ"}")
                    Text("SĐT: ${taiKhoanTaoPhanCong?.soDienThoai ?: "Không rõ"}")
                }
            }
        }

        // Thông tin phân công riêng (chỉ hiển thị nếu đúng là KTV đó)
        if (phanCong != null && currentUserId != null) {
            val ktvPhanCong = viewModel.dsKtv.collectAsState().value.firstOrNull {
                it.taiKhoan.id == currentUserId && it.phanCongKtv.phanCongId == phanCong.id
            }
            ktvPhanCong?.let {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Phân công dành riêng cho bạn", style = MaterialTheme.typography.titleMedium)
                        Text("Mô tả công việc: ${it.phanCongKtv.moTaCongViec ?: "Không có"}")
                        Text("Thời gian dự kiến: ${it.phanCongKtv.thoiGianDuKien} phút")
                    }
                }
            }
        }


    }
}



@Composable
fun TabThietBi(viewModel: PhanCongDetailViewModel) {
    val thietBi = viewModel.thietBi.collectAsState().value
    val viTri = viewModel.viTri.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Thông tin thiết bị", style = MaterialTheme.typography.titleMedium)

        thietBi?.let {
            Text("Tên thiết bị: ${it.tenThietBi}")
            Text("Loại thiết bị ID: ${it.loaiThietBiId}")
            Text("Ngày cài đặt: ${it.ngayDaCat ?: "Chưa rõ"}")
            Text("Bảo dưỡng định kỳ (ngày): ${it.baoDuongDinhKy ?: "Không xác định"}")
            Text("Ngày bảo dưỡng gần nhất: ${it.ngayBaoDuongGanNhat ?: "Không có"}")
            Text("Vị trí: $viTri")

            Spacer(modifier = Modifier.height(8.dp))
            Text("Mô tả:", style = MaterialTheme.typography.bodyLarge)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(8.dp)
                    .background(Color(0xFFF2F2F2))
            ) {
                BasicTextField(
                    value = it.moTa ?: "Không có mô tả",
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}

@Composable
fun TabMinhChung(viewModel: PhanCongDetailViewModel) {
    val imageUris = viewModel.imageUris.collectAsState().value
    val videoUri = viewModel.videoUri.collectAsState().value

    var showImagePreview by remember { mutableStateOf<Uri?>(null) }
    var showVideoPreview by remember { mutableStateOf<Uri?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Minh chứng đính kèm", style = MaterialTheme.typography.titleMedium)

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(imageUris) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showImagePreview = uri },
                    contentScale = ContentScale.Crop
                )
            }

            item {
                videoUri?.let { uri ->
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black)
                            .clickable { showVideoPreview = uri },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("▶", color = Color.White, fontSize = 32.sp)
                    }
                }
            }
        }
    }

    showImagePreview?.let { uri ->
        Dialog(onDismissRequest = { showImagePreview = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }

    showVideoPreview?.let { uri ->
        Dialog(onDismissRequest = { showVideoPreview = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                VideoPreviewAdmin(uri = uri)
            }
        }
    }
}


