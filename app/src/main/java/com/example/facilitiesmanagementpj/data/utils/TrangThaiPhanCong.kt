package com.example.facilitiesmanagementpj.data.utils

object TrangThaiPhanCong {
    const val CHO_PHAN_HOI = "Chờ Phản Hồi"
    const val DA_CHAP_NHAN = "Đã Chấp Nhận"
    const val DA_TU_CHOI = "Đã Từ Chối"
    const val DANG_THUC_HIEN = "Đang Thực Hiện"
    const val TAM_NGHI = "Tạm Nghỉ"
    const val HOAN_THANH = "Hoàn Thành"
    const val THAY_NGUOI = "Thay Người" // chỉ áp dụng cho người làm giữa chừng thì bị thay
    const val BI_HUY = "Bị Hủy"
    const val KHONG_PHAN_HOI = "Không Phản Hồi"

    val ALL = listOf(
        CHO_PHAN_HOI,
        DA_CHAP_NHAN,
        DA_TU_CHOI,
        DANG_THUC_HIEN,
        TAM_NGHI,
        HOAN_THANH,
        THAY_NGUOI,
        BI_HUY,
        KHONG_PHAN_HOI
    )

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
