package com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel

import android.net.Uri
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.facilitiesmanagementpj.data.entity.AnhMinhChungLamViec
import com.example.facilitiesmanagementpj.data.repository.AnhMinhChungLamViecRepository
import com.example.facilitiesmanagementpj.data.repository.PhanCongKtvRepository
import com.example.facilitiesmanagementpj.data.repository.PhanCongRepository
import com.example.facilitiesmanagementpj.data.utils.LoaiAnhMinhChungLamViec
import com.example.facilitiesmanagementpj.data.utils.TrangThaiPhanCong
import com.example.facilitiesmanagementpj.data.utils.uploadFileToFirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class KtvLamViecViewModel @Inject constructor(
    private val anhRepo: AnhMinhChungLamViecRepository,
    private val pcKtvRepo: PhanCongKtvRepository,
    private val phanCongRepo: PhanCongRepository,
) : ViewModel() {

    private val _imageUris = MutableStateFlow<List<Uri>>(emptyList())
    val imageUris = _imageUris.asStateFlow()

    private val _videoUri = MutableStateFlow<Uri?>(null)
    val videoUri = _videoUri.asStateFlow()

    private val _imageNotes = mutableStateMapOf<Uri, String>()
    val imageNotes: Map<Uri, String> get() = _imageNotes

    fun getNoteForImage(uri: Uri): String? {
        return _imageNotes[uri]
    }

    fun updateNoteForImage(uri: Uri, note: String) {
        _imageNotes[uri] = note
    }


    fun addImage(uri: Uri) {
        if (_imageUris.value.size < 5) {
            _imageUris.value = _imageUris.value + uri
        }
    }

    fun removeImage(uri: Uri) {
        _imageUris.value = _imageUris.value - uri
    }

    fun clearVideo() {
        _videoUri.value = null
    }


    fun setVideo(uri: Uri?) {
        _videoUri.value = uri
    }

    fun clearMedia() {
        _imageUris.value = emptyList()
        _videoUri.value = null
    }

    fun checkIn(
        phanCongKtvId: Int
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)

            imageUris.value.forEach { uri ->
                val fileName = "checkin_img_${now}.jpg"
                val url = uploadFileToFirebaseStorage(uri, fileName, "lam_viec")
                val note = _imageNotes[uri]
                url?.let {
                    anhRepo.insert(
                        AnhMinhChungLamViec(
                            phanCongKTVId = phanCongKtvId,
                            loaiAnh = "check-in",
                            urlAnh = it,
                            type = "image",
                            thoiGianTaiLen = now,
                            ghiChu = note
                        )
                    )
                }
            }

            videoUri.value?.let { uri ->
                val fileName = "checkin_video_${now}.mp4"
                val url = uploadFileToFirebaseStorage(uri, fileName)
                url?.let {
                    anhRepo.insert(
                        AnhMinhChungLamViec(
                            phanCongKTVId = phanCongKtvId,
                            loaiAnh = "check-in",
                            urlAnh = it,
                            type = "video",
                            thoiGianTaiLen = now
                        )
                    )
                }
            }

            pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.DANG_THUC_HIEN)
            // cập nhật luôn trạng thái của phân công chung
            phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
            clearMedia()
        }
    }


}
