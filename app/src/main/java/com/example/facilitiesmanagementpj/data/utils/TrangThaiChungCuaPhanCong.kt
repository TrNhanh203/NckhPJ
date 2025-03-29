package com.example.facilitiesmanagementpj.data.utils

object TrangThaiChungCuaPhanCong {

    const val CHUA_BAT_DAU = "Chưa bắt đầu"
    const val DANG_THUC_HIEN = "Đang thực hiện"
    const val DANG_TAM_NGHI = "Đang tạm nghỉ"
    const val HOAN_THANH = "Hoàn thành"
    const val NGHIEM_THU = "Nghiệm thu"
    const val BI_HUY = "Bị Hủy"


    val ALL = listOf(
        CHUA_BAT_DAU,
        DANG_THUC_HIEN,
        DANG_TAM_NGHI,
        HOAN_THANH,
        NGHIEM_THU,
        BI_HUY
    )

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}