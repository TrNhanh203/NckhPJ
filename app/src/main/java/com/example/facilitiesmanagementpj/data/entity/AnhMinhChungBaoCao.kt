package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey


// 17. Bảng AnhMinhChungBaoCao
@Entity(tableName = "anh_minh_chung_bao_cao")
data class AnhMinhChungBaoCao(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val chiTietBaoCaoId: Int = 0,
    val urlAnh: String = "",
    val type: String = "",
    val thoiGianTaiLen: Long = System.currentTimeMillis()
)
