package com.example.facilitiesmanagementpj.data.utils

object TrangThaiYeuCau {
    const val NHAP = "nhap"
    const val CHO_XAC_NHAN = "cho_xac_nhan"
    const val DA_XAC_NHAN = "da_xac_nhan"
    const val DANG_XU_LY = "dang_xu_ly"
    const val DA_XU_LY = "da_xu_ly"
    const val TU_CHOI = "tu_choi"
    const val DA_HUY = "da_huy"

    val ALL = listOf(NHAP, CHO_XAC_NHAN, DA_XAC_NHAN, DANG_XU_LY, DA_XU_LY, TU_CHOI, DA_HUY)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
