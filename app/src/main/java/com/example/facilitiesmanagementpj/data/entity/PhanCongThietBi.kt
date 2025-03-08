package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey


// 13. Bảng PhanCongThietBi
@Entity(tableName = "phan_cong_thiet_bi")
data class PhanCongThietBi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phanCongId: Int,
    val thietBiId: Int,
    val thuTuThucHien: Int = 1
)