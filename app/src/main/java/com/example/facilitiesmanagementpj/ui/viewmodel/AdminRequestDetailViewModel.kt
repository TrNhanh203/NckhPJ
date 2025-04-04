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
import com.example.facilitiesmanagementpj.data.repository.PhanCongKtvRepository
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import com.example.facilitiesmanagementpj.data.utils.LoaiYeuCau
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
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
    private val thietBiRepository: ThietBiRepository,
    private val phanCongKtvRepository: PhanCongKtvRepository
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

                    // Lấy phanCongId từ ChiTietYeuCauId
                    val phanCongId = phanCongRepository.getPhanCongIdByChiTietYeuCauId(chiTiet.id)

                    var totalDoingTechnicians = 0
                    var totalResponsibleTechnicians = 0
                    if(phanCongId != null){
                        // Lấy số lượng kỹ thuật viên theo trạng thái
                        val acceptedCount = phanCongKtvRepository.getTechnicianCountByStatus(phanCongId ?: 0, TrangThaiPhanCong.DA_CHAP_NHAN)
                        val doingCount = phanCongKtvRepository.getTechnicianCountByStatus(phanCongId ?: 0, TrangThaiPhanCong.DANG_THUC_HIEN)
                        val onBreakCount = phanCongKtvRepository.getTechnicianCountByStatus(phanCongId ?: 0, TrangThaiPhanCong.TAM_NGHI)
                        val pendingCount = phanCongKtvRepository.getTechnicianCountByStatus(phanCongId ?: 0, TrangThaiPhanCong.CHO_PHAN_HOI)
                        //val notRespondedCount = phanCongKtvRepository.getTechnicianCountByStatus(phanCongId ?: 0, TrangThaiPhanCong.KHONG_PHAN_HOI)
                        val completedCount = phanCongKtvRepository.getTechnicianCountByStatus(phanCongId ?: 0, TrangThaiPhanCong.HOAN_THANH)
                        val rejectedCount = phanCongKtvRepository.getTechnicianCountByStatus(phanCongId ?: 0, TrangThaiPhanCong.DA_TU_CHOI)
                        //val canceledCount = phanCongKtvRepository.getTechnicianCountByStatus(phanCongId ?: 0, TrangThaiPhanCong.BI_HUY)

                        // Tính số kỹ thuật viên đang làm việc
                        totalDoingTechnicians = acceptedCount + doingCount + onBreakCount  + completedCount

                        // Tính tổng số kỹ thuật viên chịu trách nhiệm thực tế (tất cả trạng thái trừ THAY_NGUOI và BI_HUY...)
                        totalResponsibleTechnicians = acceptedCount + doingCount + onBreakCount + pendingCount  + completedCount + rejectedCount
                    }


                    ChiTietYeuCauWithDisplayData(
                        chiTiet = chiTiet,
                        soAnh = soAnh,
                        soVideo = soVideo,
                        anhDaiDien = anhDaiDien,
                        totalDoingTechnicians = totalDoingTechnicians,
                        totalResponsibleTechnicians = totalResponsibleTechnicians,
                        phanCongId = phanCongId

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
                                val updatedThietBi =
                                    it.copy(trangThai = TrangThaiThietBi.CHO_BAO_TRI)
                                thietBiRepository.updateThietBiStatus(
                                    updatedThietBi.id,
                                    updatedThietBi.trangThai
                                )
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
                    repository.updateYeuCauKhiTuChoi(
                        updated.id,
                        updated.trangThai,
                        updated.lyDoTuChoi ?: "Không có lý do"
                    )
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
        val anhDaiDien: String?,
        val totalDoingTechnicians: Int, // Số kỹ thuật viên đã chấp nhận
        val totalResponsibleTechnicians: Int,
        val phanCongId: Int?
    )

    data class FilterCriteria(
        val deviceType: String?,
        val requestType: String?
    )





}

