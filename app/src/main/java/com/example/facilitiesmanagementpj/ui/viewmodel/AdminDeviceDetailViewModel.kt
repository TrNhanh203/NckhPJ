package com.example.facilitiesmanagementpj.ui.viewmodel

import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.data.entity.YeuCau
import com.example.facilitiesmanagementpj.data.repository.AnhMinhChungBaoCaoRepository
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
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
    private val anhMinhChungBaoCaoRepository: AnhMinhChungBaoCaoRepository
) : ViewModel() {

    private val _thietBi = MutableStateFlow<ThietBi?>(null)
    val thietBi: StateFlow<ThietBi?> = _thietBi

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri

    private val _yeuCau = MutableStateFlow<YeuCau?>(null)
    val yeuCau: StateFlow<YeuCau?> = _yeuCau

    fun loadThietBi(thietBiId: Int) {
        viewModelScope.launch {
            val thietBi = thietBiRepository.getThietBiById(thietBiId)
            _thietBi.value = thietBi
        }
    }

    @OptIn(UnstableApi::class)
    fun loadChiTietYeuCau(yeuCauId: Int, thietBiId: Int) {
        viewModelScope.launch {
            val chiTietYeuCau = yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
            chiTietYeuCau?.let {
                loadMedia(it.id)
            }
            _yeuCau.value = yeuCauRepository.getYeuCauById(yeuCauId)
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
}


//@HiltViewModel
//class AdminDeviceDetailViewModel @Inject constructor(
//    private val thietBiRepository: ThietBiRepository,
//    private val yeuCauRepository: YeuCauRepository,
//    private val anhMinhChungBaoCaoRepository: AnhMinhChungBaoCaoRepository
//) : ViewModel() {
//
//
//
//    private val _thietBi = MutableStateFlow<ThietBi?>(null)
//    val thietBi: StateFlow<ThietBi?> = _thietBi
//
//    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
//    val imageUris: StateFlow<List<Uri>> = _imageUris
//
//    private val _videoUri = MutableStateFlow<Uri?>(null)
//    val videoUri: StateFlow<Uri?> = _videoUri
//
//    private val _yeuCau = MutableStateFlow<YeuCau?>(null)
//    val yeuCau: StateFlow<YeuCau?> = _yeuCau
//
//    fun loadThietBi(thietBiId: Int) {
//        viewModelScope.launch {
//            val thietBi = thietBiRepository.getThietBiById(thietBiId)
//            _thietBi.value = thietBi
//        }
//    }
//
//    @OptIn(UnstableApi::class)
//    fun loadChiTietYeuCau(yeuCauId: Int, thietBiId: Int) {
//        viewModelScope.launch {
//            val chiTietYeuCau = yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
//            chiTietYeuCau?.let {
//                loadMedia(it.id)
//            }
//            _yeuCau.value = yeuCauRepository.getYeuCauById(yeuCauId)
//        }
//    }
//
//    @OptIn(UnstableApi::class)
//    private fun loadMedia(chiTietBaoCaoId: Int) {
//        viewModelScope.launch {
//            val images = anhMinhChungBaoCaoRepository.getImagesByChiTietBaoCaoId(chiTietBaoCaoId)
//            val videos = anhMinhChungBaoCaoRepository.getVideosByChiTietBaoCaoId(chiTietBaoCaoId)
//            _imageUris.value = images.map { Uri.parse(it.urlAnh) }
//            _videoUri.value = videos.firstOrNull()?.let { Uri.parse(it.urlAnh) }
//        }
//    }
//}