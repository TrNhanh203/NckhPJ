package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungBaoCao
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCauWithThietBiAndLoaiThietBi
import com.example.facilitiesmanagementpj.data.entity.LoaiThietBi
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.repository.AnhMinhChungBaoCaoRepository
import com.example.facilitiesmanagementpj.data.repository.PhanCongRepository
import com.example.facilitiesmanagementpj.data.repository.LoaiThietBiRepository
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
import com.example.facilitiesmanagementpj.data.utils.TrangThaiThietBi
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminRequestDetailViewModel @Inject constructor(
    private val repository: YeuCauRepository,
    private val loaiThietBiRepository: LoaiThietBiRepository,
    private val anhRepository: AnhMinhChungBaoCaoRepository,
    private val phanCongRepository: PhanCongRepository,
    private val thietBiRepository: ThietBiRepository
) : ViewModel() {

    private val _yeuCau = MutableStateFlow<YeuCau?>(null)
    val yeuCau: StateFlow<YeuCau?> = _yeuCau

    private val _allChiTiet = MutableStateFlow<List<ChiTietYeuCauWithDisplayData>>(emptyList())
    val allChiTiet: StateFlow<List<ChiTietYeuCauWithDisplayData>> = _allChiTiet

    private val _deviceTypes = MutableStateFlow<List<LoaiThietBi>>(emptyList())
    val deviceTypes: StateFlow<List<LoaiThietBi>> = _deviceTypes

    private val _selectedDeviceType = MutableStateFlow<String?>(null)
    val selectedDeviceType: StateFlow<String?> = _selectedDeviceType

    private val _selectedRequestType = MutableStateFlow<String?>(null)
    val selectedRequestType: StateFlow<String?> = _selectedRequestType

    val requestTypes = LoaiYeuCau.ALL

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun loadChiTietYeuCau(yeuCauId: Int) {
        viewModelScope.launch {
            val yc = repository.getYeuCauById(yeuCauId)
            _yeuCau.value = yc

            repository.getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId).collect { list ->
                val fullList = list.map { chiTiet ->
                    val media = anhRepository.getByChiTietId(chiTiet.id)
                    val soAnh = media.count { it.type == "image" }
                    val soVideo = media.count { it.type == "video" }
                    val anhDaiDien = media.firstOrNull { it.type == "image" }?.urlAnh

                    ChiTietYeuCauWithDisplayData(
                        chiTiet = chiTiet,
                        soAnh = soAnh,
                        soVideo = soVideo,
                        anhDaiDien = anhDaiDien
                    )
                }
                _allChiTiet.value = fullList
            }
        }
    }

    val daPhanCongList: StateFlow<List<ChiTietYeuCauWithDisplayData>> = _allChiTiet.map { list ->
        list.filter { phanCongRepository.hasPhanCongForChiTiet(it.chiTiet.id) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val chuaPhanCongList: StateFlow<List<ChiTietYeuCauWithDisplayData>> = _allChiTiet.map { list ->
        list.filterNot { phanCongRepository.hasPhanCongForChiTiet(it.chiTiet.id) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val filteredDaPhanCongList: StateFlow<List<ChiTietYeuCauWithDisplayData>> = combine(
        daPhanCongList, selectedDeviceType, selectedRequestType
    ) { list, deviceType, requestType ->
        list.filter {
            (deviceType.isNullOrBlank() || it.chiTiet.tenLoaiThietBi == deviceType) &&
                    (requestType.isNullOrBlank() || it.chiTiet.loaiYeuCau == requestType)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val filteredChuaPhanCongList: StateFlow<List<ChiTietYeuCauWithDisplayData>> = combine(
        chuaPhanCongList, selectedDeviceType, selectedRequestType
    ) { list, deviceType, requestType ->
        list.filter {
            (deviceType.isNullOrBlank() || it.chiTiet.tenLoaiThietBi == deviceType) &&
                    (requestType.isNullOrBlank() || it.chiTiet.loaiYeuCau == requestType)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


//    fun duyetYeuCau() {
//        viewModelScope.launch {
//            yeuCau.value?.let {
//                if (it.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
//                    val updated = it.copy(trangThai = TrangThaiYeuCau.DA_XAC_NHAN)
//                    repository.updateYeuCauStatus(updated.id, updated.trangThai)
//                    _yeuCau.value = updated // Cập nhật trạng thái ngay lập tức
//                    _snackbarMessage.value = "Yêu cầu đã được duyệt thành công."
//                }
//            }
//        }
//    }
fun duyetYeuCau() {
    viewModelScope.launch {
        yeuCau.value?.let {
            if (it.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
                // Cập nhật trạng thái yêu cầu
                val updatedYeuCau = it.copy(trangThai = TrangThaiYeuCau.DA_XAC_NHAN)
                repository.updateYeuCauStatus(updatedYeuCau.id, updatedYeuCau.trangThai)
                _yeuCau.value = updatedYeuCau // Cập nhật trạng thái ngay lập tức
                _snackbarMessage.value = "Yêu cầu đã được duyệt thành công."

                // Lấy danh sách thiết bị trong yêu cầu
                val chiTietYeuCauList = repository.getChiTietYeuCauByYeuCauId(it.id)

                // Cập nhật trạng thái của tất cả các thiết bị trong yêu cầu sang "Chờ bảo trì"
                chiTietYeuCauList.forEach { chiTietYeuCau ->
                    chiTietYeuCau.thietBiId?.let { thietBiId ->
                        val thietBi = thietBiRepository.getThietBiById(thietBiId)
                        thietBi?.let {
                            val updatedThietBi = it.copy(trangThai = TrangThaiThietBi.CHO_BAO_TRI)
                            thietBiRepository.updateThietBiStatus(updatedThietBi.id, updatedThietBi.trangThai)
                        }
                    }
                }
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

    data class ChiTietYeuCauWithDisplayData(
        val chiTiet: ChiTietYeuCauWithThietBiAndLoaiThietBi,
        val soAnh: Int,
        val soVideo: Int,
        val anhDaiDien: String?
    )

    data class FilterCriteria(
        val deviceType: String?,
        val requestType: String?
    )
}



//package com.example.facilitiesmanagementpj.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.media3.common.util.Log
//import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCauWithThietBiAndLoaiThietBi
//import com.example.facilitiesmanagementpj.data.entity.LoaiThietBi
//import com.example.facilitiesmanagementpj.data.entity.YeuCau
//import com.example.facilitiesmanagementpj.data.repository.LoaiThietBiRepository
//import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
//import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
//import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.*
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class AdminRequestDetailViewModel @Inject constructor(
//    private val repository: YeuCauRepository,
//    private val loaiThietBiRepository: LoaiThietBiRepository
//) : ViewModel() {
//
//    private val _chiTietYeuCauList = MutableStateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>>(emptyList())
//    val chiTietYeuCauList: StateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>> = _chiTietYeuCauList
//
//    private val _deviceTypes = MutableStateFlow<List<LoaiThietBi>>(emptyList())
//    val deviceTypes: StateFlow<List<LoaiThietBi>> = _deviceTypes
//
//    private val _selectedDeviceType = MutableStateFlow<String?>(null)
//    val selectedDeviceType: StateFlow<String?> = _selectedDeviceType
//
//    private val _selectedRequestType = MutableStateFlow<String?>(null)
//    val selectedRequestType: StateFlow<String?> = _selectedRequestType
//
//    val requestTypes = LoaiYeuCau.ALL
//
//    private val _yeuCau = MutableStateFlow<YeuCau?>(null)
//    val yeuCau: StateFlow<YeuCau?> = _yeuCau
//
//    private val _snackbarMessage = MutableStateFlow<String?>(null)
//    val snackbarMessage: StateFlow<String?> = _snackbarMessage
//
//    fun clearSnackbar() {
//        _snackbarMessage.value = null
//    }
//
//    fun loadChiTietYeuCau(yeuCauId: Int) {
//        viewModelScope.launch {
//            repository.getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId).collect {
//                _chiTietYeuCauList.value = it
//            }
//        }
//
//        viewModelScope.launch {
//            val yc = repository.getYeuCauById(yeuCauId)
//            _yeuCau.value = yc
//        }
//    }
//
//    fun duyetYeuCau() {
//        viewModelScope.launch {
//            yeuCau.value?.let {
//                if (it.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
//                    val updated = it.copy(trangThai = TrangThaiYeuCau.DA_XAC_NHAN)
//                    repository.updateYeuCauStatus(updated.id, updated.trangThai)
//                    _yeuCau.value = updated
//                    _snackbarMessage.value = "Yêu cầu đã được duyệt thành công."
//                }
//            }
//        }
//    }
//
//    fun tuChoiYeuCau(reason: String) {
//        viewModelScope.launch {
//            yeuCau.value?.let {
//                if (it.trangThai == TrangThaiYeuCau.CHO_XAC_NHAN) {
//                    val updated = it.copy(trangThai = TrangThaiYeuCau.TU_CHOI, lyDoTuChoi = reason)
//                    repository.updateYeuCauKhiTuChoi(updated.id, updated.trangThai, updated.lyDoTuChoi ?: "Không có lý do")
//                    _yeuCau.value = updated
//                    _snackbarMessage.value = "Yêu cầu đã bị từ chối."
//                }
//            }
//        }
//    }
//
//    fun loadDeviceTypes() {
//        viewModelScope.launch {
//            loaiThietBiRepository.getAllLoaiThietBi().collect {
//                _deviceTypes.value = it
//            }
//        }
//    }
//
//    fun setDeviceTypeFilter(deviceType: String?) {
//        _selectedDeviceType.value = deviceType
//    }
//
//    fun setRequestTypeFilter(requestType: String?) {
//        _selectedRequestType.value = requestType
//    }
//
//    data class FilterCriteria(
//        val deviceType: String?,
//        val requestType: String?
//    )
//
//    val filteredChiTietYeuCauList: StateFlow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>> = combine(
//        chiTietYeuCauList,
//        combine(selectedDeviceType, selectedRequestType) { deviceType, requestType ->
//            FilterCriteria(deviceType, requestType)
//        }
//    ) { list, filterCriteria ->
//        list.filter {
//            (filterCriteria.deviceType == null || filterCriteria.deviceType == "Tất cả" || it.tenLoaiThietBi == filterCriteria.deviceType) &&
//                    (filterCriteria.requestType == null || filterCriteria.requestType == "Tất cả" || it.loaiYeuCau == filterCriteria.requestType)
//        }
//    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
//}
