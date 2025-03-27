package com.example.facilitiesmanagementpj.data.relation
import com.example.facilitiesmanagementpj.data.entity.KyThuatVienWithTaiKhoan

data class KyThuatVienWithSoTaskWithTrangThaiPhanCong(
    val ktv: KyThuatVienWithTaiKhoan,
    val soTaskDangLam: Int,
    val trangThaiPhanCong: String? = null
)
