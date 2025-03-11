package com.example.facilitiesmanagementpj.data.entity


data class PhongWithDetails(
    val phongId: Int,
    val tenPhong: String,
    val tenLoaiPhong: String?, // Tên loại phòng (có thể null)
    val tenDonVi: String? // Tên đơn vị quản lý (có thể null)
)
