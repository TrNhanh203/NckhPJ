package com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec
import com.example.facilitiesmanagementpj.data.repository.AnhMinhChungLamViecRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TienTrinhLamViecViewModel @Inject constructor(
    private val anhRepo: AnhMinhChungLamViecRepository
) : ViewModel() {

    private val _danhSachGoc = MutableStateFlow<List<NhomAnhLamViec>>(emptyList())
    private val _danhSachNhom = MutableStateFlow<List<NhomAnhLamViec>>(emptyList())
    val danhSachNhom: StateFlow<List<NhomAnhLamViec>> = _danhSachNhom

    private val _loaiLoc = MutableStateFlow<String?>(null)
    val loaiLoc: StateFlow<String?> = _loaiLoc

    private val _sapXepGiam = MutableStateFlow(true)
    val sapXepGiam: StateFlow<Boolean> = _sapXepGiam

    suspend fun loadTienTrinh(phanCongKtvId: Int) {
        val danhSach = anhRepo.getByPhanCongKtvId(phanCongKtvId)
            .filter { it.loaiAnh != null }
            .groupBy { it.loaiAnh to it.thoiGianTaiLen.let { tg -> tg / (1000 * 60) } } // nhóm theo loại ảnh và thời gian gần nhau

        val nhom = danhSach.map { (k, v) ->
            NhomAnhLamViec(
                loaiAnh = k.first,
                thoiGian = v.minOfOrNull { it.thoiGianTaiLen } ?: 0L,
                danhSachAnh = v.sortedBy { it.thoiGianTaiLen }
            )
        }

        _danhSachGoc.value = nhom
        capNhatLocVaSapXep()
    }

    fun setLoaiLoc(loai: String?) {
        _loaiLoc.value = loai
        capNhatLocVaSapXep()
    }

    fun toggleSapXep() {
        _sapXepGiam.value = !_sapXepGiam.value
        capNhatLocVaSapXep()
    }

    private fun capNhatLocVaSapXep() {
        val loc = _loaiLoc.value
        val sapGiam = _sapXepGiam.value

        var danhSach = _danhSachGoc.value

        if (!loc.isNullOrBlank()) {
            danhSach = danhSach.filter { it.loaiAnh == loc }
        }

        danhSach = if (sapGiam) {
            danhSach.sortedByDescending { it.thoiGian }
        } else {
            danhSach.sortedBy { it.thoiGian }
        }

        _danhSachNhom.value = danhSach
    }

    data class NhomAnhLamViec(
        val loaiAnh: String,
        val thoiGian: Long,
        val danhSachAnh: List<AnhMinhChungLamViec>
    )
}



//@HiltViewModel
//class TienTrinhLamViecViewModel @Inject constructor(
//    private val anhRepo: AnhMinhChungLamViecRepository) : ViewModel() {
//
//    data class NhomAnhLamViec(
//        val loaiAnh: String,
//        val thoiGian: Long,
//        val danhSachAnh: List<AnhMinhChungLamViec>
//    )
//
//    private val _danhSachNhom = MutableStateFlow<List<NhomAnhLamViec>>(emptyList())
//    val danhSachNhom: StateFlow<List<NhomAnhLamViec>> = _danhSachNhom.asStateFlow()
//
//    private val _loaiLoc = MutableStateFlow<String?>(null) // null = tất cả
//    val loaiLoc: StateFlow<String?> = _loaiLoc.asStateFlow()
//
//    private val _sapXepGiam = MutableStateFlow(true)
//    val sapXepGiam: StateFlow<Boolean> = _sapXepGiam.asStateFlow()
//
//    fun loadTienTrinh(phanCongKtvId: Int) {
//        viewModelScope.launch {
//            val allImages = anhRepo.getByPhanCongKtvId(phanCongKtvId)
//            val nhom = groupAnhTheoNhom(allImages)
//            _danhSachNhom.value = applyLocVaSapXep(nhom)
//        }
//    }
//
//    fun setLoaiLoc(loai: String?) {
//        _loaiLoc.value = loai
//        _danhSachNhom.value = applyLocVaSapXep(_danhSachNhom.value)
//    }
//
//    fun toggleSapXep() {
//        _sapXepGiam.value = !_sapXepGiam.value
//        _danhSachNhom.value = applyLocVaSapXep(_danhSachNhom.value)
//    }
//
//    private fun applyLocVaSapXep(ds: List<NhomAnhLamViec>): List<NhomAnhLamViec> {
//        var result = ds
//        loaiLoc.value?.let { loai ->
//            result = result.filter { it.loaiAnh == loai }
//        }
//        return if (sapXepGiam.value)
//            result.sortedByDescending { it.thoiGian }
//        else
//            result.sortedBy { it.thoiGian }
//    }
//
//    private fun groupAnhTheoNhom(ds: List<AnhMinhChungLamViec>): List<NhomAnhLamViec> {
//        val sorted = ds.sortedBy { it.thoiGianTaiLen }
//        val nhomList = mutableListOf<NhomAnhLamViec>()
//
//        var currentNhom = mutableListOf<AnhMinhChungLamViec>()
//        for (img in sorted) {
//            if (currentNhom.isEmpty()) {
//                currentNhom.add(img)
//            } else {
//                val last = currentNhom.last()
//                val sameType = last.loaiAnh == img.loaiAnh
//                val closeTime = (img.thoiGianTaiLen - last.thoiGianTaiLen) <= 60000L
//                if (sameType && closeTime) {
//                    currentNhom.add(img)
//                } else {
//                    nhomList.add(
//                        NhomAnhLamViec(
//                            loaiAnh = last.loaiAnh,
//                            thoiGian = currentNhom.first().thoiGianTaiLen,
//                            danhSachAnh = currentNhom.toList()
//                        )
//                    )
//                    currentNhom = mutableListOf(img)
//                }
//            }
//        }
//        if (currentNhom.isNotEmpty()) {
//            nhomList.add(
//                NhomAnhLamViec(
//                    loaiAnh = currentNhom.first().loaiAnh,
//                    thoiGian = currentNhom.first().thoiGianTaiLen,
//                    danhSachAnh = currentNhom.toList()
//                )
//            )
//        }
//
//        return nhomList
//    }
//}
