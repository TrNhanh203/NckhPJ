package com.example.facilitiesmanagementpj.ui.screen.admin

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.facilitiesmanagementpj.R
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.data.utils.LoaiPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBiColor
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.component.TaoPhanCongBottomSheet
import com.example.facilitiesmanagementpj.ui.component.VideoPreviewAdmin
import com.example.facilitiesmanagementpj.ui.navigation.Screen
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminDeviceDetailViewModel
import com.example.facilitiesmanagementpj.ui.viewmodel.sessionViewModel.SessionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDeviceDetailScreen(
    navController: NavController,
    thietBiId: Int,
    yeuCauId: Int
) {
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val currentUser by sessionViewModel.currentUser.collectAsState()
    val viewModel: AdminDeviceDetailViewModel = hiltViewModel()
    val thietBi by viewModel.thietBi.collectAsState()
    val imageUris by viewModel.imageUris.collectAsState()
    val videoUri by viewModel.videoUri.collectAsState()
    val yeuCau by viewModel.yeuCau.collectAsState()
    val chiTietYeuCau by viewModel.chiTietYeuCau.collectAsState()
    val tenDonVi by viewModel.tenDonVi.collectAsState()
    val viTri by viewModel.viTri.collectAsState()
    val isYeuCau = yeuCauId != 0
    var showFullImage by remember { mutableStateOf<Uri?>(null) }
    var showFullVideo by remember { mutableStateOf<Uri?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }

    var loaiPhanCong by rememberSaveable { mutableStateOf(LoaiPhanCong.KHAC) }
    var ghiChu by rememberSaveable { mutableStateOf("") }
    var mucDoUuTien by rememberSaveable { mutableStateOf(1f) }

    val snackbarHostState = remember { SnackbarHostState() }




    val isLoading = thietBi == null

    LaunchedEffect(Unit) {
        viewModel.loadThietBi(thietBiId)
        if (isYeuCau) viewModel.loadChiTietYeuCau(yeuCauId, thietBiId)
    }

    LaunchedEffect(chiTietYeuCau?.id) {
        chiTietYeuCau?.let {
            viewModel.checkPhanCongDaTao(it.id)
        }
    }


    ScaffoldLayout(
        title = if (isYeuCau) "Chi tiết yêu cầu" else "Chi tiết thiết bị",
        navController = navController,
        showBottomBar = false,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { modifier ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.background)
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp , bottom = 64.dp)

                        .then(modifier),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    thietBi?.let { tb ->
                        ThongTinThietBiCard(thietBi = tb)
                        ViTriThietBiCard(viTri = viTri)
                        CardThongKeBaoDuong(tb)
                        GhiChuCard(ghiChu = tb.ghiChu)


                        if (isYeuCau && chiTietYeuCau != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Thông tin yêu cầu", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Spacer(Modifier.height(8.dp))
                                    Text("Đơn vị yêu cầu: $tenDonVi")
                                    Text("Loại yêu cầu: ${chiTietYeuCau!!.loaiYeuCau}")
                                    Text("Mô tả: ${chiTietYeuCau!!.moTa}")
                                }
                            }
                        }

                        if (videoUri != null || imageUris.isNotEmpty()) {
                            Text("Minh chứng yêu cầu:", fontWeight = FontWeight.SemiBold)
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(imageUris) { uri ->
                                    Image(
                                        painter = rememberAsyncImagePainter(uri),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { showFullImage = uri },
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
                                                .clickable { showFullVideo = uri },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("▶", color = Color.White, fontSize = 32.sp)
                                        }
                                    }
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp)) // Khoảng trống nếu cần


                        if (showBottomSheet) {
                            ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                                TaoPhanCongBottomSheet(
                                    loaiPhanCong = loaiPhanCong,
                                    onLoaiPhanCongChange = { loaiPhanCong = it },

                                    ghiChu = ghiChu,
                                    onGhiChuChange = { ghiChu = it },

                                    mucDoUuTien = mucDoUuTien,
                                    onMucDoUuTienChange = { mucDoUuTien = it },

                                    onCreatePhanCong = { loai, note, mucDo ->
                                        viewModel.taoPhanCong(
                                            chiTietYeuCauId = chiTietYeuCau!!.id,
                                            thietBiId = thietBi!!.id,
                                            loaiPhanCong = loai,
                                            ghiChu = note,
                                            mucDoUuTien = mucDo,
                                            nguoiTaoId = currentUser?.id ?: -1
                                        )

                                    },
                                    onDismiss = { showBottomSheet = false }
                                )
                            }
                        }

                    }


                }

                BottomAppBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .height(64.dp), // ⬆️ tăng nhẹ cho cân nút
                    tonalElevation = 8.dp,
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp), // ⬆️ thêm padding đều hơn
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val buttonModifier = Modifier
                            .weight(1f)
                            .height(48.dp)

                        OutlinedButton(
                            onClick = { /* TODO: Xem lịch sử */ },
                            modifier = buttonModifier,
                            shape = RoundedCornerShape(12.dp), // 👈 bo góc
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Xem lịch sử", style = MaterialTheme.typography.titleSmall)
                        }

                        val phanCongDaTao by viewModel.phanCongHienTai.collectAsState()

                        if (phanCongDaTao != null) {
                            LaunchedEffect(phanCongDaTao!!.id) {
                                navController.navigate(Screen.PhanCongDetail.createRoute(phanCongDaTao!!.id))
                            }
                            OutlinedButton(
                                onClick = {
                                    navController.navigate(Screen.PhanCongDetail.createRoute(phanCongDaTao!!.id))
                                },
                                modifier = buttonModifier,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Text("Xem phân công", style = MaterialTheme.typography.titleSmall)
                            }
                        } else if (isYeuCau) {
                            val trangThaiYeuCau = yeuCau?.trangThai ?: ""
                            OutlinedButton(
                                onClick = { showBottomSheet = true },
                                enabled = (thietBi!!.trangThai == TrangThaiThietBi.DANG_HOAT_DONG &&
                                        (trangThaiYeuCau == TrangThaiYeuCau.DA_XAC_NHAN || trangThaiYeuCau == TrangThaiYeuCau.DANG_XU_LY)),
                                modifier = buttonModifier,
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    disabledContentColor = MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Text("Tạo phân công", style = MaterialTheme.typography.titleSmall)
                            }
                        }
                    }
                }

            }

        }
    }

    showFullImage?.let { uri ->
        Dialog(onDismissRequest = { showFullImage = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
                IconButton(
                    onClick = { showFullImage = null },
                    modifier = Modifier.padding(16.dp).size(36.dp).background(Color.DarkGray, CircleShape)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Đóng", tint = Color.White)
                }
            }
        }
    }

    showFullVideo?.let { uri ->
        Dialog(onDismissRequest = { showFullVideo = null }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Video nằm giữa màn hình
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    VideoPreviewAdmin(uri = uri)
                }

                // Nút đóng nằm trên cùng bên phải
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = { showFullVideo = null },
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.DarkGray, CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng", tint = Color.White)
                    }
                }
            }
        }

    }
}

