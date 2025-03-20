package com.example.facilitiesmanagementpj.data.utils


object LoaiYeuCau {
    // ✅ Nhóm yêu cầu định kỳ
    const val BAO_DUONG = "bao_duong"
    const val KIEM_TRA = "kiem_tra"

    // ✅ Nhóm yêu cầu sửa chữa & thay thế
    const val SUA_CHUA = "sua_chua"
    const val THAY_THE = "thay_the"

    // ✅ Nhóm yêu cầu liên quan đến lắp đặt & tháo dỡ
    const val LAP_DAT = "lap_dat"
    const val THAO_DO = "thao_do"

    // ✅ Nhóm yêu cầu nâng cấp & cải tiến
    const val NANG_CAP = "nang_cap"
    const val CAI_TIEN = "cai_tien"

    // ✅ Nhóm yêu cầu đặc biệt khác
    const val KHAC = "khac"

    // ✅ Danh sách tất cả loại yêu cầu (tiện lợi khi hiển thị Dropdown)
    val ALL = listOf(
        BAO_DUONG, KIEM_TRA, SUA_CHUA, THAY_THE
        , THAO_DO, NANG_CAP, CAI_TIEN, KHAC
    )
}
