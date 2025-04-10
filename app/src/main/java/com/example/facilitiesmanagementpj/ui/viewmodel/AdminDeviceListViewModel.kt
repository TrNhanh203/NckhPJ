package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.ThietBiWithDetails
import com.example.facilitiesmanagementpj.data.entity.DonVi
import com.example.facilitiesmanagementpj.data.repository.DonViRepository
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDeviceListViewModel @Inject constructor(
    private val thietBiRepository: ThietBiRepository,
    private val donViRepository: DonViRepository
) : ViewModel() {

    val donViList: StateFlow<List<DonVi>> = donViRepository.getAllDonVi()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _selectedDay = MutableStateFlow<String?>(null)
    val selectedDay: StateFlow<String?> = _selectedDay

    private val _selectedPhong = MutableStateFlow<String?>(null)
    val selectedPhong: StateFlow<String?> = _selectedPhong

    private val _selectedTang = MutableStateFlow<String?>(null)
    val selectedTang: StateFlow<String?> = _selectedTang

    private val _selectedTrangThai = MutableStateFlow<String?>(null)
    val selectedTrangThai: StateFlow<String?> = _selectedTrangThai

    private val _selectedLoaiThietBi = MutableStateFlow<String?>(null)
    val selectedLoaiThietBi: StateFlow<String?> = _selectedLoaiThietBi

    private val _selectedDonViId = MutableStateFlow<Int?>(null)
    val selectedDonViId: StateFlow<Int?> = _selectedDonViId

    fun setSelectedDonViId(id: Int?) {
        _selectedDonViId.value = id
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val baseThietBiList: StateFlow<List<ThietBiWithDetails>> = selectedDonViId.flatMapLatest { id ->
        if (id == null)
            thietBiRepository.getAllThietBiWithDetails()
        else
            thietBiRepository.getThietBiByDonVi(id)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    val filteredThietBiList: StateFlow<List<ThietBiWithDetails>> = combine(
        baseThietBiList,
        combine(selectedDay, selectedPhong, selectedTang, selectedTrangThai, selectedLoaiThietBi) { day, phong, tang, trangThai, loaiThietBi ->
            FilterCriteria(day, phong, tang, trangThai, loaiThietBi)
        }
    ) { list, filterCriteria ->
        list.filter {
            (filterCriteria.day == null || it.tenDay == filterCriteria.day) &&
            (filterCriteria.phong == null || it.tenPhong == filterCriteria.phong) &&
            (filterCriteria.tang == null || it.tenTang == filterCriteria.tang) &&
            (filterCriteria.trangThai == null || it.trangThai == filterCriteria.trangThai) &&
            (filterCriteria.loaiThietBi == null || it.tenLoai == filterCriteria.loaiThietBi)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun resetFilters() {
        _selectedDay.value = null
        _selectedPhong.value = null
        _selectedTang.value = null
        _selectedTrangThai.value = null
        _selectedLoaiThietBi.value = null
    }

    data class FilterCriteria(
        val day: String?,
        val phong: String?,
        val tang: String?,
        val trangThai: String?,
        val loaiThietBi: String?
    )

    fun setDayFilter(day: String?) {
        _selectedDay.value = day
    }

    fun setPhongFilter(phong: String?) {
        _selectedPhong.value = phong
    }

    fun setTangFilter(tang: String?) {
        _selectedTang.value = tang
    }

    fun setTrangThaiFilter(trangThai: String?) {
        _selectedTrangThai.value = trangThai
    }

    fun setLoaiThietBiFilter(loaiThietBi: String?) {
        _selectedLoaiThietBi.value = loaiThietBi
    }
}