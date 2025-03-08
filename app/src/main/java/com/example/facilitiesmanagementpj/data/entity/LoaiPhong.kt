package com.example.facilitiesmanagementpj.data.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

// 2. Bảng LoaiPhong
@Entity(tableName = "loai_phong")
data class LoaiPhong(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenLoaiPhong: String
)