package com.example.facilitiesmanagementpj.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.TaiKhoanWithRole
import com.example.facilitiesmanagementpj.data.entity.VaiTro
import com.example.facilitiesmanagementpj.data.repository.TaiKhoanRepository
import com.example.facilitiesmanagementpj.data.repository.VaiTroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminAccountViewModel @Inject constructor(private val repository: TaiKhoanRepository,
                                                private val vaiTroRepository: VaiTroRepository
) : ViewModel() {

    private val _taiKhoanList = MutableStateFlow<List<TaiKhoanWithRole>>(emptyList())
    val taiKhoanList: StateFlow<List<TaiKhoanWithRole>> = _taiKhoanList

    private val _vaiTroList = MutableStateFlow<List<VaiTro>>(emptyList())
    val vaiTroList: StateFlow<List<VaiTro>> = _vaiTroList


    private val _trangThaiList = MutableStateFlow<List<String>>(emptyList())
    val trangThaiList: StateFlow<List<String>> = _trangThaiList

    // Biến lọc dữ liệu
    private val _selectedVaiTro = MutableStateFlow<String?>(null)
    val selectedVaiTro: StateFlow<String?> = _selectedVaiTro

    private val _selectedTrangThai = MutableStateFlow<String?>(null)
    val selectedTrangThai: StateFlow<String?> = _selectedTrangThai

    init {
        viewModelScope.launch {
            launch{
                repository.getAllTaiKhoanVoiVaiTro().collect { _taiKhoanList.value = it }
            }
            launch{
                vaiTroRepository.getAllVaiTro().collect { _vaiTroList.value = it }
            }
            launch{
                repository.getAllTrangThai().collect { _trangThaiList.value = it }
            }




        }
    }

    // Cập nhật bộ lọc
    fun setVaiTroFilter(vaiTro: String?) {
        _selectedVaiTro.value = vaiTro
    }

    fun setTrangThaiFilter(trangThai: String?) {
        _selectedTrangThai.value = trangThai
    }

    // Lọc danh sách tài khoản
    val filteredTaiKhoanList: StateFlow<List<TaiKhoanWithRole>> = combine(
        taiKhoanList, selectedVaiTro, selectedTrangThai
    ) { list, vaiTro, trangThai ->
        list.filter {
            (vaiTro == null || it.tenVaiTro == vaiTro) &&
                    (trangThai == null || it.trangThai == trangThai)
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

