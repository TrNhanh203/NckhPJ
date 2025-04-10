package com.example.facilitiesmanagementpj.data.utils

import androidx.compose.ui.graphics.Color

object TrangThaiTaiKhoan {

    const val NGOAI_TUYEN = "Ngoại Tuyến"
    const val TRUC_TUYEN = "Trực Tuyến"
    const val BI_KHOA = "Bị Khóa"
    const val CHO_XAC_THUC = "Chờ Xác Thực"
    const val TU_CHOI_XAC_THUC = "Từ Chối Xác Thực" // tu choi xac thuc thi phai co ly do

    val ALL = listOf(NGOAI_TUYEN, TRUC_TUYEN, BI_KHOA, CHO_XAC_THUC, TU_CHOI_XAC_THUC)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }

    fun getColor(trangThai: String): Color {
        return when (trangThai) {
            TRUC_TUYEN -> Color(0xFF4CAF50)            // xanh lá
            NGOAI_TUYEN -> Color(0xFFBDBDBD)           // xám nhạt
            BI_KHOA -> Color(0xFFF44336)               // đỏ
            CHO_XAC_THUC -> Color(0xFF2196F3)          // xanh dương
            TU_CHOI_XAC_THUC -> Color(0xFFFF9800)      // cam
            else -> Color(0xFF9E9E9E)                  // fallback
        }
    }
}
