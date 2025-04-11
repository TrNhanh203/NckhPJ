package com.example.facilitiesmanagementpj.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong

@Composable
fun getTrangThaiPcKtvColor(trangThai: String): Color {
    return when (trangThai) {
        TrangThaiPhanCong.CHO_PHAN_HOI -> Color(0xFFFFC107) // vàng
        TrangThaiPhanCong.DA_CHAP_NHAN -> Color(0xFF4CAF50) // xanh lá
        TrangThaiPhanCong.DANG_THUC_HIEN -> Color(0xFF2196F3) // xanh dương
        TrangThaiPhanCong.TAM_NGHI -> Color(0xFFFF9800) // cam
        TrangThaiPhanCong.HOAN_THANH -> Color(0xFF388E3C) // xanh đậm
        TrangThaiPhanCong.DA_TU_CHOI -> Color(0xFFF44336) // đỏ
        TrangThaiPhanCong.BI_HUY -> Color(0xFF616161) // xám đậm
        else -> Color.Gray
    }
}
