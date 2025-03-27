package com.example.facilitiesmanagementpj.ui.screen.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan
import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminViewDetailProfileViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AdminViewDetailProfileScreen(
    navController: NavController,
    taiKhoanId: Int,
    viewModel: AdminViewDetailProfileViewModel = hiltViewModel()
) {
    val taiKhoanChiTiet by viewModel.taiKhoanChiTiet.collectAsState()
    val chuyenMonList by viewModel.chuyenMonList.collectAsState()
    val kyThuatVienChiTiet by viewModel.kyThuatVienChiTiet.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTaiKhoanChiTiet(taiKhoanId)
    }

    ScaffoldLayout(title = "Thông tin tài khoản", navController = navController) { modifier ->
        taiKhoanChiTiet?.let { taiKhoan ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = taiKhoan.hoTen?.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Tài khoản info
                Card(shape = RoundedCornerShape(12.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Thông tin tài khoản", style = MaterialTheme.typography.titleMedium)
                        InfoRow(Icons.Default.Person, "Họ tên", taiKhoan.hoTen ?: "--")
                        InfoRow(Icons.Default.Email, "Email", taiKhoan.email ?: "--")
                        InfoRow(Icons.Default.Phone, "SĐT", taiKhoan.soDienThoai ?: "--")
                        InfoRow(null, "Vai trò", taiKhoan.tenVaiTro)
                        if (taiKhoan.tenVaiTro == "Quản Lý Đơn Vị") {
                            InfoRow(null, "Đơn vị", taiKhoan.tenDonVi ?: "--")
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Trạng thái: ", style = MaterialTheme.typography.bodyMedium)
                            AssistChip(
                                onClick = {},
                                label = { Text(taiKhoan.trangThai) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = getColorForTrangThai(taiKhoan.trangThai),
                                    labelColor = Color.White
                                )
                            )
                        }
                    }
                }

                // Chuyên môn (nếu là KTV)
                if (taiKhoan.tenVaiTro == "Kỹ Thuật Viên") {
                    Card(shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Chuyên môn", style = MaterialTheme.typography.titleMedium)
                            if (chuyenMonList.isEmpty()) {
                                Text("Chưa có chuyên môn")
                            } else {
                                chuyenMonList.forEach { Text("- $it") }
                            }
                        }
                    }
                }

                // Kỹ thuật viên info
                kyThuatVienChiTiet?.let { ktv ->
                    Card(shape = RoundedCornerShape(12.dp)) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Thông tin kỹ thuật viên", style = MaterialTheme.typography.titleMedium)
                            InfoRow(null, "Ngày vào làm", ktv.ngayBatDauLam?.let {
                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(it))
                            } ?: "Chưa cập nhật")
                            InfoRow(null, "Số ngày làm việc", (ktv.kinhNghiem ?: "--").toString())
                            InfoRow(null, "Ghi chú", ktv.ghiChu ?: "Không có")
                        }
                    }
                }

                // Nút hành động
                when (taiKhoan.trangThai) {
                    TrangThaiTaiKhoan.CHO_XAC_THUC -> {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = {
                                viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.NGOAI_TUYEN)
                            }) { Text("Xác thực") }
                            OutlinedButton(
                                onClick = {
                                    viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, "TỪ_CHỐI")
                                },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                            ) {
                                Text("Từ chối")
                            }
                        }
                    }
                    TrangThaiTaiKhoan.NGOAI_TUYEN, TrangThaiTaiKhoan.TRUC_TUYEN -> {
                        Button(
                            onClick = {
                                viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.BI_KHOA)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Text("Khóa tài khoản")
                        }
                    }
                    TrangThaiTaiKhoan.BI_KHOA -> {
                        Button(onClick = {
                            viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.NGOAI_TUYEN)
                        }) {
                            Text("Mở khóa tài khoản")
                        }
                    }
                }
            }
        } ?: run {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector?, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(8.dp))
        }
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun getColorForTrangThai(trangThai: String): Color {
    return when (trangThai) {
        TrangThaiTaiKhoan.TRUC_TUYEN -> Color(0xFF4CAF50)
        TrangThaiTaiKhoan.NGOAI_TUYEN -> Color(0xFF9E9E9E)
        TrangThaiTaiKhoan.BI_KHOA -> Color(0xFFF44336)
        TrangThaiTaiKhoan.CHO_XAC_THUC -> Color(0xFFFFC107)
        else -> Color.Gray
    }
}



