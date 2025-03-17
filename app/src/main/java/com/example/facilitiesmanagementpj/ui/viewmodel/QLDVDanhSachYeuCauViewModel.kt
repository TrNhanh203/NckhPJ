package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QLDVDanhSachYeuCauViewModel @Inject constructor(
    private val repository: YeuCauRepository
) : ViewModel() {
    fun deleteYeuCau(yeuCauId: Int) {
        viewModelScope.launch {
            repository.deleteYeuCau(yeuCauId)
        }
    }

    private val _yeuCauList = MutableStateFlow<List<YeuCau>>(emptyList())
    val yeuCauList: StateFlow<List<YeuCau>> = _yeuCauList

    private val _selectedTrangThai = MutableStateFlow<String?>(null)
    val selectedTrangThai: StateFlow<String?> = _selectedTrangThai

    fun loadYeuCauList(donViId: Int) {
        viewModelScope.launch {
            repository.getYeuCauByDonVi(donViId).collect {
                _yeuCauList.value = it
            }
        }
    }

    val filteredYeuCauList: StateFlow<List<YeuCau>> = combine(
        yeuCauList, selectedTrangThai
    ) { list, trangThai ->
        list.filter { trangThai == null || it.trangThai == trangThai }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setTrangThaiFilter(trangThai: String?) {
        _selectedTrangThai.value = trangThai
    }
}
