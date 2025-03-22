package com.example.facilitiesmanagementpj.data.utils

object TrangThaiYeuCau {
    const val NHAP = "Bản Nháp"
    const val CHO_XAC_NHAN = "Chờ Xác Nhận"
    const val DA_XAC_NHAN = "Đã Xác Nhận"
    const val DANG_XU_LY = "Đang Xử Lý"
    const val DA_XU_LY = "Đã Xử Lý"
    const val TU_CHOI = "Đã Từ Chối"
    const val DA_HUY = "Đã Hủy Bỏ"

    val ALL = listOf(NHAP, CHO_XAC_NHAN, DA_XAC_NHAN, DANG_XU_LY, DA_XU_LY, TU_CHOI, DA_HUY)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
