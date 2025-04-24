package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.map


@HiltViewModel
class QLDVDanhSachYeuCauViewModel @Inject constructor(
    private val repository: YeuCauRepository
) : ViewModel() {


    fun deleteYeuCau(yeuCauId: Int) {
        viewModelScope.launch {
            repository.deleteYeuCauWithDetails(yeuCauId)
        }
    }

    private val _yeuCauList = MutableStateFlow<List<YeuCau>>(emptyList())
    val yeuCauList: StateFlow<List<YeuCau>> = _yeuCauList

    private val _selectedTrangThai = MutableStateFlow<String?>(null)
    val selectedTrangThai: StateFlow<String?> = _selectedTrangThai

    fun observeYeuCauByDonVi(donViId: Int) {
        viewModelScope.launch {
            repository.getYeuCauByDonVi(donViId).collect { danhSach ->
                _yeuCauList.value = danhSach

                // Giữ nguyên việc cập nhật trạng thái
                danhSach.map { yeuCau ->
                    async { repository.capNhatTrangThaiYeuCau(yeuCau.id) }
                }.awaitAll()
            }
        }
    }


    fun loadYeuCauList(donViId: Int) {
        viewModelScope.launch {
            val danhSach = repository.getAllYeuCauTruNhapOnce()
            danhSach.map { yeuCau ->
                async { repository.capNhatTrangThaiYeuCau(yeuCau.id) }
            }.awaitAll()

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
