package com.example.facilitiesmanagementpj.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

// 5. PhongViewModel
@HiltViewModel
class PhongViewModel @Inject constructor(private val repository: PhongRepository) : ViewModel() {
    val allPhong: Flow<List<Phong>> = repository.getAllPhong()
    val allPhongWithLoaiPhong: Flow<List<PhongWithLoaiPhong>> = repository.getAllPhongWithLoaiPhong()
    val allPhongWithDetails: Flow<List<PhongWithDetails>> = repository.getAllPhongWithDetails()
    //val allPhongTheoDay: Flow<List<Phong>> = repository.getPhongTheoDay()

    fun insert(phong: Phong) = viewModelScope.launch {
        repository.insert(phong)
    }
    fun update(phong: Phong) = viewModelScope.launch {
        repository.update(phong)
    }
    fun delete(phong: Phong) = viewModelScope.launch {
        repository.delete(phong)
    }



    private val _donViIdChon = MutableStateFlow<Int?>(null)
    val donViIdChon: StateFlow<Int?> = _donViIdChon

    val danhSachPhong: Flow<List<Phong>> = _donViIdChon
        .filterNotNull()
        .flatMapLatest { donViId ->
            repository.getPhongTheoDonVi(donViId)
        }

    fun setDonViId(donViId: Int) {
        _donViIdChon.value = donViId
    }

    fun getPhongByTangId(tangId: Int?): Flow<List<Phong>> {
        return if (tangId != null) {
            repository.getPhongByTangId(tangId)
        } else {
            flowOf(emptyList()) // Trả về danh sách rỗng nếu chưa chọn tầng
        }
    }

    fun capNhatDonViId(phongId: Int, donViId: Int) = viewModelScope.launch {
        repository.capNhatDonViId(phongId, donViId)
    }


    fun ganDonViChoTatCaPhong() {
        viewModelScope.launch {
            Log.d("DEBUG", "Bắt đầu cập nhật DonViId cho tất cả phòng")
            val listPhong = repository.getAllPhongList()
            if (listPhong.isEmpty()) {
                Log.e("ERROR", "Không có phòng nào trong database!")
                return@launch
            }
            val mapping = mapOf(
                1 to listOf(19, 18 ),
                2 to listOf(1,2,3,4,5,6,7,8,9,10,11,12,13),

                3 to listOf(25, 22, ),
                4 to listOf(13, 7, 11, 8, 4),
                5 to listOf(25,24),
                6 to listOf(25),
                7 to listOf(23),
                8 to listOf(22),

                9 to listOf(26, 27,28,29,30,31),

                10 to listOf(22),
                29 to listOf(26),
                31 to listOf(30),
            )


            listPhong.forEach { phong ->
                val loaiPhongId = phong.loaiPhongId
                if (loaiPhongId == null) {
                    Log.e("ERROR", "Phòng ${phong.tenPhong} không có `loaiPhongId`!")
                    return@forEach
                }

                val danhSachDonVi = mapping[phong.loaiPhongId]
                val donViIdMoi = danhSachDonVi?.randomOrNull()

                if (donViIdMoi != null) {
                    repository.capNhatDonViId(phong.id, donViIdMoi)
                    Log.d("UPDATE", "Cập nhật ${phong.id} -> donViId: $donViIdMoi")
                }
                else {
                    Log.e("ERROR", "Không tìm thấy đơn vị phù hợp cho phòng: ${phong.tenPhong}")
                }
            }
        }
    }
}