package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "danh_gia_ktv")
data class DanhGiaKTV(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phanCongKtvId: Int,                // Liên kết với bản phân công cụ thể
    val nguoiDanhGiaId: Int,               // Ai đánh giá (thường là admin hoặc đơn vị yêu cầu)
    val diem: Int,                         // ⭐ Từ 1–5
    val nhanXet: String?,                  // Ghi chú, mô tả
    val thoiGian: Long = System.currentTimeMillis()
)
