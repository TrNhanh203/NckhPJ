package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 18. Bảng AnhMinhChungLamViec
@Entity(tableName = "anh_minh_chung_lam_viec")
data class AnhMinhChungLamViec(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phanCongKTVId: Int,
    val loaiAnh: String, // Loại ảnh: check-in, check-out, tạm nghỉ, minh chứng
    val urlAnh: String,
    val type: String, // Kiểu image, video
    val thoiGianTaiLen: Long = System.currentTimeMillis(),


     val ghiChu: String? = null // Ghi chú nội dung ảnh (nếu cần)

)