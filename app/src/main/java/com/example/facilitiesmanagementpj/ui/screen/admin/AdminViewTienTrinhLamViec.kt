package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.text.font.FontWeight
import com.example.facilitiesmanagementpj.data.utils.LoaiAnhMinhChungLamViec
import com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel.TienTrinhLamViecViewModel
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminViewTienTrinhLamViecScreen(
    navController: NavController,
    phanCongKtvId: Int,
    viewModel: TienTrinhLamViecViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tiến trình làm việc", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back",tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        TabTienTrinhLamViec(
            phanCongKtvId = phanCongKtvId,
            modifier = Modifier.padding(padding),
            viewModel = viewModel
        )
    }
}

@Composable
fun TabTienTrinhLamViec(
    phanCongKtvId: Int,
    modifier: Modifier = Modifier,
    viewModel: TienTrinhLamViecViewModel = hiltViewModel()
) {
    val danhSachNhom by viewModel.danhSachNhom.collectAsState()
    val loaiLoc by viewModel.loaiLoc.collectAsState()
    val sapXepGiam by viewModel.sapXepGiam.collectAsState()
    var nhomDuocChon by remember { mutableStateOf<TienTrinhLamViecViewModel.NhomAnhLamViec?>(null) }

    LaunchedEffect(phanCongKtvId) {
        viewModel.loadTienTrinh(phanCongKtvId)
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        Spacer(modifier = Modifier.height(8.dp))

        if (danhSachNhom.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chưa có dữ liệu minh chứng")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(danhSachNhom) { nhom ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { nhomDuocChon = nhom },
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                            .height(380.dp)
                    ) {
                        items(nhom.danhSachAnh) { anh ->
                            Card(
                                modifier = Modifier
                                    .width(300.dp)
                                    .fillMaxHeight()
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