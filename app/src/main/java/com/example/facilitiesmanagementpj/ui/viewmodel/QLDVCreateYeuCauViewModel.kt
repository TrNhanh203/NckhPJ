package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class QLDVCreateYeuCauViewModel @Inject constructor(
    private val repository: YeuCauRepository
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
}



//package com.example.facilitiesmanagementpj.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
//import com.example.facilitiesmanagementpj.data.entity.YeuCau
//import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@HiltViewModel
//class QLDVCreateYeuCauViewModel @Inject constructor(
//    private val repository: YeuCauRepository
//) : ViewModel() {
//
//
//    fun removeChiTietYeuCau(chiTietId: Int) {
//        viewModelScope.launch {
//            repository.removeChiTietYeuCau(chiTietId)
//            _chiTietYeuCauList.value = _chiTietYeuCauList.value.filter { it.id != chiTietId }
//        }
//    }
//
//    fun updateYeuCauStatus(yeuCauId: Int, status: String) {
//        viewModelScope.launch {
//            repository.updateYeuCauStatus(yeuCauId, status)
//        }
//    }
//
//    private val _yeuCauId = MutableStateFlow<Int?>(null)
//    val yeuCauId: StateFlow<Int?> = _yeuCauId
//
//    private val _chiTietYeuCauList = MutableStateFlow<List<ChiTietYeuCau>>(emptyList())
//    val chiTietYeuCauList: StateFlow<List<ChiTietYeuCau>> = _chiTietYeuCauList
//
//    fun getYeuCauById(yeuCauId: Int, onResult: (YeuCau) -> Unit) {
//        viewModelScope.launch {
//            val yeuCau = repository.getYeuCauById(yeuCauId)
//            yeuCau?.let { onResult(it) }
//        }
//    }
//
//    fun setYeuCauId(yeuCauId: Int) {
//        _yeuCauId.value = yeuCauId
//    }
//
//    fun createYeuCau(taiKhoanId: Int, donViId: Int, moTa: String) {
//        viewModelScope.launch {
//            val id = repository.createYeuCau(taiKhoanId, donViId, moTa).toInt()
//            _yeuCauId.value = id
//        }
//    }
//
//    fun loadChiTietYeuCau(yeuCauId: Int) {
//        viewModelScope.launch {
//            repository.getChiTietYeuCau(yeuCauId).collect {
//                _chiTietYeuCauList.value = it
//            }
//        }
//    }
//}
//
