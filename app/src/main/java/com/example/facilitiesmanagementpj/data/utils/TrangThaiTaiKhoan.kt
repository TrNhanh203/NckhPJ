package com.example.facilitiesmanagementpj.data.utils

object TrangThaiTaiKhoan {
    const val NGOAI_TUYEN = "Ngoại Tuyến"
    const val TRUC_TUYEN = "Trực Tuyến"
    const val BI_KHOA = "Bị Khóa"
    const val CHO_XAC_THUC = "Chờ Xác Thực"
    const val TU_CHOI_XAC_THUC = "Từ Chối Xác Thực" //tu choi xac thuc thi phai co ly do

    //tu choi xac thuc thi phai co ly do chu

    val ALL = listOf(NGOAI_TUYEN, TRUC_TUYEN, BI_KHOA, CHO_XAC_THUC, TU_CHOI_XAC_THUC)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
