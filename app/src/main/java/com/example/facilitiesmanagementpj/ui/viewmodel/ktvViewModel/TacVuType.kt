package com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel

import com.example.facilitiesmanagementpj.data.utils.LoaiAnhMinhChungLamViec

enum class LoaiTacVu(
    val tieuDe: String,
    val buttonLabel: String,
    val loaiAnh: String,
    val canNhapSoPhut: Boolean = false
) {
    CHECK_IN("Check-in", "Check-In", LoaiAnhMinhChungLamViec.CHECK_IN),
    XIN_GIA_HAN("Xin gia hạn", "Gửi yêu cầu", LoaiAnhMinhChungLamViec.XIN_GIA_HAN, canNhapSoPhut = true),
    TAM_NGHI("Tạm nghỉ", "Xác nhận", LoaiAnhMinhChungLamViec.TAM_NGHI),
    CHECK_OUT("Hoàn thành", "Check-Out", LoaiAnhMinhChungLamViec.CHECK_OUT)
}
