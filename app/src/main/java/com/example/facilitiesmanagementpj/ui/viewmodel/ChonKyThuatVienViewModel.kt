package com.example.facilitiesmanagementpj.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.dao.KyThuatVienDao
import com.example.facilitiesmanagementpj.data.entity.ChuyenMon
import com.example.facilitiesmanagementpj.data.entity.KyThuatVienWithTaiKhoan
import com.example.facilitiesmanagementpj.data.relation.KyThuatVienWithSoTask
import com.example.facilitiesmanagementpj.data.relation.KyThuatVienWithSoTaskWithTrangThaiPhanCong
import com.example.facilitiesmanagementpj.data.repository.ChuyenMonRepository
import com.example.facilitiesmanagementpj.data.repository.PhanCongRepository
import com.example.facilitiesmanagementpj.data.repository.KyThuatVienRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChonKyThuatVienViewModel @Inject constructor(
    private val kyThuatVienDao: KyThuatVienDao,
    private val kyThuatVienRepo: KyThuatVienRepository,
    private val phanCongRepo: PhanCongRepository,
    private val chuyenMonRepo: ChuyenMonRepository
) : ViewModel() {

    var selectedTrangThai by mutableStateOf<String?>(null)
    var selectedChuyenMonIds by mutableStateOf<Set<Int>>(emptySet())
    var searchText by mutableStateOf("")
    var allChuyenMon by mutableStateOf<List<ChuyenMon>>(emptyList())

    private val _danhSachKTV = MutableStateFlow<List<KyThuatVienWithSoTaskWithTrangThaiPhanCong>>(emptyList())
    val danhSachKTV: StateFlow<List<KyThuatVienWithSoTaskWithTrangThaiPhanCong>> = _danhSachKTV

    private var currentPhanCongId: Int? = null

    fun loadDanhSachKTV(phanCongId: Int) {
        currentPhanCongId = phanCongId
        loadChuyenMon()
        applyFilters()
    }

    private fun loadChuyenMon() {
        viewModelScope.launch {
            allChuyenMon = chuyenMonRepo.getAllChuyenMon()
        }
    }

    fun toggleChuyenMon(id: Int, checked: Boolean) {
        selectedChuyenMonIds = if (checked) selectedChuyenMonIds + id else selectedChuyenMonIds - id
    }

    fun filterByTrangThai(trangThai: String?) {
        selectedTrangThai = trangThai
        applyFilters()
    }

    fun resetFilters() {
        selectedTrangThai = null
        selectedChuyenMonIds = emptySet()
        searchText = ""
        applyFilters()
    }

    fun applyFilters() {
        viewModelScope.launch {
            // ktv + tai khoan
            val rawList = kyThuatVienDao.getByTrangThaiWithTaiKhoan(selectedTrangThai)

            //phan_cong_Ktv + tai khoan
            val danhSachPhanCong_Ktv = currentPhanCongId?.let {
                phanCongRepo.getDsKtvByPhanCongId(it)
            } ?: emptyList()

            val danhSach = rawList.filter { ktv ->
                selectedChuyenMonIds.isEmpty() || kyThuatVienRepo.hasAnyChuyenMon(
                    kyThuatVienId = ktv.kyThuatVien.id,
                    chuyenMonIds = selectedChuyenMonIds
                )
            }.filter {
                it.taiKhoan.hoTen?.contains(searchText, ignoreCase = true) == true || searchText.isBlank()
            }

            val enrichedList = danhSach.map {
                val soTask = phanCongRepo.getSoTaskDangLam(it.taiKhoan.id)
                val trangThaiPhanCong = danhSachPhanCong_Ktv.find { pc -> pc.taiKhoan.id == it.taiKhoan.id }?.phanCongKtv?.trangThai
                KyThuatVienWithSoTaskWithTrangThaiPhanCong(it, soTask, trangThaiPhanCong)
            }

            _danhSachKTV.value = enrichedList
        }
    }
}


//@HiltViewModel
//class ChonKyThuatVienViewModel @Inject constructor(
//    private val kyThuatVienDao: KyThuatVienDao,
//    private val kyThuatVienRepo: KyThuatVienRepository,
//    private val phanCongRepo: PhanCongRepository,
//    private val chuyenMonRepo: ChuyenMonRepository
//) : ViewModel() {
//
//    var selectedTrangThai by mutableStateOf<String?>(null)
//    var selectedChuyenMonIds by mutableStateOf<Set<Int>>(emptySet())
//    var searchText by mutableStateOf("")
//    var allChuyenMon by mutableStateOf<List<ChuyenMon>>(emptyList())
//
//    private val _danhSachKTV = MutableStateFlow<List<KyThuatVienWithSoTask>>(emptyList())
//    val danhSachKTV: StateFlow<List<KyThuatVienWithSoTask>> = _danhSachKTV
//
//    fun loadDanhSachKTV() {
//        loadChuyenMon()
//        applyFilters()
//    }
//
//    fun loadChuyenMon() {
//        viewModelScope.launch {
//            allChuyenMon = chuyenMonRepo.getAllChuyenMon()
//        }
//    }
//
//
//    fun toggleChuyenMon(id: Int, checked: Boolean) {
//        selectedChuyenMonIds = if (checked) selectedChuyenMonIds + id else selectedChuyenMonIds - id
//    }
//
//    fun filterByTrangThai(trangThai: String?) {
//        selectedTrangThai = trangThai
//        applyFilters()
//    }
//
//    fun resetFilters() {
//        selectedTrangThai = null
//        selectedChuyenMonIds = emptySet()
//        searchText = ""
//        applyFilters()
//    }
//
//    fun applyFilters() {
//        viewModelScope.launch {
//            val rawList = kyThuatVienDao.getByTrangThaiWithTaiKhoan(selectedTrangThai)
//
//            val danhSach = rawList.filter { ktv ->
//                selectedChuyenMonIds.isEmpty() || kyThuatVienRepo.hasAnyChuyenMon(
//                    kyThuatVienId = ktv.kyThuatVien.id,
//                    chuyenMonIds = selectedChuyenMonIds
//                )
//            }.filter {
//                it.taiKhoan.hoTen?.contains(searchText, ignoreCase = true) == true || searchText.isBlank()
//            }
//
//            val enrichedList = danhSach.map {
//                val soTask = phanCongRepo.getSoTaskDangLam(it.taiKhoan.id)
//                KyThuatVienWithSoTask(it, soTask)
//            }
//
//            _danhSachKTV.value = enrichedList
//        }
//    }
//}
