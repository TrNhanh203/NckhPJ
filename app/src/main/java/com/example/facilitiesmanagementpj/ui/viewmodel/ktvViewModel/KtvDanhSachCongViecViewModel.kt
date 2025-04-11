package com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.facilitiesmanagementpj.data.repository.*
import com.example.facilitiesmanagementpj.data.utils.TrangThaiChungCuaPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri
import com.example.facilitiesmanagementpj.data.dao.PhanCongKtvDao
import com.example.facilitiesmanagementpj.data.relation.PhanCongKtvWithFullInfo
import com.example.facilitiesmanagementpj.data.utils.LoaiAnhMinhChungLamViec

@HiltViewModel
class KtvDanhSachCongViecViewModel @Inject constructor(
    private val phanCongKtvDao: PhanCongKtvDao,
    private val anhRepo: AnhMinhChungLamViecRepository
) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<PhanCongKtvWithFullInfo>>(emptyList())
    val allTasks: StateFlow<List<PhanCongKtvWithFullInfo>> = _allTasks

    fun loadTasks(ktvId: Int) {
        viewModelScope.launch {
            _allTasks.value = phanCongKtvDao.getWithFullInfo(ktvId)
        }
    }

    suspend fun tinhThoiGianConLaiThucTeThamKhao(
        phanCongKtvId: Int,
        pcKtvRepo: PhanCongKtvRepository,
        anhRepo: AnhMinhChungLamViecRepository
    ): Int {
        val pc = pcKtvRepo.getById(phanCongKtvId)
        val thoiGianDuKienPhut = pc?.thoiGianDuKien ?: 0

        val listAnh = anhRepo.getByPhanCongKtvId(phanCongKtvId)
            .filter {
                it.loaiAnh == LoaiAnhMinhChungLamViec.CHECK_IN ||
                        it.loaiAnh == LoaiAnhMinhChungLamViec.TAM_NGHI
            }
            .sortedBy { it.thoiGianTaiLen }

        val pairs = mutableListOf<Pair<Long, Long>>()
        var currentCheckInTime: Long? = null

        for (anh in listAnh) {
            when (anh.loaiAnh) {
                LoaiAnhMinhChungLamViec.CHECK_IN -> {
                    if (currentCheckInTime == null) {
                        currentCheckInTime = anh.thoiGianTaiLen
                    }
                }

                LoaiAnhMinhChungLamViec.TAM_NGHI -> {
                    if (currentCheckInTime != null) {
                        pairs.add(currentCheckInTime to anh.thoiGianTaiLen)
                        currentCheckInTime = null
                    }
                }
            }
        }

        currentCheckInTime?.let {
            pairs.add(it to System.currentTimeMillis())
        }

        val tongMillis = pairs.sumOf { (start, end) -> end - start }
        val soPhutDaLam = (tongMillis / 60_000L).toInt()

        val soPhutConLai = (thoiGianDuKienPhut - soPhutDaLam).coerceAtLeast(0)
        return soPhutConLai
    }


}
