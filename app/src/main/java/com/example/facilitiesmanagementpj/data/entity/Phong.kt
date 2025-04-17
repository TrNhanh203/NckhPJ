package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
// 5. Bảng Phong
//@Entity(tableName = "phong")
//data class Phong(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//    val tenPhong: String,
//    val tangId: Int,
//    val dayId: Int,
//    val loaiPhongId: Int?,
//    val donViId: Int?
//)
@Entity(tableName = "phong")
data class Phong(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenPhong: String = "",
    val tangId: Int = 0,
    val dayId: Int = 0,
    val loaiPhongId: Int? = null,
    val donViId: Int? = null
)
