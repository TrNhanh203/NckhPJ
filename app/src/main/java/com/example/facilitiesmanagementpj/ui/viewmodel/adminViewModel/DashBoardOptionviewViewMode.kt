package com.example.facilitiesmanagementpj.ui.viewmodel.adminViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.example.facilitiesmanagementpj.data.dao.ThietBiDao
import com.example.facilitiesmanagementpj.data.dao.YeuCauDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashBoardOptionviewViewMode @Inject constructor(
    private val yeuCauDao: YeuCauDao,
    private val thietBiDao: ThietBiDao
) : ViewModel() {

    private val _statusCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val statusCounts: StateFlow<Map<String, Int>> = _statusCounts

    private val _deviceStatusCounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val deviceStatusCounts: StateFlow<Map<String, Int>> = _deviceStatusCounts

    fun loadData(currentUser: TaiKhoanWithRole) {
        viewModelScope.launch {
            val yeuCaus = if (currentUser.vaiTroId == 1) {
                yeuCauDao.getYeuCauTrong1ThangGanNhat()
            } else {
                yeuCauDao.getYeuCauTheoDonViTrong1ThangGanNhat(currentUser.donViId!!)
            }
            val grouped = yeuCaus.groupingBy { it.trangThai }.eachCount()
            _statusCounts.value = grouped
        }
    }

    fun loadDeviceData(currentUser: TaiKhoanWithRole) {
        viewModelScope.launch {
            val thietBis = if (currentUser.vaiTroId == 1) {
                thietBiDao.getTatCaThietBi()
            } else {
                thietBiDao.getThietBiTheoDonVi(currentUser.donViId!!)
            }
            val grouped = thietBis.groupingBy { it.trangThai }.eachCount()
            _deviceStatusCounts.value = grouped
        }
    }
}


