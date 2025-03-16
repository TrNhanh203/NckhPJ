package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanChiTiet
import com.example.facilitiesmanagementpj.data.entity.KyThuatVien
import com.example.facilitiesmanagementpj.data.repository.KyThuatVienRepository
import com.example.facilitiesmanagementpj.data.repository.TaiKhoanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class AdminViewDetailProfileViewModel @Inject constructor(
    private val taiKhoanRepository: TaiKhoanRepository,
    private val kyThuatVienRepository: KyThuatVienRepository
) : ViewModel() {

    private val _taiKhoanChiTiet = MutableStateFlow<TaiKhoanChiTiet?>(null)
    val taiKhoanChiTiet: StateFlow<TaiKhoanChiTiet?> = _taiKhoanChiTiet

    private val _chuyenMonList = MutableStateFlow<List<String>>(emptyList())
    val chuyenMonList: StateFlow<List<String>> = _chuyenMonList

    private val _kyThuatVienChiTiet = MutableStateFlow<KyThuatVien?>(null)
    val kyThuatVienChiTiet: StateFlow<KyThuatVien?> = _kyThuatVienChiTiet

    fun loadTaiKhoanChiTiet(taiKhoanId: Int) {
        viewModelScope.launch {
            val taiKhoan = taiKhoanRepository.getTaiKhoanChiTiet(taiKhoanId)
            _taiKhoanChiTiet.value = taiKhoan

            if (taiKhoan?.tenVaiTro == "Kỹ Thuật Viên") {
                viewModelScope.launch {
                    kyThuatVienRepository.getChuyenMonCuaKTV(taiKhoanId).collect {
                        _chuyenMonList.value = it
                    }
                }

                viewModelScope.launch {
                    kyThuatVienRepository.getKyThuatVienByTaiKhoanId(taiKhoanId).collect {
                        _kyThuatVienChiTiet.value = it
                    }
                }

            }
        }
    }




    fun updateTrangThaiTaiKhoan(taiKhoanId: Int, trangThaiMoi: String) {
        viewModelScope.launch {
            taiKhoanRepository.updateTrangThai(taiKhoanId, trangThaiMoi)
            _taiKhoanChiTiet.value = _taiKhoanChiTiet.value?.copy(trangThai = trangThaiMoi)
        }
    }
}
