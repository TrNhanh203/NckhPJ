package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 7. Bảng TaiKhoan
@Entity(tableName = "tai_khoan")
data class TaiKhoan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenTaiKhoan: String,
    val matKhau: String,
    val vaiTroId: Int,
    val soDienThoai: String?,
    val email: String?,
    val hoTen: String?,
    val trangThai: String = "ngoai_tuyen",
    val lastLogin: Long? = null,
    val donViId: Int?
)