@Composable
fun ViTriThietBiCard(viTri: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column {
            // 🔹 Dải màu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ✅ GIỮ NGUYÊN MÀU ICON
                Image(
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .padding(end = 4.dp)
                    // Không tint
                )

                Spacer(modifier = Modifier.width(8.dp))

                val parts = (viTri ?: "Đang cập nhật...").split(">")
                val annotatedText = buildAnnotatedString {
                    parts.forEachIndexed { index, part ->
                        append(part.trim())
                        if (index != parts.lastIndex) {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            ) {
                                append(" > ")
                            }
                        }
                    }
                }

                Text(
                    text = annotatedText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }


    }
}



@Composable
fun CardThongKeBaoDuong(thietBi: ThietBi) {
    val ngayTiepTheo = thietBi.ngayBaoDuongTiepTheo?.let { Date(it) }
    val ngayGanNhat = thietBi.ngayBaoDuongGanNhat?.let { Date(it) }
    val cycle = thietBi.baoDuongDinhKy ?: 0

    // Tính số ngày còn lại đến kỳ tiếp theo
    val daysLeft = ngayTiepTheo?.let {
        ((it.time - System.currentTimeMillis()) / (1000 * 60 * 60 * 24)).toInt()
    } ?: 0

    val progress = if (cycle > 0 && daysLeft >= 0) {
        1f - (daysLeft / cycle.toFloat())
    } else 1f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column {
            // 🔷 HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "🔧 Thông tin bảo dưỡng",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            // 🔽 BODY: 2 CỘT
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(16.dp)
            ) {
                // ◀️ VÒNG TRÒN BÊN TRÁI
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),

                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Còn lại",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = progress.coerceIn(0f, 1f),
                            strokeWidth = 12.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.size(100.dp)
                        )
                        Text(
                            text = "${daysLeft.coerceAtLeast(0)} ngày",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }


                }

                Spacer(modifier = Modifier.width(16.dp))

                // ▶️ THÔNG TIN BÊN PHẢI
                Column(modifier = Modifier.weight(2f).fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceEvenly) {
                    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                    Text(
                        text = "Chu kỳ BD: ${cycle} ngày",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary)
                    )

                    Text(
                        text = "Sử dụng từ: " +
                                (thietBi.ngayDaCat?.let { sdf.format(Date(it)) }
                                    ?: thietBi.ngayDungSuDung?.let { "Dừng sử dụng: ${sdf.format(Date(it))}" }
                                    ?: "Chưa cập nhật"),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )



                    Text(
                        text = "BD Gần nhất: ${ngayGanNhat?.let { sdf.format(it) } ?: "Không rõ"}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "BD Kế tiếp: ${ngayTiepTheo?.let { sdf.format(it) } ?: "Không rõ"}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}


