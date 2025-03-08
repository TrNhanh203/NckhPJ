package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 8. Bảng ThietBi
@Entity(tableName = "thiet_bi")
data class ThietBi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenThietBi: String,
    val loaiThietBiId: Int,
    val phongId: Int?,
    val tangId: Int?,
    val trangThai: String = "binh_thuong",
    val ngayDaCat: Long? = null,
    val ngayDungSuDung: Long? = null,
    val ghiChu: String? = null,
    val donViId: Int,
    val ngayBaoDuongGanNhat: Long?, // ✅ Ngày thực hiện bảo dưỡng gần nhất
    val ngayBaoDuongTiepTheo: Long?, // ✅ Ngày dự kiến bảo dưỡng tiếp theo
    val baoDuongDinhKy: Int?, // ✅ Số ngày giữa các lần bảo dưỡng
    val loaiBaoDuong: String?, // ✅ Kiểu bảo dưỡng (VD: "Vệ sinh", "Thay linh kiện")
    val ghiChuBaoDuong: String? // ✅ Ghi chú về bảo dưỡng
)