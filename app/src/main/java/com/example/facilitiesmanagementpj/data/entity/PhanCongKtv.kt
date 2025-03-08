package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey


// 14. Bảng PhanCongKyThuatVien
@Entity(tableName = "phan_cong_ktv")
data class PhanCongKtv(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phanCongId: Int,
    val taiKhoanKTVId: Int,
    val thoiGianDuKien: Int, // phút
    val thoiGianBatDau: Long = System.currentTimeMillis(),
    val thoiGianHoanThien: Long? = null,
    val soLanGiaHan: Int = 0,
    val thoiGianGiaHan: Long? = null,
    val lyDoGiaHan: String? = null,
    val trangThai: String = "chua_bat_dau",
    val trangThaiCuoiCung: String? = null
)