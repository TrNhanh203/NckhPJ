package com.example.facilitiesmanagementpj.data.entity


data class PhongWithLoaiPhong(
    val phongId: Int,
    val tenPhong: String,
    val tangId: Int,
    val dayId: Int,
    val loaiPhongId: Int?,
    val donViId: Int?,
    val tenLoaiPhong: String? // ✅ Lấy tên loại phòng
)
