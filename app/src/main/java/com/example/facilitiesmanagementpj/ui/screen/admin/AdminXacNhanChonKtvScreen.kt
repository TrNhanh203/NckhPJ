package com.example.facilitiesmanagementpj.ui.screen.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminViewDetailProfileViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XacNhanDeCuKtvScreen(
    phanCongId: Int,
    taiKhoanId: Int,
    navController: NavController
) {
    val viewModel: AdminViewDetailProfileViewModel = hiltViewModel()
    val context = LocalContext.current
    val taiKhoan by viewModel.taiKhoanChiTiet.collectAsState()
    val kyThuatVien by viewModel.kyThuatVienChiTiet.collectAsState()
    val chuyenMonList by viewModel.chuyenMonList.collectAsState()
    val xacNhanThanhCong by viewModel.xacNhanThanhCong.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var thoiGianGio by remember { mutableFloatStateOf(0f) }
    var thoiGianPhut by remember { mutableFloatStateOf(0f) }
    var moTa by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(false) }

    val isReadyToConfirm = (thoiGianGio > 0f || thoiGianPhut > 0f) && moTa.text.isNotBlank()

    LaunchedEffect(Unit) {
        viewModel.loadTaiKhoanChiTiet(taiKhoanId)
    }

    LaunchedEffect(xacNhanThanhCong) {
        if (xacNhanThanhCong) {
            Toast.makeText(context, "Đã xác nhận phân công kỹ thuật viên!", Toast.LENGTH_SHORT).show()
            delay(500)
            navController.popBackStack()
        }
    }

    val bottomButtons: @Composable () -> Unit = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Quay lại")
            }
            Button(onClick = { showBottomSheet = true }) {
                Icon(Icons.Default.CheckCircle, contentDescription = null)
                Spacer(Modifier.width(4.dp))
                Text("Chọn")
            }
        }
    }

    ScaffoldLayout(
        title = "Xác nhận đề cử KTV",
        navController = navController,
        showTopBar = true,
        showBottomBar = false,
        showDrawer = false,
        isHomeScreen = false
    ) { modifier ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Thông tin kỹ thuật viên", style = MaterialTheme.typography.titleLarge)
            taiKhoan?.let {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = it.hoTen?.firstOrNull()?.toString() ?: "?",
                                    color = MaterialTheme.colorScheme.primary,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(it.hoTen ?: "", style = MaterialTheme.typography.titleMedium)
                                Text(it.email ?: "", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text("Trạng thái: ${it.trangThai}", style = MaterialTheme.typography.bodyMedium)
                        Text("Số điện thoại: ${it.soDienThoai ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                        Text("Chuyên môn: ${chuyenMonList.joinToString()}", style = MaterialTheme.typography.bodyMedium)
                        kyThuatVien?.let { k ->
                            Spacer(Modifier.height(8.dp))
                            Text("Ngày bắt đầu làm: ${k.ngayBatDauLam ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                            Text("Kinh nghiệm: ${k.kinhNghiem ?: "-"}", style = MaterialTheme.typography.bodyMedium)
                            if (!k.ghiChu.isNullOrBlank()) {
                                Text("Ghi chú: ${k.ghiChu}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(32.dp))
            bottomButtons()
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Thông tin phân công", style = MaterialTheme.typography.titleMedium)

                Text("Thời gian dự kiến")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Giờ: ${thoiGianGio.toInt()}")
                    //Text("${thoiGianGio.toInt()}h")
                    Slider(value = thoiGianGio, onValueChange = { thoiGianGio = it }, valueRange = 0f..12f, steps = 11)

                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Phút: ${thoiGianPhut.toInt()}")
                    Slider(value = thoiGianPhut, onValueChange = { thoiGianPhut = it }, valueRange = 0f..59f, steps = 58)
                    //Text("${thoiGianPhut.toInt()}p")
                }

                OutlinedTextField(
                    value = moTa,
                    onValueChange = { moTa = it },
                    label = { Text("Mô tả công việc") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (isReadyToConfirm) {
                    Button(
                        onClick = {
                            isLoading = true
                            val thoiGianTong = (thoiGianGio * 60 + thoiGianPhut).toInt()
                            viewModel.xacNhanPhanCong(phanCongId, taiKhoanId, thoiGianTong, moTa.text)
                            showBottomSheet = false
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Xác nhận")
                    }
                }
            }
        }
    }
}

