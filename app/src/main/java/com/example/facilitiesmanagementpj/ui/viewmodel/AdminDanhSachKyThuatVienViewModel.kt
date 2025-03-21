package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.ChuyenMon
import com.example.facilitiesmanagementpj.data.entity.KyThuatVienWithTaiKhoan
import com.example.facilitiesmanagementpj.data.repository.ChuyenMonRepository
import com.example.facilitiesmanagementpj.data.repository.KyThuatVienRepository



// ✅ ViewModel
@HiltViewModel
class DanhSachKyThuatVienViewModel @Inject constructor(
    private val kyThuatVienRepo: KyThuatVienRepository,
    private val chuyenMonRepo: ChuyenMonRepository
) : ViewModel() {

    var allChuyenMon by mutableStateOf<List<ChuyenMon>>(emptyList())
        private set

    var selectedTrangThai by mutableStateOf<String?>(null)
    var selectedChuyenMonIds by mutableStateOf<Set<Int>>(emptySet())
    var searchText by mutableStateOf("")

    var danhSachKTV by mutableStateOf<List<KyThuatVienWithTaiKhoan>>(emptyList())
        private set

    init {
        loadChuyenMon()
        applyFilters()
    }

    fun loadChuyenMon() {
        viewModelScope.launch {
            allChuyenMon = chuyenMonRepo.getAllChuyenMon()
        }
    }

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
            val rawList = kyThuatVienRepo.getKyThuatVienFiltered(
                selectedTrangThai, selectedChuyenMonIds.toList()
            )
            danhSachKTV = if (searchText.isBlank()) rawList
            else rawList.filter {
                it.taiKhoan.hoTen?.contains(searchText, ignoreCase = true) == true
            }
        }
    }
}