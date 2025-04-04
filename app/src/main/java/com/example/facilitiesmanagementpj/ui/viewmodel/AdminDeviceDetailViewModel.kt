package com.example.facilitiesmanagementpj.ui.viewmodel

import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.entity.PhanCong
import com.example.facilitiesmanagementpj.data.repository.*
import com.example.facilitiesmanagementpj.data.utils.TrangThaiChungCuaPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDeviceDetailViewModel @Inject constructor(
    private val thietBiRepository: ThietBiRepository,
    private val yeuCauRepository: YeuCauRepository,
    private val anhMinhChungBaoCaoRepository: AnhMinhChungBaoCaoRepository,
    private val donViRepository: DonViRepository,
    private val phongRepository: PhongRepository,
    private val tangRepository: TangRepository,
    private val dayRepository: DayRepository,
    private val phanCongRepository: PhanCongRepository
) : ViewModel() {

    private val _thietBi = MutableStateFlow<ThietBi?>(null)
    val thietBi: StateFlow<ThietBi?> = _thietBi

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri

    private val _yeuCau = MutableStateFlow<YeuCau?>(null)
    val yeuCau: StateFlow<YeuCau?> = _yeuCau

    private val _chiTietYeuCau = MutableStateFlow<ChiTietYeuCau?>(null)
    val chiTietYeuCau: StateFlow<ChiTietYeuCau?> = _chiTietYeuCau

    private val _tenDonVi = MutableStateFlow("")
    val tenDonVi: StateFlow<String> = _tenDonVi

    private val _viTri = MutableStateFlow("")
    val viTri: StateFlow<String> = _viTri

    fun loadThietBi(thietBiId: Int) {
        viewModelScope.launch {
            val thietBi = thietBiRepository.getThietBiById(thietBiId)
            _thietBi.value = thietBi

            val phong = phongRepository.getById(thietBi?.phongId ?: return@launch)
            val tang = tangRepository.getById(phong?.tangId ?: return@launch)
            val day = dayRepository.getById(tang?.dayId ?: return@launch)
            _viTri.value = listOfNotNull(day?.tenDay, tang?.tenTang, phong?.tenPhong).joinToString(" > ")
        }
    }

    @OptIn(UnstableApi::class)
    fun loadChiTietYeuCau(yeuCauId: Int, thietBiId: Int) {
        viewModelScope.launch {
            val chiTiet = yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
            _chiTietYeuCau.value = chiTiet
            chiTiet?.let {
                loadMedia(it.id)
            }

            val yc = yeuCauRepository.getYeuCauById(yeuCauId)
            _yeuCau.value = yc

            val donVi = yc?.donViId?.let { donViRepository.getById(it) }
            _tenDonVi.value = donVi?.tenDonVi ?: "Không xác định"
        }
    }

    @OptIn(UnstableApi::class)
    private fun loadMedia(chiTietBaoCaoId: Int) {
        viewModelScope.launch {
            val images = anhMinhChungBaoCaoRepository.getImagesByChiTietBaoCaoId(chiTietBaoCaoId)
            val videos = anhMinhChungBaoCaoRepository.getVideosByChiTietBaoCaoId(chiTietBaoCaoId)
            _imageUris.value = images.map { Uri.parse(it.urlAnh) }
            _videoUri.value = videos.firstOrNull()?.let { Uri.parse(it.urlAnh) }
        }
    }

    private val _phanCongHienTai = MutableStateFlow<PhanCong?>(null)
    val phanCongHienTai: StateFlow<PhanCong?> = _phanCongHienTai

    fun checkPhanCongDaTao(chiTietId: Int) {
        viewModelScope.launch {
            _phanCongHienTai.value = phanCongRepository.getPhanCongByChiTietYeuCau(chiTietId)
        }
    }


    fun reloadPhanCongSauKhiTao(chiTietYeuCauId: Int) {
        viewModelScope.launch {
            _phanCongHienTai.value = phanCongRepository.getPhanCongByChiTietYeuCau(chiTietYeuCauId)
        }
    }


    fun taoPhanCong(
        chiTietYeuCauId: Int,
        thietBiId: Int,
        loaiPhanCong: String,
        ghiChu: String?,
        mucDoUuTien: Int,
        nguoiTaoId: Int
    ) {

        viewModelScope.launch {
            val phanCong = PhanCong(
                chiTietYeuCauId = chiTietYeuCauId,
                thietBiId = thietBiId,
                loaiPhanCong = loaiPhanCong,
                ghiChu = ghiChu,
                mucDoUuTien = mucDoUuTien,
                nguoiTaoPhanCong = nguoiTaoId,
                trangThai = TrangThaiChungCuaPhanCong.CHUA_BAT_DAU,
                soLuongKTVThamGia = 0
            )
            phanCongRepository.insert(phanCong)
            _phanCongHienTai.value = phanCongRepository.getPhanCongByChiTietYeuCau(chiTietYeuCauId)


        }
    }

}




