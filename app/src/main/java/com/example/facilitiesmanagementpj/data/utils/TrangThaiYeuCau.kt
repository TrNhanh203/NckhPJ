package com.example.facilitiesmanagementpj.data.utils

object TrangThaiYeuCau {
    const val NHAP = "Bản Nháp"
    const val CHO_XAC_NHAN = "Chờ Xác Nhận"
    const val DA_XAC_NHAN = "Đã Xác Nhận"
    const val DANG_XU_LY = "Đang Xử Lý" // Khi một trong các chi tiết yêu cầu đó đã có tạo phân công
    const val DA_XU_LY = "Đã Xử Lý" // khi tất cả chi tiết của yêu cầu đã có phân công - và tất cả phân công đó đều hoàn thành
    const val TU_CHOI = "Đã Từ Chối"
    const val DA_HUY = "Đã Hủy Bỏ"
    const val DA_NGHIEM_THU = "Đã Nghiệm Thu" // Khi tất cả các chi tiết yêu cầu đều đã được nghiệm thu

    val ALL = listOf(NHAP, CHO_XAC_NHAN, DA_XAC_NHAN, DANG_XU_LY, DA_XU_LY, TU_CHOI, DA_HUY)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
