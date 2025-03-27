package com.example.facilitiesmanagementpj.data.relation

// DTO mới theo dạng lồng nhau với @Relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.facilitiesmanagementpj.data.entity.*

// Thông tin kỹ thuật viên với phân công và thiết bị (gồm cả loại thiết bị)
data class PhanCongKtvWithFullInfo(
    @Embedded val phanCongKtv: PhanCongKtv,

    @Relation(
        parentColumn = "phanCongId",
        entityColumn = "id",
        entity = PhanCong::class
    )
    val phanCong: PhanCongWithThietBiAndLoai
)

// Phân công với thiết bị và loại thiết bị

data class PhanCongWithThietBiAndLoai(
    @Embedded val phanCong: PhanCong,

    @Relation(
        parentColumn = "thietBiId",
        entityColumn = "id",
        entity = ThietBi::class
    )
    val thietBi: ThietBiWithLoai
)

// Thiết bị với loại thiết bị

data class ThietBiWithLoai(
    @Embedded val thietBi: ThietBi,

    @Relation(
        parentColumn = "loaiThietBiId",
        entityColumn = "id"
    )
    val loaiThietBi: LoaiThietBi?
)

