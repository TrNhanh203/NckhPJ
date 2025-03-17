package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThietBiDetailViewModel @Inject constructor(
    private val thietBiRepository: ThietBiRepository,
    private val yeuCauRepository: YeuCauRepository
) : ViewModel() {

    private val _thietBi = MutableStateFlow<ThietBi?>(null)
    val thietBi: StateFlow<ThietBi?> = _thietBi

    fun loadThietBi(thietBiId: Int) {
        viewModelScope.launch {
            val thietBi = thietBiRepository.getThietBiById(thietBiId)
            _thietBi.value = thietBi
        }
    }

    fun addChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String) {
        viewModelScope.launch {
            yeuCauRepository.addThietBiToYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
        }
    }

    fun updateChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String) {
        viewModelScope.launch {
            yeuCauRepository.updateChiTietYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
        }
    }

    fun loadChiTietYeuCau(yeuCauId: Int, thietBiId: Int, onLoaded: (ChiTietYeuCau) -> Unit) {
        viewModelScope.launch {
            val chiTietYeuCau = yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
            chiTietYeuCau?.let { onLoaded(it) }
        }
    }
}
