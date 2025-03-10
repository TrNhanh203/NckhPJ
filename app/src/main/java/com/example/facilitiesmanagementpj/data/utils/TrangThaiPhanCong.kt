package com.example.facilitiesmanagementpj.data.utils

object TrangThaiPhanCong {
    const val CHUA_BAT_DAU = "chua_bat_dau"
    const val DANG_THUC_HIEN = "dang_thuc_hien"
    const val HOAN_THANH = "hoan_thanh"
    const val BI_HUY = "bi_huy"
    const val TAM_NGHI = "tam_nghi"

    val ALL = listOf(CHUA_BAT_DAU, DANG_THUC_HIEN, HOAN_THANH, BI_HUY, TAM_NGHI)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
