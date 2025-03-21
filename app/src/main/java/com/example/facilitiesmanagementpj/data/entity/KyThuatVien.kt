package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey


// 10. Bảng KyThuatVien
@Entity(tableName = "ky_thuat_vien")
data class KyThuatVien(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taiKhoanId: Int,
    val kinhNghiem: Int?, // Số ngày kinh nghiệm làm việc ở nơi này (k phải số năm trong nghề)
    val ngayBatDauLam: Long?,
    val trangThaiHienTai: String = "Đang làm", // Trạng thái hiện tại
    val ghiChu: String? // Ghi chú thêm
)