package com.example.facilitiesmanagementpj.ui.viewmodel

import com.example.facilitiesmanagementpj.data.repository.PhanCongKtvRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec
import com.example.facilitiesmanagementpj.data.utils.LoaiAnhMinhChungLamViec



@HiltViewModel
class YeuCauGiaHanViewModel @Inject constructor(
    private val anhRepo: AnhMinhChungLamViecRepository,
    private val pcKtvRepo: PhanCongKtvRepository
) : ViewModel() {

    private val _anhGiaHan = MutableStateFlow<List<AnhMinhChungLamViec>>(emptyList())
    val anhGiaHan: StateFlow<List<AnhMinhChungLamViec>> = _anhGiaHan

    private val _soPhutXinGiaHan = MutableStateFlow(0)
    val soPhutXinGiaHan: StateFlow<Int> = _soPhutXinGiaHan

    fun loadYeuCauGiaHan(phanCongKtvId: Int) {
        viewModelScope.launch {
            val allAnh = anhRepo.getByPhanCongKtvId(phanCongKtvId)
                .filter { it.loaiAnh == LoaiAnhMinhChungLamViec.XIN_GIA_HAN }

            // ✅ Tìm thời gian mới nhất trong nhóm
            val latestTime = allAnh.maxOfOrNull { it.thoiGianTaiLen }

            // ✅ Lọc ra những ảnh thuộc lần xin gần nhất
            val latestGroup = allAnh.filter { it.thoiGianTaiLen == latestTime }

            _anhGiaHan.value = latestGroup

            val pc = pcKtvRepo.getById(phanCongKtvId)
            _soPhutXinGiaHan.value = pc?.soThoiGianXinGiaHan ?: 0
        }
    }


//    fun loadYeuCauGiaHan(phanCongKtvId: Int) {
//        viewModelScope.launch {
//            val anh = anhRepo.getByPhanCongKtvId(phanCongKtvId)
//                .filter { it.loaiAnh == LoaiAnhMinhChungLamViec.XIN_GIA_HAN }
//                .sortedBy { it.thoiGianTaiLen }
//
//            _anhGiaHan.value = anh
//
//            val pc = pcKtvRepo.getById(phanCongKtvId)
//            _soPhutXinGiaHan.value = pc?.soThoiGianXinGiaHan ?: 0
//        }
//    }

    fun duyetYeuCau(phanCongKtvId: Int) {
        viewModelScope.launch {
            pcKtvRepo.duyetGiaHan(phanCongKtvId)
        }
    }

    fun tuChoiYeuCau(phanCongKtvId: Int) {
        viewModelScope.launch {
            pcKtvRepo.tuChoiGiaHan(phanCongKtvId)
        }
    }
}
