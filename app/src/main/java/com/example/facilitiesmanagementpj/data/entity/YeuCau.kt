package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau

// 15. Bảng BaoCaoSuCo
@Entity(tableName = "yeu_cau")
data class YeuCau(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ngayYeuCau: Long = System.currentTimeMillis(),
    val taiKhoanId: Int = 0,
    val trangThai: String = "NHAP",
    val donViId: Int = 0,
    val moTa: String = "",
    val lyDoTuChoi: String? = null
)
