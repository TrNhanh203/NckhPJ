package com.example.facilitiesmanagementpj.data.utils

object TrangThaiKtv {

    const val DANG_NGHI = "Đang nghỉ"
    const val DANG_LAM_VIEC = "Đang làm việc"


    val ALL = listOf(DANG_NGHI, DANG_LAM_VIEC)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}

