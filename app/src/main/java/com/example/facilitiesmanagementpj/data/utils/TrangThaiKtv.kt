package com.example.facilitiesmanagementpj.data.utils

object TrangThaiKtv {

    const val DANG_NGHI = "Đang Nghỉ"
    const val DANG_LAM_VIEC = "Đang Làm Việc"


    val ALL = listOf(DANG_NGHI, DANG_LAM_VIEC)

    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}

