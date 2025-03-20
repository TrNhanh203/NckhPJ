package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau

// 15. Bảng BaoCaoSuCo
@Entity(tableName = "yeu_cau")
data class YeuCau(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ngayYeuCau: Long = System.currentTimeMillis(),
    val taiKhoanId: Int,
    val trangThai: String = TrangThaiYeuCau.NHAP,
    val donViId: Int,
    val moTa: String = "" ,
    val lyDoTuChoi: String? = null
)