package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.TaiKhoan
import com.example.facilitiesmanagementpj.data.entity.DonVi
import com.example.facilitiesmanagementpj.data.entity.KyThuatVien
import com.example.facilitiesmanagementpj.data.repository.KyThuatVienRepository
import com.example.facilitiesmanagementpj.data.repository.TaiKhoanRepository
import com.example.facilitiesmanagementpj.data.utils.TrangThaiKtv
import com.example.facilitiesmanagementpj.data.utils.TrangThaiTaiKhoan
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: TaiKhoanRepository,
    private val kyThuatVienRepo: KyThuatVienRepository
) : ViewModel() {

    private val _registerState = MutableStateFlow<String?>(null)
    val registerState: StateFlow<String?> = _registerState

    private val _donViList = MutableStateFlow<List<DonVi>>(emptyList())
    val donViList: StateFlow<List<DonVi>> = _donViList

    init {
        viewModelScope.launch {
            repository.getAllDonVi().collectLatest { _donViList.value = it }
        }
    }

    fun register(
        tenTaiKhoan: String,
        matKhau: String,
        hoTen: String?,
        email: String?,
        soDienThoai: String?,
        vaiTroId: Int,
        donViId: Int?
    ) {
        viewModelScope.launch {
            try {
                val taiKhoan = TaiKhoan(
                    tenTaiKhoan = tenTaiKhoan,
                    matKhau = matKhau,
                    hoTen = hoTen,
                    email = email,
                    soDienThoai = soDienThoai,
                    vaiTroId = vaiTroId,
                    trangThai = TrangThaiTaiKhoan.CHO_XAC_THUC,
                    donViId = donViId
                )
                val id = repository.registerTaiKhoan(taiKhoan)
                _registerState.value = "success"



                if (vaiTroId == 2) { // kỹ thuật viên
                    val kyThuatVien = KyThuatVien(
                        taiKhoanId = id,
                        kinhNghiem = null,
                        ngayBatDauLam = null,
                        trangThaiHienTai = TrangThaiKtv.DANG_NGHI,
                        ghiChu = null
                    )
                    kyThuatVienRepo.insert(kyThuatVien)
                }

            } catch (e: Exception) {
                _registerState.value = "failed: ${e.message}"
            }
        }
    }
}
