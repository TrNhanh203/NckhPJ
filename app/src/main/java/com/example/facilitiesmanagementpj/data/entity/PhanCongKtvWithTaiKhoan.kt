package com.example.facilitiesmanagementpj.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PhanCongKtvWithTaiKhoan(
    @Embedded val phanCongKtv: PhanCongKtv,

    @Relation(
        parentColumn = "taiKhoanKTVId",
        entityColumn = "id"
    )
    val taiKhoan: TaiKhoan
)
