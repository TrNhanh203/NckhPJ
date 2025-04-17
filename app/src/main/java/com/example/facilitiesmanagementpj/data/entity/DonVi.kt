package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "don_vi")
data class DonVi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenDonVi: String = "",
    val moTa: String? = null
)

