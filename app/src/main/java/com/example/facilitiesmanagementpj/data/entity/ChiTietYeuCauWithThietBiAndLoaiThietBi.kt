package com.example.facilitiesmanagementpj.data.entity



data class ChiTietYeuCauWithThietBiAndLoaiThietBi(
    val id: Int,
    val yeuCauId: Int,
    val thietBiId: Int?,
    val loaiYeuCau: String, // ✅ Lắp đặt, bảo trì, sửa chữa...
    val moTa: String,
    val loaiThietBiId: Int?,
    val tenLoaiThietBi: String?,
    val tenThietBi: String
)