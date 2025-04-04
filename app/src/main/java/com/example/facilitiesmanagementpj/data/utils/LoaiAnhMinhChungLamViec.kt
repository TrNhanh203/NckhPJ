package com.example.facilitiesmanagementpj.data.utils

object LoaiAnhMinhChungLamViec {
    const val CHECK_IN = "CHECKIN"
    const val TAM_NGHI = "TAMNGHI"
    const val XIN_GIA_HAN = "GIAHAN" // CẦN DUYỆT
    const val CHECK_OUT = "CHECKOUT" // CẦN DUYỆT
    const val MINH_CHUNG = "MINHCHUNG"


    val ALL = listOf(
        CHECK_IN, TAM_NGHI, XIN_GIA_HAN, CHECK_OUT, MINH_CHUNG
    )
}