package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bai_viet")
data class BaiViet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tieuDe: String,
    val moTa: String,
    val anhDaiDien: String,   // đường dẫn ảnh (URL hoặc local asset name)
    val link: String?,        // đường dẫn tới bài viết chi tiết (nếu có)
    val noiDungHtml: String?, // nếu lưu toàn bộ nội dung trong app (option)
    val thoiGianTao: Long = System.currentTimeMillis(),
    val nguoiTaoId: Int? = null
)

