package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey


// 10. Bảng KyThuatVien
@Entity(tableName = "ky_thuat_vien")
data class KyThuatVien(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taiKhoanId: Int = 0,
    val kinhNghiem: Int? = null,
    val ngayBatDauLam: Long? = null,
    val trangThaiHienTai: String = "Đang Nghỉ",
    val ghiChu: String? = null
)
