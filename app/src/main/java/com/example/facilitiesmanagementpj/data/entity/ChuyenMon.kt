package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey


// 9. Bảng ChuyenMon
@Entity(tableName = "chuyen_mon")
data class ChuyenMon(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenChuyenMon: String,
    val moTa: String?
)