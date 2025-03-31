package com.example.facilitiesmanagementpj.ui.viewmodel.ktvViewModel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _thoiGianConLai = MutableStateFlow<Long?>(null)
    val thoiGianConLai = _thoiGianConLai.asStateFlow()



    private var countdownJob: Job? = null

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

    fun startCountdown(phanCongKtvId: Int, thoiGianDuKienPhut: Int) {
        countdownJob?.cancel() // Hủy job cũ nếu có
        countdownJob = viewModelScope.launch {
            val listCheckin = anhRepo.getByPhanCongKtvId(phanCongKtvId)
                .filter { it.loaiAnh == LoaiAnhMinhChungLamViec.CHECK_IN }
                .sortedBy { it.thoiGianTaiLen }
            val thoiGianBatDau = listCheckin.firstOrNull()?.thoiGianTaiLen ?: return@launch
            val tongMillis = thoiGianDuKienPhut * 60_000L
            val deadline = thoiGianBatDau + tongMillis
            while (true) {
                val thoiGianCon = deadline - System.currentTimeMillis()
                _thoiGianConLai.value = thoiGianCon
                delay(60_000L)
            }
        }
    }

    fun stopCountdown() {
        countdownJob?.cancel()
        countdownJob = null
    }

    private val _thoiGianDuKien = MutableStateFlow<Int?>(null)
    val thoiGianDuKien = _thoiGianDuKien.asStateFlow()

    private val _dangXinGiaHan = MutableStateFlow(false)
    val dangXinGiaHan = _dangXinGiaHan.asStateFlow()

    fun loadThoiGianDuKien(phanCongKtvId: Int) {
        viewModelScope.launch {
            val pcKtv = pcKtvRepo.getById(phanCongKtvId)
            _thoiGianDuKien.value = pcKtv?.thoiGianDuKien
            _dangXinGiaHan.value = pcKtv?.dangXinGiaHan == true
        }
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
                            loaiAnh = LoaiAnhMinhChungLamViec.CHECK_IN,
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
                            loaiAnh = LoaiAnhMinhChungLamViec.CHECK_IN,
                            urlAnh = it,
                            type = "video",
                            thoiGianTaiLen = now
                        )
                    )
                }
            }

            pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.DANG_THUC_HIEN)
            phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
            clearMedia()
        }
    }

    fun xinGiaHan(phanCongKtvId: Int, soPhut: Int) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()

            // Upload ảnh như check-in, nhưng loại là XIN_GIA_HAN
            imageUris.value.forEach { uri ->
                val fileName = "xin_gia_han_img_${now}_${uri.hashCode()}.jpg"
                val url = uploadFileToFirebaseStorage(uri, fileName, "lam_viec")
                val note = _imageNotes[uri]
                url?.let {
                    anhRepo.insert(
                        AnhMinhChungLamViec(
                            phanCongKTVId = phanCongKtvId,
                            loaiAnh = LoaiAnhMinhChungLamViec.XIN_GIA_HAN,
                            urlAnh = it,
                            type = "image",
                            thoiGianTaiLen = now,
                            ghiChu = note
                        )
                    )
                }
            }

            // Cập nhật trạng thái trong bảng phan_cong_ktv
            pcKtvRepo.xinGiaHan(phanCongKtvId, soPhut)
            clearMedia()
        }
    }


    fun kiemTraTruocCheckIn(phanCongKtvId: Int, onKhongDuoc: () -> Unit, onDuoc: () -> Unit) {
        viewModelScope.launch {
            Log.d("kiemTraTruocCheckIn", "Ham kiem tra dang chay")
            val pc = pcKtvRepo.getById(phanCongKtvId) ?: return@launch
            val userId = pc.taiKhoanKTVId
            val isBusy = pcKtvRepo.isDangThucHienCongViecKhac(userId, phanCongKtvId)
            Log.d("kiemTraTruocCheckIn", "isBusy: $isBusy")
            if (isBusy) {
                onKhongDuoc()
            } else {
                onDuoc()
            }
        }
    }


    private val _uiMessage = MutableSharedFlow<String>()
    val uiMessage: SharedFlow<String> = _uiMessage

    fun showMessage(message: String) {
        _uiMessage.tryEmit(message)
    }


    fun guiMinhChungTacVu(
        phanCongKtvId: Int,
        tacVu: LoaiTacVu,
        soPhut: Int? = null
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val uploadedFiles = mutableListOf<AnhMinhChungLamViec>()

            val isXinGiaHan = tacVu == LoaiTacVu.XIN_GIA_HAN
            if (isXinGiaHan) {
                _dangXinGiaHan.value = true
            }

            imageUris.value.forEach { uri ->
                val fileName = "${tacVu.name.lowercase()}_img_${now}_${uri.hashCode()}.jpg"
                val url = uploadFileToFirebaseStorage(uri, fileName, "lam_viec")
                val note = _imageNotes[uri]
                url?.let {
                    uploadedFiles.add(
                        AnhMinhChungLamViec(
                            phanCongKTVId = phanCongKtvId,
                            loaiAnh = tacVu.loaiAnh,
                            urlAnh = it,
                            type = "image",
                            thoiGianTaiLen = now,
                            ghiChu = note
                        )
                    )
                }
            }

            videoUri.value?.let { uri ->
                val fileName = "${tacVu.name.lowercase()}_video_${now}.mp4"
                val url = uploadFileToFirebaseStorage(uri, fileName)
                url?.let {
                    uploadedFiles.add(
                        AnhMinhChungLamViec(
                            phanCongKTVId = phanCongKtvId,
                            loaiAnh = tacVu.loaiAnh,
                            urlAnh = it,
                            type = "video",
                            thoiGianTaiLen = now
                        )
                    )
                }
            }

            // Nếu không có ảnh/video nào được upload thành công thì cảnh báo và không cập nhật
            if (uploadedFiles.isEmpty()) {
                if (isXinGiaHan) _dangXinGiaHan.value = false
                showMessage("Không thể gửi minh chứng. Vui lòng chụp lại ít nhất 1 ảnh.")
                return@launch
            }

            // Insert tất cả file thành công vào DB
            uploadedFiles.forEach { anhRepo.insert(it) }

            when (tacVu) {
                LoaiTacVu.XIN_GIA_HAN -> {
                    if (soPhut != null) pcKtvRepo.xinGiaHan(phanCongKtvId, soPhut)
                }

                LoaiTacVu.CHECK_IN -> {
                    pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.DANG_THUC_HIEN)
                    val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
                    phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
                }

                LoaiTacVu.TAM_NGHI -> {
                    pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.TAM_NGHI)
                    val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
                    phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
                }

                LoaiTacVu.CHECK_OUT -> {
                    pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.HOAN_THANH)
                    val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
                    phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
                }
            }

            clearMedia()
        }
    }



