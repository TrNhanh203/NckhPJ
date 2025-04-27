package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.DonViDao
import com.example.facilitiesmanagementpj.data.entity.DonVi
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.repository.PhanCongRepository
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRequestListViewModel @Inject constructor(
    private val yeuCauRepository: YeuCauRepository,
    private val phanCongRepository: PhanCongRepository,
    private val donViDao: DonViDao
) : ViewModel() {

    private val _donViList = MutableStateFlow<List<DonVi>>(emptyList())
    val donViList: StateFlow<List<DonVi>> = _donViList

    private val _yeuCauList = MutableStateFlow<List<YeuCau>>(emptyList())
    val yeuCauList: StateFlow<List<YeuCau>> = _yeuCauList

    private val _selectedTrangThai = MutableStateFlow<String?>(null)
    val selectedTrangThai: StateFlow<String?> = _selectedTrangThai

    private val _selectedDonVi = MutableStateFlow<Int?>(null)
    val selectedDonVi: StateFlow<Int?> = _selectedDonVi

    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder

    init {
        loadYeuCauList()
        loadDonViList()
    }

    fun reloadYeuCauList() {
        viewModelScope.launch {
            loadYeuCauList()
        }
    }


    fun loadYeuCauList() {
//        viewModelScope.launch {
//            val danhSach = yeuCauRepository.getAllYeuCauTruNhapOnce()
//            danhSach.map { yeuCau ->
//                async { yeuCauRepository.capNhatTrangThaiYeuCau(yeuCau.id) }
//            }.awaitAll()
//
//            yeuCauRepository.getAllYeuCauTruNhap().collect {
//                _yeuCauList.value = it
//            }
//        }
        viewModelScope.launch {
            yeuCauRepository.getAllYeuCauTruNhap().collect { list ->
                list.map { yeuCau ->
                    launch {
                        yeuCauRepository.capNhatTrangThaiYeuCau(yeuCau.id)
                    }
                }
                _yeuCauList.value = list
            }
        }

    }

    fun loadDonViList() {
        viewModelScope.launch {
            donViDao.getAll().collect {
                _donViList.value = it
            }
        }
    }

    fun setTrangThaiFilter(trangThai: String?) {
        _selectedTrangThai.value = trangThai
    }

    fun setDonViFilter(donViId: Int?) {
        _selectedDonVi.value = donViId
    }

    fun setSortOrder(order: SortOrder) {
        _sortOrder.value = order
    }

    data class YeuCauWithDonViAndCount(
        val yeuCau: YeuCau,
        val tenDonVi: String,
        val tongChiTiet: Int,
        val daPhanCong: Int
    )

    val yeuCauWithCount: StateFlow<List<YeuCauWithDonViAndCount>> = combine(
        yeuCauList, selectedTrangThai, selectedDonVi, sortOrder, donViList
    ) { list, trangThai, donViId, sortOrder, donVis ->

        val donViMap = donVis.associateBy { it.id }

        list.filter { trangThai == null || it.trangThai == trangThai }
            .filter { donViId == null || it.donViId == donViId }
            .sortedBy { if (sortOrder == SortOrder.NEWEST) -it.ngayYeuCau else it.ngayYeuCau }
            .map { yeuCau ->
                val tenDonVi = donViMap[yeuCau.donViId]?.tenDonVi ?: "Không rõ"
                val chiTietList = yeuCauRepository.getChiTietYeuCauByYeuCauId(yeuCau.id)
                val daPhanCong = chiTietList.count { phanCongRepository.hasPhanCongForChiTiet(it.id) }

                YeuCauWithDonViAndCount(
                    yeuCau = yeuCau,
                    tenDonVi = tenDonVi,
                    tongChiTiet = chiTietList.size,
                    daPhanCong = daPhanCong
                )
            }

    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    enum class SortOrder {
        NEWEST, OLDEST
    }
}





