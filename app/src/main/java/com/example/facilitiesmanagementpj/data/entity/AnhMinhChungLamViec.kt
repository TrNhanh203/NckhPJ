package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 18. Bảng AnhMinhChungLamViec
@Entity(tableName = "anh_minh_chung_lam_viec")
data class AnhMinhChungLamViec(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phanCongKTVId: Int,
    val loaiAnh: String,
    val urlAnh: String,
    val type: String,
    val thoiGianTaiLen: Long = System.currentTimeMillis()
)