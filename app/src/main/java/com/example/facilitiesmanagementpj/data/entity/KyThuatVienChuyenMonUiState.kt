package com.example.facilitiesmanagementpj.data.entity

data class KyThuatVienChuyenMonUiState(
    val taiKhoan: TaiKhoan? = null,
    val kyThuatVien: KyThuatVien? = null,
    val chuyenMonItems: List<ChuyenMonCheckItem> = emptyList(),
    val isLoading: Boolean = true
)
