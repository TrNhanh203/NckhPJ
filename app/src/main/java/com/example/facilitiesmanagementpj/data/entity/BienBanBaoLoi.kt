package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "bien_ban_bao_loi",
    foreignKeys = [
        ForeignKey(entity = BaoCaoSuCo::class,
            parentColumns = ["id"],
            childColumns = ["baoCaoId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class BienBanBaoLoi(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val baoCaoId: Int,  // ✅ Mỗi biên bản gắn với 1 báo cáo sự cố
    val ngayLap: Long,   // ✅ Thời gian lập biên bản (timestamp)
    val noiDung: String, // ✅ Mô tả nội dung biên bản
    val fileDinhKem: String? // ✅ Lưu đường dẫn file đính kèm (PDF, ảnh)
)
