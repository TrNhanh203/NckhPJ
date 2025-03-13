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
    @PrimaryKey(autoGenerate = true) val id: Int,
    val yeuCauId: Int,  // ✅ Mỗi biên bản gắn với 1 báo cáo sự cố
    val ngayLap: Long,   // ✅ Thời gian lập biên bản (timestamp)
    val noiDung: String, // ✅ Mô tả nội dung biên bản
    val fileDinhKem: String? // ✅ Lưu đường dẫn file đính kèm (PDF, ảnh)
)
