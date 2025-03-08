package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 11. Bảng ChuyenMonKyThuatVien
@Entity(tableName = "chuyen_mon_ky_thuat_vien")
data class ChuyenMonKtv(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chuyenMonId: Int,
    val kyThuatVienId: Int
)