//    fun guiMinhChungTacVu(
//        phanCongKtvId: Int,
//        tacVu: LoaiTacVu,
//        soPhut: Int? = null
//    ) {
//        viewModelScope.launch {
//            val now = System.currentTimeMillis()
//
//            imageUris.value.forEach { uri ->
//                val fileName = "${tacVu.name.lowercase()}_img_${now}_${uri.hashCode()}.jpg"
//                val url = uploadFileToFirebaseStorage(uri, fileName, "lam_viec")
//                val note = _imageNotes[uri]
//                url?.let {
//                    anhRepo.insert(
//                        AnhMinhChungLamViec(
//                            phanCongKTVId = phanCongKtvId,
//                            loaiAnh = tacVu.loaiAnh,
//                            urlAnh = it,
//                            type = "image",
//                            thoiGianTaiLen = now,
//                            ghiChu = note
//                        )
//                    )
//                }
//            }
//
//            videoUri.value?.let { uri ->
//                val fileName = "${tacVu.name.lowercase()}_video_${now}.mp4"
//                val url = uploadFileToFirebaseStorage(uri, fileName)
//                url?.let {
//                    anhRepo.insert(
//                        AnhMinhChungLamViec(
//                            phanCongKTVId = phanCongKtvId,
//                            loaiAnh = tacVu.loaiAnh,
//                            urlAnh = it,
//                            type = "video",
//                            thoiGianTaiLen = now
//                        )
//                    )
//                }
//            }
//
//            when (tacVu) {
//                LoaiTacVu.XIN_GIA_HAN -> {
//                    _dangXinGiaHan.value = true
//                    if (soPhut != null) pcKtvRepo.xinGiaHan(phanCongKtvId, soPhut)
//                }
//
//                LoaiTacVu.CHECK_IN -> {
//                    pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.DANG_THUC_HIEN)
//                    val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
//                    phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
//                }
//
//                LoaiTacVu.TAM_NGHI -> {
//                    pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.TAM_NGHI)
//                    val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
//                    phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
//                }
//
//                LoaiTacVu.CHECK_OUT -> {
//                    pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.HOAN_THANH)
//                    val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
//                    phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
//                }
//
//            }
//
//            clearMedia()
//        }
//    }



}
