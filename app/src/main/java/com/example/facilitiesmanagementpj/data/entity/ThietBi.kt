package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 8. Bảng ThietBi
@Entity(tableName = "thiet_bi")
data class ThietBi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenThietBi: String = "",
    val loaiThietBiId: Int = 0,
    val phongId: Int? = null,
    val tangId: Int? = null,
    val trangThai: String = "Đang Hoạt Động",
    val ngayDaCat: Long? = null,
    val ngayDungSuDung: Long? = null,
    val ghiChu: String? = null,
    val ngayBaoDuongGanNhat: Long? = null,
    val ngayBaoDuongTiepTheo: Long? = null,
    val baoDuongDinhKy: Int? = null,
    val loaiBaoDuong: String? = null,
    val ghiChuBaoDuong: String? = null,
    val moTa: String? = null
)
