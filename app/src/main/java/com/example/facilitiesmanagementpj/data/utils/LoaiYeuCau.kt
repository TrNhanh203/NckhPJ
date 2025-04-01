package com.example.facilitiesmanagementpj.data.utils


object LoaiYeuCau {
    // ✅ Nhóm yêu cầu định kỳ
    const val BAO_DUONG = "Bảo Dưỡng"
    const val KIEM_TRA = "Kiểm Tra"

    // ✅ Nhóm yêu cầu sửa chữa & thay thế
    const val SUA_CHUA = "Sửa Chữa"
    //const val THAY_THE = "Thay Thế"

    // ✅ Nhóm yêu cầu liên quan đến lắp đặt & tháo dỡ
    const val LAP_DAT = "Lắp Đặt"
    //const val THAO_DO = "Tháo Dỡ"

    // ✅ Nhóm yêu cầu nâng cấp & cải tiến
    const val NANG_CAP = "Nâng Cấp"

    // ✅ Nhóm yêu cầu đặc biệt khác
    const val KHAC = "Khác"

    // ✅ Danh sách tất cả loại yêu cầu (tiện lợi khi hiển thị Dropdown)
    val ALL = listOf(
        BAO_DUONG, KIEM_TRA, SUA_CHUA
        ,NANG_CAP, KHAC
    )
}
