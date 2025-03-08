package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 6. Bảng VaiTro
@Entity(tableName = "vai_tro")
data class VaiTro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenVaiTro: String
)