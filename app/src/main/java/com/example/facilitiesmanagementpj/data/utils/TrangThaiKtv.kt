package com.example.facilitiesmanagementpj.data.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object TrangThaiKtv {

    const val CHO_XAC_NHAN = "Chờ Xác Nhận"
    const val DA_THOI_VIEC = "Đã Thôi Việc"
    const val DANG_NGHI = "Đang Nghỉ"
    const val DANG_LAM_VIEC = "Đang Làm Việc"

    val ALL = listOf(DANG_NGHI, DANG_LAM_VIEC, DA_THOI_VIEC, CHO_XAC_NHAN)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }

    fun getColor(trangThai: String): Color {
        return when (trangThai) {
            DANG_LAM_VIEC -> Color(0xFF4CAF50)       // xanh lá
            DANG_NGHI -> Color(0xFFFFC107)            // vàng
            DA_THOI_VIEC -> Color(0xFF9E9E9E)         // xám
            CHO_XAC_NHAN -> Color(0xFF2196F3)         // xanh dương
            else -> Color(0xFFBDBDBD)                 // xám nhạt fallback
        }
    }
}


