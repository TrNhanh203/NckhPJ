package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao
import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao.KyThuatVienWithTaiKhoanImpl
import com.example.facilitiesmanagementpj.data.entity.ChuyenMon
import com.example.facilitiesmanagementpj.data.entity.KyThuatVienWithTaiKhoan
import com.example.facilitiesmanagementpj.data.relation.KyThuatVienWithSoTask
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

    var selectedTrangThai by mutableStateOf<String?>(null)
    var selectedChuyenMonIds by mutableStateOf<Set<Int>>(emptySet())
    var searchText by mutableStateOf("")
    var allChuyenMon by mutableStateOf<List<ChuyenMon>>(emptyList())


    private val _danhSachKTV = MutableStateFlow<List<KyThuatVienWithSoTask>>(emptyList())
    val danhSachKTV: StateFlow<List<KyThuatVienWithSoTask>> = _danhSachKTV


    private val _selectedKtvIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedKtvIds: StateFlow<Set<Int>> = _selectedKtvIds


    fun toggleChuyenMon(id: Int, checked: Boolean) {
        selectedChuyenMonIds = if (checked) selectedChuyenMonIds + id else selectedChuyenMonIds - id
    }

    fun filterByTrangThai(trangThai: String?) {
        selectedTrangThai = trangThai
        applyFilters()
    }

    fun resetFilters() {
        selectedTrangThai = null
        selectedChuyenMonIds = emptySet()
        searchText = ""
        applyFilters()
    }

    fun applyFilters() {
        viewModelScope.launch {
            val rawList = kyThuatVienDao.getFilteredWithTaiKhoan(
                selectedTrangThai, selectedChuyenMonIds.toList()
            )

            val danhSach = rawList.map { impl ->
                KyThuatVienWithTaiKhoan(impl.kyThuatVien, impl.taiKhoan)
            }

            val enrichedList = danhSach.filter {
                it.taiKhoan.hoTen?.contains(searchText, ignoreCase = true) == true || searchText.isBlank()
            }.map {
                val soTask = phanCongRepo.getSoTaskDangLam(it.taiKhoan.id)
                KyThuatVienWithSoTask(it, soTask)
            }

            _danhSachKTV.value = enrichedList
        }
    }


    fun loadDanhSachKTV(trangThai: String? = null) {
        viewModelScope.launch {
            val rawList = kyThuatVienDao.getAllWithTaiKhoanByTrangThai(trangThai)

            val danhSach = rawList.map { impl ->
                KyThuatVienWithTaiKhoan(
                    kyThuatVien = impl.kyThuatVien,
                    taiKhoan = impl.taiKhoan
                )
            }

            val enrichedList = danhSach.map { ktv ->
                val soTask = phanCongRepo.getSoTaskDangLam(ktv.taiKhoan.id)
                KyThuatVienWithSoTask(ktv, soTask)
            }

            _danhSachKTV.value = enrichedList
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