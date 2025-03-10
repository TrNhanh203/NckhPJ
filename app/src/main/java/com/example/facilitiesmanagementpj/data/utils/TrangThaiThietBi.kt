package com.example.facilitiesmanagementpj.data.utils

object TrangThaiThietBi {
    // Nhóm trạng thái hoạt động bình thường
    const val MOI_TIEP_NHAN = "moi_tiep_nhan"
    const val SAN_SANG_SU_DUNG = "san_sang_su_dung"
    const val DANG_SU_DUNG = "dang_su_dung"

    // Nhóm trạng thái bảo trì & sửa chữa
    const val DANG_BAO_TRI = "dang_bao_tri"
    const val CHO_BAO_TRI = "cho_bao_tri"
    const val DANG_BAO_DUONG = "dang_bao_duong"
    const val CHO_BAO_DUONG = "cho_bao_duong"
    const val CHO_SUA_CHUA = "cho_sua_chua"
    const val DANG_SUA_CHUA = "dang_sua_chua"

    // Nhóm trạng thái hỏng & ngừng sử dụng
    const val HONG = "hong"
    const val KHONG_KHA_DUNG = "khong_kha_dung"
    const val DA_NGUNG_SU_DUNG = "da_ngung_su_dung"
    const val THANH_LY = "thanh_ly"

    // Nhóm trạng thái đặc biệt
    const val CHO_KIEM_DINH = "cho_kiem_dinh"
    const val DANG_KIEM_DINH = "dang_kiem_dinh"
    const val DANG_THU_NGHIEM = "dang_thu_nghiem"
    const val CHO_DUYET = "cho_duyet"

    // Nhóm trạng thái thất lạc & mất
    const val THAT_LAC = "that_lac"
    const val DANG_TIM_KIEM = "dang_tim_kiem"

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
