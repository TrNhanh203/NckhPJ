package com.example.facilitiesmanagementpj.data.entity



data class ChiTietYeuCauWithThietBiAndLoaiThietBi(
    val id: Int = 0,
    val yeuCauId: Int = 0,
    val thietBiId: Int? = null,
    val loaiYeuCau: String = "",
    val moTa: String = "",
    val loaiThietBiId: Int? = null,
    val tenLoaiThietBi: String? = null,
    val tenThietBi: String = ""
)
