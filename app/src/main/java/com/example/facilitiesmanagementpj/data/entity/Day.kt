package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 3. Bảng Day
@Entity(tableName = "day")
data class Day(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenDay: String
)