package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

// 16. Bảng ChiTietBaoCao
@Entity(tableName = "chi_tiet_bao_cao")
data class ChiTietBaoCao(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val baoCaoId: Int,
    val thietBiId: Int,
    val moTa: String
)