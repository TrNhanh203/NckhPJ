package com.example.facilitiesmanagementpj.ui.viewmodel


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
class DebugViewModel @Inject constructor(
    private val vaiTroRepository: VaiTroRepository,
    private val taiKhoanRepository: TaiKhoanRepository
) : ViewModel() {

    private val _vaiTroList = MutableStateFlow<List<VaiTro>>(emptyList())
    val vaiTroList: StateFlow<List<VaiTro>> = _vaiTroList

    private val _taiKhoanList = MutableStateFlow<List<TaiKhoanWithRole>>(emptyList())
    val taiKhoanList: StateFlow<List<TaiKhoanWithRole>> = _taiKhoanList

    private val _trangThaiList = MutableStateFlow<List<String>>(emptyList())
    val trangThaiList: StateFlow<List<String>> = _trangThaiList

    init {
        viewModelScope.launch {
            launch {
                taiKhoanRepository.getAllTaiKhoanVoiVaiTro().collect { list ->
                    println("DEBUG_TAIKHOAN: ${list.size} tài khoản")
                    _taiKhoanList.value = list
                }
            }

            launch {
                vaiTroRepository.getAllVaiTro().collect { list ->
                    println("DEBUG_VAITRO: ${list.size} vai trò")
                    _vaiTroList.value = list
                }
            }

            launch {
                taiKhoanRepository.getAllTrangThai().collect { list ->
                    println("DEBUG_TRANGTHAI: ${list.size} trạng thái")
                    _trangThaiList.value = list
                }
            }
        }
    }

}
