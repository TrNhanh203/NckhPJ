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
import com.example.facilitiesmanagementpj.data.repository.AnhMinhChungBaoCaoRepository
import com.example.facilitiesmanagementpj.data.repository.ThietBiRepository
import com.example.facilitiesmanagementpj.data.repository.YeuCauRepository
import com.example.facilitiesmanagementpj.data.utils.deleteFileFromFirebaseStorage
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
    private val anhMinhCungBaoCaoRepository: AnhMinhChungBaoCaoRepository,
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
            val chiTietYeuCau =
                yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
            chiTietYeuCau?.let { onLoaded(it) }
        }
    }

    @OptIn(UnstableApi::class)
    fun addChiTietYeuCau(
        yeuCauId: Int,
        thietBiId: Int,
        loaiYeuCau: String,
        moTa: String,
        images: List<Uri>,
        video: Uri?
    ) {
        viewModelScope.launch {
            Log.d(
                "ThietBiDetailViewModel",
                "Adding ChiTietYeuCau with images: $images and video: $video"
            )
            val chiTietYeuCauId =
                yeuCauRepository.addThietBiToYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
            saveMedia(chiTietYeuCauId, images, video)
        }
    }

    //    @OptIn(UnstableApi::class)
//    fun updateChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String, images: List<Uri>, video: Uri?) {
//        viewModelScope.launch {
//            Log.d("ThietBiDetailViewModel", "Updating ChiTietYeuCau with images: $images and video: $video")
//            val chiTietYeuCau = yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
//            chiTietYeuCau?.let {
//                yeuCauRepository.updateChiTietYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
//                saveMedia(it.id, images, video)
//            }
//        }
//    }
    @OptIn(UnstableApi::class)
    fun updateChiTietYeuCau(
        yeuCauId: Int,
        thietBiId: Int,
        loaiYeuCau: String,
        moTa: String,
        images: List<Uri>,
        video: Uri?
    ) {
        viewModelScope.launch {
            Log.d(
                "ThietBiDetailViewModel",
                "Updating ChiTietYeuCau with images: $images and video: $video"
            )
            val chiTietYeuCau =
                yeuCauRepository.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
            chiTietYeuCau?.let {
                yeuCauRepository.updateChiTietYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
                synchronizeMedia(it.id, images, video)
            }
        }
    }

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris: StateFlow<List<Uri>> = _imageUris

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri: StateFlow<Uri?> = _videoUri

    @OptIn(UnstableApi::class)
    fun loadMedia(chiTietBaoCaoId: Int) {
        viewModelScope.launch {
            val images = anhMinhCungBaoCaoRepository.getImagesByChiTietBaoCaoId(chiTietBaoCaoId)
            val videos = anhMinhCungBaoCaoRepository.getVideosByChiTietBaoCaoId(chiTietBaoCaoId)
            _imageUris.value = images.map { Uri.parse(it.urlAnh) }
            _videoUri.value = videos.firstOrNull()?.let { Uri.parse(it.urlAnh) }
            Log.d(
                "ThietBiDetailViewModel",
                "Loaded images: ${_imageUris.value}, video: ${_videoUri.value}"
            )
        }
    }

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


    @OptIn(UnstableApi::class)
    private suspend fun synchronizeMedia(chiTietBaoCaoId: Int, images: List<Uri>, video: Uri?) {
        val currentImages = anhMinhCungBaoCaoRepository.getImagesByChiTietBaoCaoId(chiTietBaoCaoId)
            .map { it.urlAnh }
        val currentVideos = anhMinhCungBaoCaoRepository.getVideosByChiTietBaoCaoId(chiTietBaoCaoId)
            .map { it.urlAnh }

        val newImagePaths = images.map { it.toString() }
        val newVideoPath = video?.toString()

        // Delete removed images
        currentImages.filter { it !in newImagePaths }.forEach { path ->
            yeuCauRepository.deleteAnhMinhChungByPath(path)
            deleteFileFromFirebaseStorage(path)
        }

        // Delete removed videos
        currentVideos.filter { it != newVideoPath }.forEach { path ->
            yeuCauRepository.deleteAnhMinhChungByPath(path)
            deleteFileFromFirebaseStorage(path)
        }

        // Save new media
        saveMedia(chiTietBaoCaoId, images, video)
    }


}



