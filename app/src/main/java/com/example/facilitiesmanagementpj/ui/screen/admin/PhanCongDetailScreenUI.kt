package com.example.facilitiesmanagementpj.ui.screen.admin

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.window.Dialog
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtv
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtvWithTaiKhoan
import com.example.facilitiesmanagementpj.data.entity.TaiKhoan
import com.example.facilitiesmanagementpj.data.session.SessionManager
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.ui.component.VideoPreviewAdmin
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.utils.TrangThaiChungCuaPhanCong
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.KtvLamViecViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
                .background(color = MaterialTheme.colorScheme.background)

        ) {

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
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
        TrangThaiPhanCong.BI_HUY to 6
    )
    val list by viewModel.dsKtv.collectAsState()
    val trangThaiChung by viewModel.trangThaiChung.collectAsState()
    val sortedList = list.sortedBy { trangThaiOrder[it.phanCongKtv.trangThai] ?: Int.MAX_VALUE }

    var selectedKtv: PhanCongKtvWithTaiKhoan? by remember { mutableStateOf(null) }

    selectedKtv?.let { ktv ->
        KtvOptionsBottomSheet( item = ktv,
            navController = navController, // truyền vào
            onDismiss = { selectedKtv = null })
    }

    Box(Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)) {
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
                    tint = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Chưa có kỹ thuật viên tham gia",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium
                )
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

        if (trangThaiChung != TrangThaiChungCuaPhanCong.HOAN_THANH) {
            LargeFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.ChonKyThuatVien.createRoute(phanCongId = phanCongId))
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Thêm KTV",
                    modifier = Modifier.size(40.dp)
                )
            }
        } else {
            Text(
                "Phân công đã hoàn thành",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
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
        TrangThaiPhanCong.BI_HUY -> Color(0xFF9E9E9E)
        TrangThaiPhanCong.DA_TU_CHOI -> Color(0xFFF44336)
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline, // ✅ dùng đúng theme
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(6.dp) // ✅ nổi khối lên rõ hơn
    ) {
        Column {
            // 🔹 Viền trạng thái trên cùng
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .background(borderColor)
            )

            // 🔸 Nội dung chính
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar chữ cái
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(4.dp, CircleShape) // 🔸 độ nổi khối
                        .border(
                            width = 2.dp,
                            color = borderColor, // 🔸 màu theo trạng thái
                            shape = CircleShape
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.taiKhoan.hoTen?.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                        color = MaterialTheme.colorScheme.onSurface, // hoặc dùng MaterialTheme.colorScheme.primary nếu bạn muốn giữ màu cố định
                        style = MaterialTheme.typography.titleMedium
                    )
                }


                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.taiKhoan.hoTen.orEmpty(),
                            style = MaterialTheme.typography.titleMedium
                        )

                        if (item.phanCongKtv.dangXinGiaHan) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.tertiaryContainer,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "Đang xin gia hạn",
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }

                    Box {
                        // Viền chữ (lớp dưới, mờ, dịch nhẹ)
                        Text(
                            text = item.phanCongKtv.trangThai,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.primary, // viền là màu nền đối lập
                            modifier = Modifier
                                //.offset(x = 0.5.dp, y = 0.5.dp)
                        )

                        // Chữ thật (lớp trên)
//                        Text(
//                            text = item.phanCongKtv.trangThai,
//                            style = MaterialTheme.typography.bodySmall,
//                            color = borderColor // giữ màu theo trạng thái
//                        )
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KtvOptionsBottomSheet(item: PhanCongKtvWithTaiKhoan, onDismiss: () -> Unit, navController: NavController) {
    val viewModel: PhanCongDetailViewModel = hiltViewModel()
    val ktvLamViecViewModel: KtvLamViecViewModel = hiltViewModel()

    var showRejectReasonDialog by remember { mutableStateOf(false) }
    var showConfirmCancelDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current


    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Tùy chọn cho trạng thái: ${item.phanCongKtv.trangThai}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            val baseOptions = when (item.phanCongKtv.trangThai) {
                TrangThaiPhanCong.CHO_PHAN_HOI -> listOf("Xem thông tin cá nhân", "Gọi điện", "Hủy bỏ")
                TrangThaiPhanCong.DA_CHAP_NHAN -> listOf("Xem thông tin cá nhân", "Gọi điện", "Hủy bỏ")
                TrangThaiPhanCong.DA_TU_CHOI -> listOf("Xem lý do từ chối", "Xem thông tin cá nhân", "Gọi điện")
                TrangThaiPhanCong.DANG_THUC_HIEN -> listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện","Hủy bỏ")
                TrangThaiPhanCong.TAM_NGHI -> listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện", "Hủy bỏ")
                TrangThaiPhanCong.HOAN_THANH -> listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện")
                TrangThaiPhanCong.BI_HUY -> listOf("Xem tiến độ", "Xem thông tin cá nhân", "Gọi điện")
                else -> emptyList()
            }

            val options = baseOptions.toMutableList()

            if (
                item.phanCongKtv.trangThai == TrangThaiPhanCong.DANG_THUC_HIEN &&
                item.phanCongKtv.dangXinGiaHan
            ) {
                options.add(0, "Xem yêu cầu gia hạn")
            }


            options.forEach { option ->
                ListItem(
                    headlineContent = { Text(option) },
                    modifier = Modifier.clickable {
                        when (option) {
                            "Gọi điện" -> {
                                goiDien(context, item.taiKhoan.soDienThoai)
                                onDismiss()
                            }
                            "Xem thông tin cá nhân" -> {onDismiss()
                                navController.navigate(Screen.AdminViewDetailProfile.createRoute(item.taiKhoan.id))}
                            "Xem yêu cầu gia hạn" -> {
                                onDismiss()
                                navController.navigate(Screen.XemYeuCauGiaHan.createRoute(item.phanCongKtv.id))
                            }
                            "Xem lý do từ chối" -> {
                                onDismiss()
                                showRejectReasonDialog = true
                            }
                            "Hủy bỏ" -> {
                                onDismiss()
                                showConfirmCancelDialog = true
                            }
                            "Xem tiến độ" -> {navController.navigate(Screen.AdminViewTienTrinhLamViec.createRoute(item.phanCongKtv.id))}
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
                            // để tổng thời gian họ đang làm việc
                            ktvLamViecViewModel.viewModelScope.launch {
                                ktvLamViecViewModel.tinhVaLuuThongTinLamViec(
                                    phanCongKtvId = item.phanCongKtv.id,
                                    trangThaiKetThuc = TrangThaiPhanCong.BI_HUY
                                )
                            }

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
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()

    val phanCong = viewModel.phanCong.collectAsState().value
    val chiTiet = viewModel.chiTietYeuCau.collectAsState().value
    val tenDonVi = viewModel.tenDonVi.collectAsState().value
    val taiKhoanYeuCau = viewModel.taiKhoanYeuCau.collectAsState().value
    val taiKhoanTaoPhanCong = viewModel.taiKhoanTaoPhanCong.collectAsState().value
    val context = LocalContext.current
    val currentUserId = currentUser?.id

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        InfoCard(title = "Người yêu cầu", icon = Icons.Default.Person) {
            // 🟢 Dòng đầu: Đơn vị
            InfoMini(label = "Đơn vị", value = tenDonVi, modifier = Modifier.fillMaxWidth())

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

            // 🟢 Dòng 2: Họ tên & SĐT
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                InfoMini(
                    label = "Họ tên",
                    value = taiKhoanYeuCau?.hoTen ?: "Không rõ",
                    modifier = Modifier.weight(1f)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "SĐT",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = taiKhoanYeuCau?.soDienThoai ?: "Không rõ",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(Modifier.width(4.dp))

                        if (!taiKhoanYeuCau?.soDienThoai.isNullOrBlank()) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Gọi",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { goiDien(context, taiKhoanYeuCau!!.soDienThoai!!) }
                                    .padding(2.dp) // optional
                            )
                        }
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

            // 🟢 Mô tả (giống Ghi chú)
            Text(
                "Mô tả",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(4.dp))
            ExpandableTextBox(text = chiTiet?.moTa ?: "Không rõ")
        }

        // 🔹 Thông tin phân công chung
        InfoCard(title = "Thông tin phân công", icon = Icons.Default.Info) {
            phanCong?.let {
                // 🟩 Hàng 1: Loại phân công + Thời gian tạo
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    InfoMini(label = "Loại phân công", value = it.loaiPhanCong, modifier = Modifier.weight(1f))
                    InfoMini(label = "Thời gian tạo", value = it.thoiGianTaoPhanCong.toDateTimeString(), modifier = Modifier.weight(1f))
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                // 🟩 Hàng 2: Mức độ ưu tiên (có thanh) + Số lượng KTV
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Mức độ ưu tiên", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        PriorityLevelBarGameStyle(
                            level = it.mucDoUuTien.toInt(),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(16.dp),
                            iconSize = 20.dp
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    InfoMini(label = "Số lượng KTV", value = it.soLuongKTVThamGia?.toString() ?: "Không rõ", modifier = Modifier.weight(1f))
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                // 🟩 Ghi chú: chiếm toàn hàng
                Text("Ghi chú", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                ExpandableTextBox(text = it.ghiChu ?: "Không có")

                Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

                // 🟩 Hàng 3: Người tạo + SĐT (có nút gọi)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    InfoMini(
                        label = "Người tạo",
                        value = taiKhoanTaoPhanCong?.hoTen ?: "Không rõ",
                        modifier = Modifier.weight(1f)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "SĐT",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = taiKhoanTaoPhanCong?.soDienThoai ?: "Không rõ",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            if (!taiKhoanTaoPhanCong?.soDienThoai.isNullOrBlank()) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Gọi",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { goiDien(context, taiKhoanTaoPhanCong!!.soDienThoai!!) }
                                        .padding(2.dp) // optional
                                )
                            }
                        }
                    }
                }

            }
        }

        // 🔹 Thông tin phân công riêng của KTV
        if (phanCong != null && currentUserId != null) {
            val ktvPhanCong = viewModel.dsKtv.collectAsState().value.firstOrNull {
                it.taiKhoan.id == currentUserId && it.phanCongKtv.phanCongId == phanCong.id
            }
            ktvPhanCong?.let {
                InfoCard(title = "Phân công dành riêng cho bạn", icon = Icons.Default.Star) {
                    // Thời gian dự kiến
                    InfoRow("Thời gian dự kiến", "${it.phanCongKtv.thoiGianDuKien} phút")

                    // Mô tả công việc với vùng mở rộng
                    Column {
                        Text(
                            "Mô tả công việc",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.height(4.dp))
                        ExpandableTextBox(text = it.phanCongKtv.moTaCongViec ?: "Không có")
                    }
                }
            }

        }
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // nổi khối rõ hơn
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column {
            // 🔷 Header có màu chủ đạo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // 🔸 Body phần nội dung
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}


@Composable
fun InfoMini(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(end = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )

    }
}


