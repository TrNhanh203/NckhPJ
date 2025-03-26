package com.example.facilitiesmanagementpj.data.relation

import com.example.facilitiesmanagementpj.data.entity.KyThuatVienWithTaiKhoan

data class KyThuatVienWithSoTask(
    val ktv: KyThuatVienWithTaiKhoan,
    val soTaskDangLam: Int
)
