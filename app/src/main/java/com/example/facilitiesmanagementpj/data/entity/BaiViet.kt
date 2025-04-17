package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "bai_viet")
data class BaiViet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tieuDe: String = "",
    val moTa: String = "",
    val anhDaiDien: String = "",
    val link: String? = null,
    val noiDungHtml: String? = null,
    val thoiGianTao: Long = System.currentTimeMillis(),
    val nguoiTaoId: Int? = null
)





