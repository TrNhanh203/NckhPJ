package com.example.facilitiesmanagementpj.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.facilitiesmanagementpj.data.entity.ChiTietYeuCau
import com.example.facilitiesmanagementpj.data.entity.ThietBi
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import com.example.facilitiesmanagementpj.data.utils.uploadFileToFirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ThietBiDetailViewModel @Inject constructor(
    private val thietBiRepository: ThietBiRepository,
    private val yeuCauRepository: YeuCauRepository,
    application: Application
) : AndroidViewModel(application) {


    private val _thietBi = MutableStateFlow<ThietBi?>(null)
    val thietBi: StateFlow<ThietBi?> = _thietBi

    fun loadThietBi(thietBiId: Int) {
        viewModelScope.launch {
            val thietBi = thietBiRepository.getThietBiById(thietBiId)
            _thietBi.value = thietBi
        }
    }

    fun loadChiTietYeuCau(yeuCauId: Int, thietBiId: Int, onLoaded: (ChiTietYeuCau) -> Unit) {
        viewModelScope.launch {
            val chiTietYeuCau = yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
            chiTietYeuCau?.let { onLoaded(it) }
        }
    }

    @OptIn(UnstableApi::class)
    fun addChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String, images: List<Uri>, video: Uri?) {
        viewModelScope.launch {
            Log.d("ThietBiDetailViewModel", "Adding ChiTietYeuCau with images: $images and video: $video")
            yeuCauRepository.addThietBiToYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
            saveMedia(yeuCauId, images, video)
        }
    }

    @OptIn(UnstableApi::class)
    fun updateChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String, images: List<Uri>, video: Uri?) {
        viewModelScope.launch {
            Log.d("ThietBiDetailViewModel", "Updating ChiTietYeuCau with images: $images and video: $video")
            yeuCauRepository.updateChiTietYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
            saveMedia(yeuCauId, images, video)
        }
    }

//    @OptIn(UnstableApi::class)
//    private suspend fun saveMedia(chiTietBaoCaoId: Int, images: List<Uri>, video: Uri?) {
//        images.forEach { uri ->
//            val fileName = "image_${System.currentTimeMillis()}.jpg"
//            val filePath = uploadFileToFirebaseStorage(uri, fileName)
//            filePath?.let {
//                yeuCauRepository.insertAnhMinhChung(chiTietBaoCaoId, it, "image")
//            } ?: Log.e("saveMedia", "Failed to upload image: $uri")
//        }
//        video?.let { uri ->
//            val fileName = "video_${System.currentTimeMillis()}.mp4"
//            val filePath = uploadFileToFirebaseStorage(uri, fileName)
//            filePath?.let {
//                yeuCauRepository.insertAnhMinhChung(chiTietBaoCaoId, it, "video")
//            } ?: Log.e("saveMedia", "Failed to upload video: $uri")
//        }
//    }
    @OptIn(UnstableApi::class)
    private suspend fun saveMedia(chiTietBaoCaoId: Int, images: List<Uri>, video: Uri?) {
        images.forEach { uri ->
            val fileName = "image_${System.currentTimeMillis()}.jpg"
            val filePath = uploadFileToFirebaseStorage(uri, fileName)
            filePath?.let {
                //Log.d("saveMedia", "Inserting image into database: $filePath")
                yeuCauRepository.insertAnhMinhChung(chiTietBaoCaoId, it, "image")
            } //?: Log.e("saveMedia", "Failed to upload image: $uri")
        }
        video?.let { uri ->
            val fileName = "video_${System.currentTimeMillis()}.mp4"
            val filePath = uploadFileToFirebaseStorage(uri, fileName)
            filePath?.let {
                //Log.d("saveMedia", "Inserting video into database: $filePath")
                yeuCauRepository.insertAnhMinhChung(chiTietBaoCaoId, it, "video")
            } //?: Log.e("saveMedia", "Failed to upload video: $uri")
        }
    }
}


//@HiltViewModel
//class ThietBiDetailViewModel @Inject constructor(
//    private val thietBiRepository: ThietBiRepository,
//    private val yeuCauRepository: YeuCauRepository,
//    private val context: Context
//) : ViewModel() {
//
//    private val _thietBi = MutableStateFlow<ThietBi?>(null)
//    val thietBi: StateFlow<ThietBi?> = _thietBi
//
//    fun loadThietBi(thietBiId: Int) {
//        viewModelScope.launch {
//            val thietBi = thietBiRepository.getThietBiById(thietBiId)
//            _thietBi.value = thietBi
//        }
//    }
//
////    fun addChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String) {
////        viewModelScope.launch {
////            yeuCauRepository.addThietBiToYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
////        }
////    }
////
////    fun updateChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String) {
////        viewModelScope.launch {
////            yeuCauRepository.updateChiTietYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
////        }
////    }
//
//    fun loadChiTietYeuCau(yeuCauId: Int, thietBiId: Int, onLoaded: (ChiTietYeuCau) -> Unit) {
//        viewModelScope.launch {
//            val chiTietYeuCau = yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
//            chiTietYeuCau?.let { onLoaded(it) }
//        }
//    }
//
//    @OptIn(UnstableApi::class)
//    fun addChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String, images: List<Uri>, video: Uri?) {
//        viewModelScope.launch {
//            Log.d("ThietBiDetailViewModel", "Adding ChiTietYeuCau with images: $images and video: $video")
//            yeuCauRepository.addThietBiToYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
//            saveMedia(yeuCauId, images, video)
//        }
//    }
//
//    @OptIn(UnstableApi::class)
//    fun updateChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String, images: List<Uri>, video: Uri?) {
//        viewModelScope.launch {
//            Log.d("ThietBiDetailViewModel", "Updating ChiTietYeuCau with images: $images and video: $video")
//            yeuCauRepository.updateChiTietYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
//            saveMedia(yeuCauId, images, video)
//        }
//    }
//
//    private suspend fun saveMedia(chiTietBaoCaoId: Int, images: List<Uri>, video: Uri?) {
//        val imageDir = "D:\\QuangNhanh\\Workspace\\NCKH\\NckhLocalServer\\Images"
//        val videoDir = "D:\\QuangNhanh\\Workspace\\NCKH\\NckhLocalServer\\Videos"
//
//        images.forEach { uri ->
//            val fileName = "image_${System.currentTimeMillis()}.jpg"
//            val filePath = "$imageDir\\$fileName"
//            if (saveFileToExternalStorage(context, uri, filePath)) {
//                yeuCauRepository.insertAnhMinhChung(chiTietBaoCaoId, filePath, "image")
//            }
//        }
//        video?.let { uri ->
//            val fileName = "video_${System.currentTimeMillis()}.mp4"
//            val filePath = "$videoDir\\$fileName"
//            if (saveFileToExternalStorage(context, uri, filePath)) {
//                yeuCauRepository.insertAnhMinhChung(chiTietBaoCaoId, filePath, "video")
//            }
//        }
//    }
//}
