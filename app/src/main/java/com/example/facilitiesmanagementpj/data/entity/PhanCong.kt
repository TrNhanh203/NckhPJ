package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

// 12. Bảng PhanCong
@Entity(tableName = "phan_cong")
data class PhanCong(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chiTietYeuCauId: Int = 0,
    val thietBiId: Int = 0,
    val loaiPhanCong: String = "",
    val ghiChu: String? = null,
    val mucDoUuTien: Int = 1,
    val nguoiTaoPhanCong: Int? = null,
    val thoiGianTaoPhanCong: Long = System.currentTimeMillis(),
    val trangThai: String = "",
    val soLuongKTVThamGia: Int? = null
)
