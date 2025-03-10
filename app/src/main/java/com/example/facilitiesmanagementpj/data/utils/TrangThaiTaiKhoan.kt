package com.example.facilitiesmanagementpj.data.utils

object TrangThaiTaiKhoan {
    const val NGOAI_TUYEN = "ngoai_tuyen"
    const val TRUC_TUYEN = "truc_tuyen"
    const val BI_KHOA = "bi_khoa"
    const val CHO_XAC_THUC = "cho_xac_thuc"

    val ALL = listOf(NGOAI_TUYEN, TRUC_TUYEN, BI_KHOA, CHO_XAC_THUC)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
