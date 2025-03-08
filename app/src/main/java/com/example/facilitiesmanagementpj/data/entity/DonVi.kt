package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "don_vi")
data class DonVi(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val tenDonVi: String,
    val moTa: String?
)
