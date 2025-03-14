package com.example.facilitiesmanagementpj.ui.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.example.facilitiesmanagementpj.data.entity.VaiTro
import com.example.facilitiesmanagementpj.data.repository.TaiKhoanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAccountViewModel @Inject constructor(private val repository: TaiKhoanRepository) : ViewModel() {

    private val _taiKhoanList = MutableStateFlow<List<TaiKhoanWithRole>>(emptyList())
    val taiKhoanList: StateFlow<List<TaiKhoanWithRole>> = _taiKhoanList

    private val _vaiTroList = MutableStateFlow<List<VaiTro>>(emptyList())
    val vaiTroList: StateFlow<List<VaiTro>> = _vaiTroList

    private val _trangThaiList = MutableStateFlow<List<String>>(emptyList())
    val trangThaiList: StateFlow<List<String>> = _trangThaiList

    init {
        viewModelScope.launch {
            repository.getAllTaiKhoanVoiVaiTro().collect { _taiKhoanList.value = it }
            repository.getAllVaiTro().collect { _vaiTroList.value = it }
            repository.getAllTrangThai().collect { _trangThaiList.value = it }
        }
    }
}
