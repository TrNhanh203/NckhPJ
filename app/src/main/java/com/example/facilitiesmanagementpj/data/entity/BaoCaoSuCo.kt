package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 15. Bảng BaoCaoSuCo
@Entity(tableName = "bao_cao_su_co")
data class BaoCaoSuCo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ngayBaoCao: Long = System.currentTimeMillis(),
    val taiKhoanId: Int,
    val trangThai: String = "cho_xu_ly",
    val donViId: Int
)