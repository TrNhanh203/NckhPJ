package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Embedded
import androidx.room.Relation

// ✅ DATA CLASS (DTO)
data class KyThuatVienWithTaiKhoan(
    @Embedded val kyThuatVien: KyThuatVien,

    @Relation(
        parentColumn = "taiKhoanId",
        entityColumn = "id"
    )
    val taiKhoan: TaiKhoan
)