//package com.example.facilitiesmanagementpj.ui.screen.admin
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.facilitiesmanagementpj.R
//import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan
//import com.example.facilitiesmanagementpj.ui.component.ScaffoldLayout
//import com.example.facilitiesmanagementpj.ui.viewmodel.AdminViewDetailProfileViewModel
//import java.text.SimpleDateFormat
//import java.util.*
//
//@Composable
//fun AdminViewDetailProfileScreen(
//    navController: NavController,
//    taiKhoanId: Int,
//    viewModel: AdminViewDetailProfileViewModel = hiltViewModel()
//) {
//    val taiKhoanChiTiet by viewModel.taiKhoanChiTiet.collectAsState()
//    val chuyenMonList by viewModel.chuyenMonList.collectAsState()
//    val kyThuatVienChiTiet by viewModel.kyThuatVienChiTiet.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadTaiKhoanChiTiet(taiKhoanId)
//    }
//
//    ScaffoldLayout(title = "Thông tin tài khoản", navController = navController) { modifier ->
//        taiKhoanChiTiet?.let { taiKhoan ->
//            Column(
//                modifier = modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                // Ảnh đại diện giả định
//                Box(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .clip(CircleShape)
//                        .background(Color.LightGray),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(taiKhoan.hoTen?.firstOrNull()?.uppercase() ?: "?", style = MaterialTheme.typography.headlineMedium)
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text("Tên tài khoản: ${taiKhoan.tenTaiKhoan}")
//                Text("Họ tên: ${taiKhoan.hoTen ?: "Chưa cập nhật"}")
//                Text("Email: ${taiKhoan.email ?: "Chưa cập nhật"}")
//                Text("Số điện thoại: ${taiKhoan.soDienThoai ?: "Chưa cập nhật"}")
//                Text("Vai trò: ${taiKhoan.tenVaiTro}")
//                Text("Trạng thái: ${taiKhoan.trangThai}")
//
//                if (taiKhoan.tenVaiTro == "Quản Lý Đơn Vị") {
//                    Text("Đơn vị: ${taiKhoan.tenDonVi ?: "Không có"}")
//                }
//
//                if (taiKhoan.tenVaiTro == "Kỹ Thuật Viên") {
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Divider()
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("Chuyên môn:")
//                    chuyenMonList.forEach { Text("- $it") }
//
//                    kyThuatVienChiTiet?.let { ktv ->
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text("Ngày vào làm: ${ktv.ngayBatDauLam?.let { millis ->
//                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(millis))
//                        } ?: "Chưa cập nhật"}")
//                        Text("Số ngày làm việc: ${ktv.kinhNghiem ?: "Chưa rõ"}")
//                        Text("Ghi chú: ${ktv.ghiChu ?: "Không có"}")
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                when (taiKhoan.trangThai) {
//                    TrangThaiTaiKhoan.CHO_XAC_THUC -> {
//                        Button(onClick = {
//                            viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.NGOAI_TUYEN)
//                        }) {
//                            Text("Xác thực tài khoản")
//                        }
//                        Button(
//                            onClick = {
//                                viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, "TỪ_CHỐI")
//                            },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
//                        ) {
//                            Text("Từ chối tài khoản")
//                        }
//                    }
//                    TrangThaiTaiKhoan.NGOAI_TUYEN, TrangThaiTaiKhoan.TRUC_TUYEN -> {
//                        Button(
//                            onClick = {
//                                viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.BI_KHOA)
//                            },
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
//                        ) {
//                            Text("Khóa tài khoản")
//                        }
//                    }
//                    TrangThaiTaiKhoan.BI_KHOA -> {
//                        Button(onClick = {
//                            viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.NGOAI_TUYEN)
//                        }) {
//                            Text("Mở khóa tài khoản")
//                        }
//                    }
//                }
//            }
//        } ?: run {
//            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                CircularProgressIndicator()
//            }
//        }
//    }
//}
//
//
//
//
