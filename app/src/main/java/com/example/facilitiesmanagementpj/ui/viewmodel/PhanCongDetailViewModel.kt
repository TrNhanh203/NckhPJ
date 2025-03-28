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
import androidx.core.net.toUri
import com.example.facilitiesmanagementpj.data.entity.PhanCongKtvWithTaiKhoan
import com.example.facilitiesmanagementpj.data.entity.TaiKhoan
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import kotlinx.coroutines.flow.first

@HiltViewModel
class PhanCongDetailViewModel @Inject constructor(
    private val phanCongRepository: PhanCongRepository,
    private val yeuCauRepository: YeuCauRepository,
    private val thietBiRepository: ThietBiRepository,
    private val anhMinhChungBaoCaoRepository: AnhMinhChungBaoCaoRepository,
    private val phongRepository: PhongRepository,
    private val tangRepository: TangRepository,
    private val dayRepository: DayRepository,
    private val donViRepository: DonViRepository,
    private val phanCongKtvRepository: PhanCongKtvRepository,
    private val taiKhoanRepository: TaiKhoanRepository
) : ViewModel() {

    private val _phanCong = MutableStateFlow<PhanCong?>(null)
    val phanCong: StateFlow<PhanCong?> = _phanCong

    private val _chiTietYeuCau = MutableStateFlow<ChiTietYeuCau?>(null)
    val chiTietYeuCau: StateFlow<ChiTietYeuCau?> = _chiTietYeuCau

    private val _yeuCau = MutableStateFlow<YeuCau?>(null)
    val yeuCau: StateFlow<YeuCau?> = _yeuCau

    private val _thietBi = MutableStateFlow<ThietBi?>(null)
    val thietBi: StateFlow<ThietBi?> = _thietBi

    private val _tenDonVi = MutableStateFlow("")
    val tenDonVi: StateFlow<String> = _tenDonVi

    private val _viTri = MutableStateFlow("")
    val viTri: StateFlow<String> = _viTri

    private val _taiKhoanYeuCau = MutableStateFlow<TaiKhoan?>(null)
    val taiKhoanYeuCau: StateFlow<TaiKhoan?> = _taiKhoanYeuCau

    private val _taiKhoanTaoPhanCong = MutableStateFlow<TaiKhoan?>(null)
    val taiKhoanTaoPhanCong: StateFlow<TaiKhoan?> = _taiKhoanTaoPhanCong

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri

    private val _dsKtv = MutableStateFlow<List<PhanCongKtvWithTaiKhoan>>(emptyList())
    val dsKtv: StateFlow<List<PhanCongKtvWithTaiKhoan>> = _dsKtv

    fun loadDsKtv(phanCongId: Int) {
        viewModelScope.launch {
            _dsKtv.value = phanCongRepository.getDsKtvByPhanCongId(phanCongId)
        }
    }


    fun loadPhanCongChiTiet(phanCongId: Int) {
        viewModelScope.launch {
            val pc = phanCongRepository.getPhanCongById(phanCongId)
            _phanCong.value = pc ?: return@launch

            val ct = pc.chiTietYeuCauId.let { yeuCauRepository.getChiTietYeuCauById(it) }
            _chiTietYeuCau.value = ct

            val yc = ct?.yeuCauId?.let { yeuCauRepository.getYeuCauById(it) }
            _yeuCau.value = yc

            val donVi = yc?.donViId?.let { donViRepository.getById(it) }
            _tenDonVi.value = donVi?.tenDonVi ?: "Không xác định"

            val taiKhoanYeuCau = yc?.taiKhoanId?.let { taiKhoanRepository.getTaiKhoanById(it).first() }
            _taiKhoanYeuCau.value = taiKhoanYeuCau

            val taiKhoanTaoPhanCong = pc.nguoiTaoPhanCong?.let { taiKhoanRepository.getTaiKhoanById(it).first() }
            _taiKhoanTaoPhanCong.value = taiKhoanTaoPhanCong

            pc.thietBiId.let {
                val tb = thietBiRepository.getThietBiById(it)
                _thietBi.value = tb

                val phong = phongRepository.getById(tb?.phongId ?: return@launch)
                val tang = tangRepository.getById(phong?.tangId ?: return@launch)
                val day = dayRepository.getById(tang?.dayId ?: return@launch)
                _viTri.value = listOfNotNull(day?.tenDay, tang.tenTang, phong.tenPhong).joinToString(" > ")
            }

            ct?.id?.let { loadMedia(it) }
        }
    }


    private suspend fun loadMedia(chiTietId: Int) {
        val images = anhMinhChungBaoCaoRepository.getImagesByChiTietBaoCaoId(chiTietId)
        val videos = anhMinhChungBaoCaoRepository.getVideosByChiTietBaoCaoId(chiTietId)
        _imageUris.value = images.map { it.urlAnh.toUri() }
        _videoUri.value = videos.firstOrNull()?.urlAnh?.toUri()
    }

    fun chapNhanPhanCongChoKtv(phanCongKtvId: Int) {
        viewModelScope.launch {
            phanCongKtvRepository.updateTrangThaiVaChapNhan(
                id = phanCongKtvId,
                trangThai = TrangThaiPhanCong.DA_CHAP_NHAN,
                daChapNhan = true
            )
            loadDsKtv(_phanCong.value?.id ?: return@launch)
        }
    }

    fun tuChoiPhanCongChoKtv(phanCongKtvId: Int, lyDo: String?) {
        viewModelScope.launch {
            phanCongKtvRepository.updateTuChoiPhanCong(
                id = phanCongKtvId,
                trangThai = TrangThaiPhanCong.DA_TU_CHOI,
                thoiGianTuChoi = System.currentTimeMillis(),
                lyDo = lyDo
            )
            loadDsKtv(_phanCong.value?.id ?: return@launch)
        }
    }

}
