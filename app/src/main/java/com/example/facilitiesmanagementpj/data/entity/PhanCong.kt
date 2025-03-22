package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

// 12. Bảng PhanCong
@Entity(tableName = "phan_cong")
data class PhanCong(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chiTietYeuCauId: Int, // ✅ Liên kết với yêu cầu
    val thietBiId: Int, // ✅ Thiết bị mà phân công này làm việc trên
    val loaiPhanCong: String,// Loại phân công: sửa chữa, kiểm tra, thay thế...
    val ghiChu: String?,// Ghi chú nội dung tổng thể của phân công
    val mucDoUuTien: Int = 1,// Mức độ ưu tiên (1 là cao nhất)
    val nguoiTaoPhanCong: Int?,// ID tài khoản admin tạo
    val thoiGianTaoPhanCong: Long = System.currentTimeMillis(),// Thời điểm tạo phân công


     val trangThai: String,// Trạng thái tổng hợp (chưa bắt đầu / đang thực hiện / hoàn thành / bị huỷ)
     val soLuongKTVThamGia: Int? // Số lượng KTV được phân và chấp nhận


)