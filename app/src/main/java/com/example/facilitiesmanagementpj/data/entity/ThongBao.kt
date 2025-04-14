package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "thong_bao")
data class ThongBao(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val nguoiNhanId: Int,
    val tieuDe: String,
    val noiDung: String,
    val loai: String = "he_thong",
    val thoiGian: Long = System.currentTimeMillis(),
    val daDoc: Boolean = false
)
