package com.example.facilitiesmanagementpj.data.relation

// File: model/PhanCongThongTin.kt

data class PhanCongThongTin(
    val tenBenA: String,
    val donViBenA: String,
    val tenBenB: String,
    val viTri: String,
    val danhSachThietBi: List<ThietBiDaSua>
)

data class ThietBiDaSua(
    val tenThietBi: String,
    val viTri: String,
    val noiDung: String
)
