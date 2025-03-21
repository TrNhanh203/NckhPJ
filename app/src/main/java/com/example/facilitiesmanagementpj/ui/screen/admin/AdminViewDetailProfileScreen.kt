package com.example.facilitiesmanagementpj.ui.screen.admin


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan
import com.example.facilitiesmanagementpj.ui.viewmodel.AdminViewDetailProfileViewModel
import kotlinx.coroutines.launch


@Composable
fun AdminViewDetailProfileScreen(navController: NavController, taiKhoanId: Int, viewModel: AdminViewDetailProfileViewModel = hiltViewModel()) {
    val taiKhoanChiTiet by viewModel.taiKhoanChiTiet.collectAsState()
    val chuyenMonList by viewModel.chuyenMonList.collectAsState()
    val kyThuatVienChiTiet by viewModel.kyThuatVienChiTiet.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTaiKhoanChiTiet(taiKhoanId)
    }

    taiKhoanChiTiet?.let { taiKhoan ->
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Chi tiết tài khoản", style = MaterialTheme.typography.headlineMedium)

            Text("Tên tài khoản: ${taiKhoan.tenTaiKhoan}")
            Text("Họ tên: ${taiKhoan.hoTen ?: "Chưa cập nhật"}")
            Text("Email: ${taiKhoan.email ?: "Chưa cập nhật"}")
            Text("Số điện thoại: ${taiKhoan.soDienThoai ?: "Chưa cập nhật"}")
            Text("Vai trò: ${taiKhoan.tenVaiTro}")
            Text("Trạng thái: ${taiKhoan.trangThai}")

            if (taiKhoan.tenVaiTro == "Quản Lý Đơn Vị") {
                Text("Đơn vị: ${taiKhoan.tenDonVi ?: "Không có"}")
            }

            if (taiKhoan.tenVaiTro == "Kỹ Thuật Viên") {
                Text("Chuyên môn:")
                chuyenMonList.forEach {
                    Text("- $it")
                }


//                kyThuatVienChiTiet?.let { ktv ->
//                    Text("Số năm kinh nghiệm: ${ktv.kinhNghiem}")
//                    Text("Ngày vào làm: ${ktv.ngayBatDauLam ?: "Chưa cập nhật"}")
//                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (taiKhoan.trangThai) {
                TrangThaiTaiKhoan.CHO_XAC_THUC -> {
                    Button(onClick = { viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.NGOAI_TUYEN) }) {
                        Text("Xác thực tài khoản")
                    }
                    Button(onClick = { viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, "TỪ_CHỐI") }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("Từ chối tài khoản")
                    }
                }
                TrangThaiTaiKhoan.NGOAI_TUYEN -> {
                    Button(onClick = { viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.BI_KHOA) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("Khóa tài khoản")
                    }
                }
                TrangThaiTaiKhoan.TRUC_TUYEN -> {
                    Button(onClick = { viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.BI_KHOA) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("Khóa tài khoản")
                    }
                }
                TrangThaiTaiKhoan.BI_KHOA -> {
                    Button(onClick = { viewModel.updateTrangThaiTaiKhoan(taiKhoan.id, TrangThaiTaiKhoan.NGOAI_TUYEN) }) {
                        Text("Mở khóa tài khoản")
                    }
                }
            }
        }
    } ?: run {
        Text("Đang tải dữ liệu...", modifier = Modifier.fillMaxSize().padding(16.dp))
    }
}
