package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// 4. Bảng Tang
@Entity(
    tableName = "tang",
    foreignKeys = [
        ForeignKey(
            entity = Day::class,
            parentColumns = ["id"],
            childColumns = ["dayId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["dayId"])]
)
data class Tang(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tenTang: String,
    val dayId: Int
)