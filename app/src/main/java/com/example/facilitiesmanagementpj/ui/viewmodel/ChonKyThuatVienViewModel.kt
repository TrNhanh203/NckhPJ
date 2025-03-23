package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao
import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao.KyThuatVienWithTaiKhoanImpl
import com.example.facilitiesmanagementpj.data.repository.PhanCongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChonKyThuatVienViewModel @Inject constructor(
    private val kyThuatVienDao: KyThuatVienDao,
    private val phanCongRepo: PhanCongRepository
) : ViewModel() {

    private val _danhSachKTV = MutableStateFlow<List<KyThuatVienWithTaiKhoanImpl>>(emptyList())
    val danhSachKTV: StateFlow<List<KyThuatVienWithTaiKhoanImpl>> = _danhSachKTV

    private val _selectedKtvIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedKtvIds: StateFlow<Set<Int>> = _selectedKtvIds

    fun loadDanhSachKTV(trangThai: String? = null) {
        viewModelScope.launch {
            _danhSachKTV.value = kyThuatVienDao.getAllWithTaiKhoanByTrangThai(trangThai)
        }
    }

    fun toggleSelection(kyThuatVienId: Int) {
        _selectedKtvIds.value = _selectedKtvIds.value.toMutableSet().apply {
            if (contains(kyThuatVienId)) remove(kyThuatVienId)
            else add(kyThuatVienId)
        }
    }

    fun isSelected(kyThuatVienId: Int): Boolean = _selectedKtvIds.value.contains(kyThuatVienId)

    fun clearSelection() {
        _selectedKtvIds.value = emptySet()
    }

//    fun taoPhanCong(thietBiId: Int, yeuCauId: Int) {
//        viewModelScope.launch {
//            selectedKtvIds.value.forEach { ktvId ->
//                phanCongRepo.insertPhanCong(thietBiId, yeuCauId, ktvId)
//            }
//        }
//    }
}