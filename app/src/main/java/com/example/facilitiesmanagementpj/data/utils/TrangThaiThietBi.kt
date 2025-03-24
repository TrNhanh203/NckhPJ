package com.example.facilitiesmanagementpj.data.utils

object TrangThaiThietBi {
    // Nhóm trạng thái hoạt động bình thường
    const val MOI_TIEP_NHAN = "Mới Tiếp Nhận"
    const val SAN_SANG_SU_DUNG = "Sẵn Sàng Sử Dụng"
    const val DANG_SU_DUNG = "Đang Sử Dụng"

    // Nhóm trạng thái bảo trì & sửa chữa
    const val DANG_BAO_TRI = "Đang Bảo Trì"
    const val CHO_BAO_TRI = "Chờ Bảo Trì"
    const val DANG_BAO_DUONG = "Đang Bảo Dưỡng"
    const val CHO_BAO_DUONG = "Chờ Bảo Dưỡng"
    const val CHO_SUA_CHUA = "Chờ Sửa Chữa"
    const val DANG_SUA_CHUA = "Đang Sửa Chữa"

    // Nhóm trạng thái hỏng & ngừng sử dụng
    const val HONG = "Hỏng"
    const val KHONG_KHA_DUNG = "Không Khả Dụng"
    const val DA_NGUNG_SU_DUNG = "Đã Ngừng Sử Dụng"
    const val THANH_LY = "Thanh Lý"

    // Nhóm trạng thái đặc biệt
    const val CHO_KIEM_DINH = "Chờ Kiểm Định"
    const val DANG_KIEM_DINH = "Đang Kiểm Định"
    const val DANG_THU_NGHIEM = "Đang Thử Nghiệm"
    const val CHO_DUYET = "Chờ Duyệt"

    // Nhóm trạng thái thất lạc & mất
    const val THAT_LAC = "Thất Lạc"
    const val DANG_TIM_KIEM = "Đang Tìm Kiếm"


    // Danh sách tất cả các trạng thái
    val ALL = listOf(
        MOI_TIEP_NHAN, SAN_SANG_SU_DUNG, DANG_SU_DUNG,
        DANG_BAO_TRI, CHO_BAO_TRI, DANG_BAO_DUONG, CHO_BAO_DUONG, CHO_SUA_CHUA, DANG_SUA_CHUA,
        HONG, KHONG_KHA_DUNG, DA_NGUNG_SU_DUNG, THANH_LY,
        CHO_KIEM_DINH, DANG_KIEM_DINH, DANG_THU_NGHIEM, CHO_DUYET,
        THAT_LAC, DANG_TIM_KIEM
    )

    // Kiểm tra trạng thái hợp lệ
    fun isValid(trangThai: String): Boolean {
        return trangThai in ALL
    }
}
