package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

// 16. Bảng ChiTietBaoCao
@Entity(tableName = "chi_tiet_yeu_cau")
data class ChiTietYeuCau(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val yeuCauId: Int = 0,
    val thietBiId: Int? = null,
    val loaiYeuCau: String = "",
    val moTa: String = ""
)
