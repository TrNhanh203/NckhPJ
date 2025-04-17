package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bien_ban_yeu_cau",
    foreignKeys = [
        ForeignKey(entity = YeuCau::class,
            parentColumns = ["id"],
            childColumns = ["yeuCauId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class BienBanYeuCau(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val yeuCauId: Int = 0,
    val ngayLap: Long = System.currentTimeMillis(),
    val noiDung: String = "",
    val fileDinhKem: String? = null
)
