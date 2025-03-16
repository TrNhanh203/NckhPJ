package com.example.facilitiesmanagementpj.data.utils

object TrangThaiYeuCau {
    const val NHAP = "nhap"
    const val CHO_XU_LY = "cho_xu_ly"
    const val DANG_XU_LY = "dang_xu_ly"
    const val DA_XU_LY = "da_xu_ly"
    const val DA_HUY = "da_huy"

    val ALL = listOf(NHAP, CHO_XU_LY, DANG_XU_LY, DA_XU_LY, DA_HUY)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
