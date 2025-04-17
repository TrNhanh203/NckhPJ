package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau



//@Entity(tableName = "phan_cong_ktv")
//data class PhanCongKtv(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val phanCongId: Int,
//    val taiKhoanKTVId: Int,
//    val trangThai: String = TrangThaiPhanCong.CHO_PHAN_HOI, // cập nhật dựa trên các tương tác của ktv
//
//    val thoiGianDuKien: Int, // Phút, do admin quyết định khi phân công
//    val thoiGianPhatSinh: Int = 0, // Thời gian phát sinh thêm
//    val thoiGianBatDau: Long? = null, // Thời gian thực tế bắt đầu, được tính khi KTV bắt đầu làm việc này, sửa lại cho phép null vì đợi tới khi ktv check in mới có tgian
//    val thoiGianHoanThien: Long? = null,   // Thời gian thực tế hoàn thành, được tính khi KTV hoàn thành công việc này
//
//    val dangXinGiaHan: Boolean = false, // KTV đang xin gia hạn công việc này hay không
//    val soThoiGianXinGiaHan: Int = 0, // Số phút KTV xin gia hạn (reset lại nếu admin duyệt)
//    val soLanGiaHan: Int = 0,// +1 mỗi lần nhấn xin gia hạn
//    val tongThoiGianDaXinGiaHan: Int = 0, // Tổng cộng tất cả thời gian đã xin gia hạn thêm (phút)
//
//    val moTaCongViec: String? = null, // Mô tả công việc riêng cho KTV
//    val daChapNhan: Boolean = false, // KTV đã chấp nhận phân công hay chưa
//    val thoiGianTuChoi: Long? = null, // Nếu từ chối, ghi thời điểm
//    val lyDoTuChoi: String?, // Ghi rõ lý do từ chối
//    val thoiGianLamViecThucTe: Int = 0, // Thời gian thực tế KTV làm việc (số phút)
//    val trangThaiCuoiCung: String? = null, // Trạng thái kết thúc để thống kê (nếu cần)
//
//
//)
@Entity(tableName = "phan_cong_ktv")
data class PhanCongKtv(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val phanCongId: Int = 0,
    val taiKhoanKTVId: Int = 0,
    val trangThai: String = "CHO_PHAN_HOI",
    val thoiGianDuKien: Int = 0,
    val thoiGianPhatSinh: Int = 0,
    val thoiGianBatDau: Long? = null,
    val thoiGianHoanThien: Long? = null,
    val dangXinGiaHan: Boolean = false,
    val soThoiGianXinGiaHan: Int = 0,
    val soLanGiaHan: Int = 0,
    val tongThoiGianDaXinGiaHan: Int = 0,
    val moTaCongViec: String? = null,
    val daChapNhan: Boolean = false,
    val thoiGianTuChoi: Long? = null,
    val lyDoTuChoi: String? = null,
    val thoiGianLamViecThucTe: Int = 0,
    val trangThaiCuoiCung: String? = null
)