@Composable
fun ThongTinThietBiCard(
    thietBi: ThietBi
) {
    val statusColor = TrangThaiThietBiColor.getColor(thietBi.trangThai)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), // Viền dùng outline
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer // Nền tổng thể card
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // 🔷 HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Thông tin thiết bị",
                        color = MaterialTheme.colorScheme.onPrimary, // text trắng cho header
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(statusColor, CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.onPrimary, CircleShape) // bo viền trắng
                    )
                }
            }

            // 🔽 BODY
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Icon đại diện
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = getDeviceIconRes(thietBi.loaiThietBiId)),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(Modifier.width(16.dp))

                    Column {
                        Text(
                            text = thietBi.tenThietBi,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = statusColor.copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, statusColor)
                        ) {
                            Text(
                                text = thietBi.trangThai,
                                color = statusColor,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 🔻 Mô tả
                Text(
                    text = "Mô tả thiết bị:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 80.dp, max = 180.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(8.dp))
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = thietBi.moTa ?: "Không có mô tả",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GhiChuCard(
    ghiChu: String?
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { isExpanded = !isExpanded }
    ) {
        Column{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isExpanded) "📋 Ghi chú (đang mở)" else "📋 Ghi chú (nhấn để xem)",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = ghiChu ?: "Không có ghi chú",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    )
                }
            }
        }

    }
}




@DrawableRes
fun getDeviceIconRes(loaiThietBiId: Int): Int {
    return when (loaiThietBiId) {
        1 -> R.drawable.projector     // Máy chiếu
        2 -> R.drawable.airconditioner // Điều hoà
        3 -> R.drawable.computer       // Máy tính
        4 -> R.drawable.microphone     // Micro không dây
        5 -> R.drawable.speaker        // Loa
        6 -> R.drawable.printer        // Máy in
        7 -> R.drawable.photocopy      // Máy photocopy
        8 -> R.drawable.question       // Máy quét (scanner) - chưa có icon riêng
        9 -> R.drawable.smarttv        // Tivi
        10 -> R.drawable.question      // Bảng điện tử
        11 -> R.drawable.question      // Bàn ghế giảng đường
        12 -> R.drawable.question      // Bảng trắng
        13 -> R.drawable.question      // Đèn chiếu sáng
        14 -> R.drawable.question      // Camera giám sát
        15 -> R.drawable.question      // Bộ phát WiFi
        16 -> R.drawable.question      // Bộ lưu điện (UPS)
        17 -> R.drawable.question      // Ổ cắm điện đa năng
        18 -> R.drawable.question      // Máy ảnh kỹ thuật số
        19 -> R.drawable.question      // Máy đo nhiệt độ
        20 -> R.drawable.question      // Máy xét nghiệm
        else -> R.drawable.question    // fallback
    }
}