@Composable
fun TabThietBi(viewModel: PhanCongDetailViewModel) {
    val thietBi = viewModel.thietBi.collectAsState().value
    val viTri = viewModel.viTri.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        thietBi?.let {
            ThongTinThietBiCard(it)
            ViTriThietBiCard(viTri)
            CardThongKeBaoDuong(it)
            GhiChuCard(it.ghiChu)
        }
    }
}


@Composable
fun TabMinhChung(viewModel: PhanCongDetailViewModel) {
    val imageUris = viewModel.imageUris.collectAsState().value
    val videoUri = viewModel.videoUri.collectAsState().value

    var showImagePreview by remember { mutableStateOf<Uri?>(null) }
    var showVideoPreview by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 🔹 Vùng ảnh
        Column {
            Text(
                "Ảnh minh chứng",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground // hoặc onSurface
            )

            if (imageUris.isEmpty()) {
                Text("Không có ảnh được đính kèm", color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    tonalElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surfaceContainerHighest, // màu nền dịu
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),

                ) {
                    LazyRow(
                        modifier = Modifier
                            .padding(12.dp), // khoảng cách bên trong vùng
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(imageUris) { uri ->
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
                                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                                    .clickable { showImagePreview = uri },
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(uri),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }

            }
        }

        // 🔹 Vùng video
        Column {
            Text("Video minh chứng", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)

            videoUri?.let { uri ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                        .clickable { showVideoPreview = uri },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Xem video",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(48.dp)
                    )
                }

            } ?: Text("Không có video được đính kèm", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

    // 🖼 Xem toàn màn hình ảnh
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

    // ▶ Xem toàn màn hình video
    showVideoPreview?.let { uri ->
        Dialog(onDismissRequest = { showVideoPreview = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                VideoPreviewAdmin(uri = uri)
            }
        }

    }
}


fun goiDien(context: Context, soDienThoai: String?) {
    if (!soDienThoai.isNullOrBlank()) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$soDienThoai".toUri()
        }
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show()
    }
}
fun Long.toDateTimeString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return format.format(date)
}
@Composable
fun ExpandableTextBox(
    text: String,
    collapsedLines: Int = 3
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            text = text,
            maxLines = if (expanded) Int.MAX_VALUE else collapsedLines,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (text.length > 60) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (expanded) "Thu gọn" else "Xem thêm",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { expanded = !expanded }
            )
        }
    }
}



