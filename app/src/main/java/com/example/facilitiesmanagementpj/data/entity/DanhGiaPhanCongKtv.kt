package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "danh_gia_ktv")
data class DanhGiaKTV(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phanCongKtvId: Int = 0,
    val nguoiDanhGiaId: Int = 0,
    val diem: Int = 0,
    val nhanXet: String? = null,
    val thoiGian: Long = System.currentTimeMillis()
)

