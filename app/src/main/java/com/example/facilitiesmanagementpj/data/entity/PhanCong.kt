package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

// 12. Bảng PhanCong
@Entity(tableName = "phan_cong")
data class PhanCong(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val loaiPhanCong: String,
    val ghiChu: String?,
    val mucDoUuTien: Int = 1,
    val nguoiTaoPhanCong: Int?,
    val thoiGianTaoPhanCong: Long = System.currentTimeMillis()
)