package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCauWithThietBiAndLoaiThietBi
import com.example.facilitiesmanagementpj.data.entity.LoaiThietBi
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.repository.LoaiThietBiRepository
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRequestDetailViewModel @Inject constructor(
    private val repository: YeuCauRepository,
    private val loaiThietBiRepository: LoaiThietBiRepository
) : ViewModel() {

    private val _chiTietYeuCauList = MutableStateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>>(emptyList())
    val chiTietYeuCauList: StateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>> = _chiTietYeuCauList

    private val _deviceTypes = MutableStateFlow<List<LoaiThietBi>>(emptyList())
    val deviceTypes: StateFlow<List<LoaiThietBi>> = _deviceTypes

    private val _selectedDeviceType = MutableStateFlow<String?>(null)
    val selectedDeviceType: StateFlow<String?> = _selectedDeviceType

    private val _selectedRequestType = MutableStateFlow<String?>(null)
    val selectedRequestType: StateFlow<String?> = _selectedRequestType

    val requestTypes = LoaiYeuCau.ALL

    private val _yeuCau = MutableStateFlow<YeuCau?>(null)
    val yeuCau: StateFlow<YeuCau?> = _yeuCau

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun loadChiTietYeuCau(yeuCauId: Int) {
        viewModelScope.launch {
            repository.getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId).collect {
                _chiTietYeuCauList.value = it
            }
        }

        viewModelScope.launch {
            val yc = repository.getYeuCauById(yeuCauId)
            _yeuCau.value = yc
        }
    }

    fun duyetYeuCau() {
        viewModelScope.launch {
            yeuCau.value?.let {
                if (it.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
                    val updated = it.copy(trangThai = TrangThaiYeuCau.DA_XAC_NHAN)
                    repository.updateYeuCauStatus(updated.id, updated.trangThai)
                    _yeuCau.value = updated
                    _snackbarMessage.value = "Yêu cầu đã được duyệt thành công."
                }
            }
        }
    }

    fun tuChoiYeuCau(reason: String) {
        viewModelScope.launch {
            yeuCau.value?.let {
                if (it.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
                    val updated = it.copy(trangThai = TrangThaiYeuCau.TU_CHOI, lyDoTuChoi = reason)
                    repository.updateYeuCauKhiTuChoi(updated.id, updated.trangThai, updated.lyDoTuChoi ?: "Không có lý do")
                    _yeuCau.value = updated
                    _snackbarMessage.value = "Yêu cầu đã bị từ chối."
                }
            }
        }
    }

    fun loadDeviceTypes() {
        viewModelScope.launch {
            loaiThietBiRepository.getAllLoaiThietBi().collect {
                _deviceTypes.value = it
            }
        }
    }

    fun setDeviceTypeFilter(deviceType: String?) {
        _selectedDeviceType.value = deviceType
    }

    fun setRequestTypeFilter(requestType: String?) {
        _selectedRequestType.value = requestType
    }

    data class FilterCriteria(
        val deviceType: String?,
        val requestType: String?
    )

    val filteredChiTietYeuCauList: StateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>> = combine(
        chiTietYeuCauList,
        combine(selectedDeviceType, selectedRequestType) { deviceType, requestType ->
            FilterCriteria(deviceType, requestType)
        }
    ) { list, filterCriteria ->
        list.filter {
            (filterCriteria.deviceType == null || filterCriteria.deviceType == "Tất cả" || it.tenLoaiThietBi == filterCriteria.deviceType) &&
                    (filterCriteria.requestType == null || filterCriteria.requestType == "Tất cả" || it.loaiYeuCau == filterCriteria.requestType)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
