package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 1. Bảng LoaiThietBi
@Entity(tableName = "loai_thiet_bi")
data class LoaiThietBi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenLoai: String
)