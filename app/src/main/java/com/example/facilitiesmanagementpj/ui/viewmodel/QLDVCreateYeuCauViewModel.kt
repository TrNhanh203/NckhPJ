package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.AnhMinhChungBaoCaoDao
import com.example.facilitiesmanagementpj.data.dao.ChiTietYeuCauDao
import com.example.facilitiesmanagementpj.data.dao.LoaiThietBiDao
import com.example.facilitiesmanagementpj.data.dao.ThietBiDao
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCauWithThietBiAndLoaiThietBi
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QLDVCreateYeuCauViewModel @Inject constructor(
    private val repository: YeuCauRepository,
    private val chiTietYeuCauDao: ChiTietYeuCauDao,
    private val thietBiDao: ThietBiDao,
    private val anhMinhChungBaoCaoDao: AnhMinhChungBaoCaoDao,
    private val loaiThietBiDao: LoaiThietBiDao
) : ViewModel() {

    private val _yeuCau = MutableStateFlow<YeuCau?>(null)
    val yeuCau: StateFlow<YeuCau?> = _yeuCau

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }

    fun capNhatTrangThaiYeuCau(trangThaiMoi: String) {
        viewModelScope.launch {
            yeuCau.value?.let {
                repository.updateYeuCauStatus(it.id, trangThaiMoi)
                _yeuCau.value = it.copy(trangThai = trangThaiMoi)
                _snackbarMessage.value = "Đã cập nhật trạng thái yêu cầu: $trangThaiMoi"
            }
        }
    }



    fun removeChiTietYeuCau(chiTietId: Int) {
        viewModelScope.launch {
            repository.removeChiTietYeuCau(chiTietId)
            _chiTietYeuCauList.value = _chiTietYeuCauList.value.filter { it.id != chiTietId }
        }
    }

    fun updateYeuCauStatus(yeuCauId: Int, status: String) {
        viewModelScope.launch {
            repository.updateYeuCauStatus(yeuCauId, status)
        }
    }

    private val _yeuCauId = MutableStateFlow<Int?>(null)
    val yeuCauId: StateFlow<Int?> = _yeuCauId

    private val _chiTietYeuCauList = MutableStateFlow<List<ChiTietYeuCau>>(emptyList())
    val chiTietYeuCauList: StateFlow<List<ChiTietYeuCau>> = _chiTietYeuCauList

    private val _chiTietList = MutableStateFlow<List<QLDVChiTietYeuCauWithDisplayData>>(emptyList())
    val chiTietList = _chiTietList.asStateFlow()


    fun getYeuCauById(yeuCauId: Int, onResult: (YeuCau) -> Unit) {
        viewModelScope.launch {
            val yc = repository.getYeuCauById(yeuCauId)
            yc?.let {
                _yeuCau.value = it
                onResult(it)
            }
        }
    }

    fun setYeuCauId(yeuCauId: Int) {
        _yeuCauId.value = yeuCauId
    }

    fun createYeuCau(taiKhoanId: Int, donViId: Int, moTa: String) {
        viewModelScope.launch {
            val id = repository.createYeuCau(taiKhoanId, donViId, moTa).toInt()
            _yeuCauId.value = id
        }
    }

    fun loadChiTietYeuCau(yeuCauId: Int) {
        viewModelScope.launch {
            repository.getChiTietYeuCau(yeuCauId).collect {
                _chiTietYeuCauList.value = it
            }
        }
    }

    fun loadChiTietYeuCauList(yeuCauId: Int) {
        viewModelScope.launch {
            chiTietYeuCauDao.getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId)
                .collect { list ->
                    val displayList = list.map { item ->
                        val images = anhMinhChungBaoCaoDao.getImagesByChiTietBaoCaoId(item.id)
                        val videos = anhMinhChungBaoCaoDao.getVideosByChiTietBaoCaoId(item.id)

                        QLDVChiTietYeuCauWithDisplayData(
                            chiTiet = item,
                            tenThietBi = item.tenThietBi,
                            tenLoaiThietBi = item.tenLoaiThietBi,
                            anhDaiDien = images.firstOrNull()?.urlAnh,
                            soAnh = images.size,
                            soVideo = videos.size
                        )
                    }
                    _chiTietList.value = displayList
                }
        }
    }


}


data class QLDVChiTietYeuCauWithDisplayData(
    val chiTiet: ChiTietYeuCauWithThietBiAndLoaiThietBi,
    val tenThietBi: String?,
    val tenLoaiThietBi: String?,
    val anhDaiDien: String?,
    val soAnh: Int,
    val soVideo: Int
)
