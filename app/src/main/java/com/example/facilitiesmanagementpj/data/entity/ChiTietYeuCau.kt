package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

// 16. Bảng ChiTietBaoCao
@Entity(tableName = "chi_tiet_yeu_cau")
data class ChiTietYeuCau(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val yeuCauId: Int,
    val thietBiId: Int?,
    val loaiYeuCau: String, // ✅ Lắp đặt, bảo trì, sửa chữa...
    val moTa: String
)