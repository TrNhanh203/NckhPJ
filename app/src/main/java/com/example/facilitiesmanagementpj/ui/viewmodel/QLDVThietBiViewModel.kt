package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.ThietBiWithDetails
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import com.example.facilitiesmanagementpj.data.dao.ThietBiWithLoai
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QLDVThietBiViewModel @Inject constructor(
    private val thietBiRepository: ThietBiRepository
) : ViewModel() {

    private val _thietBiList = MutableStateFlow<List<ThietBiWithDetails>>(emptyList())
    val thietBiList: StateFlow<List<ThietBiWithDetails>> = _thietBiList

    private val _selectedDay = MutableStateFlow<String?>(null)
    val selectedDay: StateFlow<String?> = _selectedDay

    private val _selectedPhong = MutableStateFlow<String?>(null)
    val selectedPhong: StateFlow<String?> = _selectedPhong

    private val _selectedTang = MutableStateFlow<String?>(null)
    val selectedTang: StateFlow<String?> = _selectedTang

    private val _selectedTrangThai = MutableStateFlow<String?>(null)
    val selectedTrangThai: StateFlow<String?> = _selectedTrangThai

    private val _selectedLoaiThietBi = MutableStateFlow<String?>(null)
    val selectedLoaiThietBi: StateFlow<String?> = _selectedLoaiThietBi // ✅ Thêm bộ lọc Loại Thiết Bị

    fun loadThietBiList(donViId: Int) {
        viewModelScope.launch {
            thietBiRepository.getThietBiByDonVi(donViId).collect {
                _thietBiList.value = it
            }
        }
    }

    val filteredThietBiList: StateFlow<List<ThietBiWithDetails>> = combine(
        thietBiList,
        combine(selectedDay, selectedPhong, selectedTang, selectedTrangThai, selectedLoaiThietBi) { day, phong, tang, trangThai, loaiThietBi ->
            FilterCriteria(day, phong, tang, trangThai, loaiThietBi) // ✅ Đóng gói bộ lọc vào một class
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

    // ✅ Định nghĩa class để chứa bộ lọc
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




//@HiltViewModel
//class QLDVThietBiViewModel @Inject constructor(
//    private val thietBiRepository: ThietBiRepository
//) : ViewModel() {
//
//    private val _thietBiList = MutableStateFlow<List<ThietBiWithDetails>>(emptyList())
//    val thietBiList: StateFlow<List<ThietBiWithDetails>> = _thietBiList
//
//    private val _selectedDay = MutableStateFlow<String?>(null)
//    val selectedDay: StateFlow<String?> = _selectedDay
//
//    private val _selectedPhong = MutableStateFlow<String?>(null)
//    val selectedPhong: StateFlow<String?> = _selectedPhong
//
//    private val _selectedTang = MutableStateFlow<String?>(null)
//    val selectedTang: StateFlow<String?> = _selectedTang
//
//    private val _selectedTrangThai = MutableStateFlow<String?>(null)
//    val selectedTrangThai: StateFlow<String?> = _selectedTrangThai
//
//    private val _selectedLoaiThietBi = MutableStateFlow<String?>(null)
//    val selectedLoaiThietBi: StateFlow<String?> = _selectedLoaiThietBi
//
//    fun loadThietBiList(donViId: Int) {
//        viewModelScope.launch {
//            thietBiRepository.getThietBiByDonVi(donViId).collect {
//                _thietBiList.value = it
//            }
//        }
//    }
//
//    val filteredThietBiList: StateFlow<List<ThietBiWithDetails>> = combine(
//        thietBiList, selectedDay, selectedPhong, selectedTang, selectedTrangThai
//    ) { list, day, phong, tang, trangThai ->
//        list.filter {
//            (day == null || it.tenDay == day) &&
//                    (phong == null || it.tenPhong == phong) &&
//                    (tang == null || it.tenTang == tang) &&
//                    (trangThai == null || it.trangThai == trangThai)
//        }
//    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
//
//    fun setDayFilter(day: String?) {
//        _selectedDay.value = day
//    }
//
//    fun setPhongFilter(phong: String?) {
//        _selectedPhong.value = phong
//    }
//
//    fun setTangFilter(tang: String?) {
//        _selectedTang.value = tang
//    }
//
//    fun setTrangThaiFilter(trangThai: String?) {
//        _selectedTrangThai.value = trangThai
//    }
//}



