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
import com.example.facilitiesmanagementpj.data.utils.formatDuration
import com.example.facilitiesmanagementpj.data.utils.formatTime
import com.example.facilitiesmanagementpj.data.utils.uploadFileToFirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            val listAnh = anhRepo.getByPhanCongKtvId(phanCongKtvId)
                .filter { it.loaiAnh == LoaiAnhMinhChungLamViec.CHECK_IN || it.loaiAnh == LoaiAnhMinhChungLamViec.TAM_NGHI }
                .sortedBy { it.thoiGianTaiLen }

            val pairs = mutableListOf<Pair<Long, Long>>() // các khoảng (checkin, tam_nghi)
            var currentCheckInTime: Long? = null

            for (anh in listAnh) {
                when (anh.loaiAnh) {
                    LoaiAnhMinhChungLamViec.CHECK_IN -> {
                        // Nếu đang có check-in chưa kết thúc thì bỏ qua (tránh lỗi logic lặp)
                        if (currentCheckInTime == null) {
                            currentCheckInTime = anh.thoiGianTaiLen
                        }
                    }

                    LoaiAnhMinhChungLamViec.TAM_NGHI -> {
                        if (currentCheckInTime != null) {
                            pairs.add(currentCheckInTime!! to anh.thoiGianTaiLen)
                            currentCheckInTime = null
                        }
                    }
                }
            }

            // Nếu có check-in cuối chưa bị tạm nghỉ → tính đến thời điểm hiện tại
            currentCheckInTime?.let {
                pairs.add(it to System.currentTimeMillis())
            }

            val tongThoiGianLamViec = pairs.sumOf { (start, end) -> end - start }

            val tongThoiGianDuKienMillis = thoiGianDuKienPhut * 60_000L
            val thoiGianCon = tongThoiGianDuKienMillis - tongThoiGianLamViec

            while (true) {
                _thoiGianConLai.value =
                    thoiGianCon - (System.currentTimeMillis() - pairs.lastOrNull()?.second.orZero())
                delay(60_000L)
            }
        }
    }

    // Extension an toàn
    fun Long?.orZero() = this ?: 0L


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
            val pc = pcKtvRepo.getById(phanCongKtvId) ?: return@launch
            val userId = pc.taiKhoanKTVId
            val isBusy = pcKtvRepo.isDangThucHienCongViecKhac(userId, phanCongKtvId)
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


    private val _isGuiMinhChungLoading = MutableStateFlow(false)
    val isGuiMinhChungLoading: StateFlow<Boolean> = _isGuiMinhChungLoading

    suspend fun guiMinhChungTacVu(
        phanCongKtvId: Int,
        tacVu: LoaiTacVu,
        soPhut: Int? = null
    ): Boolean = withContext(NonCancellable) {
        val now = System.currentTimeMillis()
        val uploadedFiles = mutableListOf<AnhMinhChungLamViec>()

        val isXinGiaHan = tacVu == LoaiTacVu.XIN_GIA_HAN
        if (isXinGiaHan) {
            _dangXinGiaHan.value = true
        }

        _isGuiMinhChungLoading.value = true
        try {

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

            if (uploadedFiles.isEmpty()) {
                if (isXinGiaHan) _dangXinGiaHan.value = false
                showMessage("Không thể gửi minh chứng. Vui lòng chụp lại ít nhất 1 ảnh.")
                return@withContext false
            }

            uploadedFiles.forEach { anhRepo.insert(it) }

            val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)
            when (tacVu) {
                LoaiTacVu.XIN_GIA_HAN -> {
                    if (soPhut != null) pcKtvRepo.xinGiaHan(phanCongKtvId, soPhut)
                }

                LoaiTacVu.CHECK_IN -> {
                    pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.DANG_THUC_HIEN)
                    phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
                }

                LoaiTacVu.TAM_NGHI -> {
                    pcKtvRepo.updateTrangThaiPhanCongKtv(phanCongKtvId, TrangThaiPhanCong.TAM_NGHI)
                    phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
                }

                LoaiTacVu.CHECK_OUT -> {
                    tinhVaLuuThongTinLamViec(phanCongKtvId, TrangThaiPhanCong.HOAN_THANH)
                }
            }

            clearMedia()
            return@withContext true
        } finally {
            _isGuiMinhChungLoading.value = false
        }

    }


    private val _thongTinHoanThanh = MutableStateFlow<ThongTinHoanThanh?>(null)
    val thongTinHoanThanh: StateFlow<ThongTinHoanThanh?> = _thongTinHoanThanh



    suspend fun tinhVaLuuThongTinLamViec(
        phanCongKtvId: Int,
        trangThaiKetThuc: String // VD: TrangThaiPhanCong.HOAN_THANH hoặc TrangThaiPhanCong.BI_HUY
    ) {
        val danhSachAnh = anhRepo.getByPhanCongKtvId(phanCongKtvId)
        val phanCongId = phanCongRepo.getPhanCongIdByPhanCongKtvId(phanCongKtvId)

        // 1. Thời gian bắt đầu: ảnh CHECK_IN đầu tiên
        val thoiGianBatDau = danhSachAnh
            .filter { it.loaiAnh == LoaiAnhMinhChungLamViec.CHECK_IN }
            .minOfOrNull { it.thoiGianTaiLen }

        // 2. Thời gian hoàn thiện: thời điểm hiện tại
        val thoiGianHoanThien = System.currentTimeMillis()

        // 3. Tính các cặp thời gian làm việc thực tế: CHECK_IN → TẠM NGHỈ hoặc → CHECK_OUT/BI_HUY
        val sortedAnh = danhSachAnh
            .filter {
                it.loaiAnh in listOf(
                    LoaiAnhMinhChungLamViec.CHECK_IN,
                    LoaiAnhMinhChungLamViec.TAM_NGHI
                )
            }
            .sortedBy { it.thoiGianTaiLen }

        val pairs = mutableListOf<Pair<Long, Long>>()
        var currentCheckIn: Long? = null

        for (anh in sortedAnh) {
            when (anh.loaiAnh) {
                LoaiAnhMinhChungLamViec.CHECK_IN -> {
                    if (currentCheckIn == null) currentCheckIn = anh.thoiGianTaiLen
                }

                LoaiAnhMinhChungLamViec.TAM_NGHI -> {
                    if (currentCheckIn != null) {
                        pairs.add(currentCheckIn to anh.thoiGianTaiLen)
                        currentCheckIn = null
                    }
                }
            }
        }

        // Nếu còn lần CHECK_IN cuối cùng chưa nghỉ thì tính đến thời điểm hiện tại
        currentCheckIn?.let {
            pairs.add(it to thoiGianHoanThien)
        }

        val tongThoiGianLamViecMillis = pairs.sumOf { it.second - it.first }
        val tongThoiGianLamViecPhut = (tongThoiGianLamViecMillis / 60_000L).toInt()

        // 4. Thời gian phát sinh = tổng thời gian - thời gian làm việc
        val thoiGianPhatSinhPhut = (
                (thoiGianHoanThien - (thoiGianBatDau ?: thoiGianHoanThien)) - tongThoiGianLamViecMillis
                ).coerceAtLeast(0L) / 60_000L

        // 5. Ghi vào DB
        pcKtvRepo.capNhatThongTinCheckOut(
            phanCongKtvId = phanCongKtvId,
            thoiGianBatDau = thoiGianBatDau,
            thoiGianHoanThien = thoiGianHoanThien,
            thoiGianLamViec = tongThoiGianLamViecPhut,
            thoiGianPhatSinh = thoiGianPhatSinhPhut.toInt()
        )

        pcKtvRepo.updateTrangThaiPhanCongKtv(
            phanCongKtvId,
            trangThaiKetThuc
        )

        phanCongRepo.capNhatTrangThaiPhanCong(phanCongId)
    }

    fun loadThongTinHoanThanh(phanCongKtvId: Int) {
        viewModelScope.launch {
            val pc = pcKtvRepo.getById(phanCongKtvId)
            pc?.let {
                _thongTinHoanThanh.value = ThongTinHoanThanh(
                    thoiGianBatDau = it.thoiGianBatDau,
                    thoiGianHoanThien = it.thoiGianHoanThien ?: System.currentTimeMillis(),
                    thoiGianLamViec = it.thoiGianLamViecThucTe ?: 0,
                    thoiGianPhatSinh = it.thoiGianPhatSinh ?: 0
                )
            }
        }
    }


    data class ThongTinHoanThanh(
        val thoiGianBatDau: Long?,
        val thoiGianHoanThien: Long,
        val thoiGianLamViec: Int,
        val thoiGianPhatSinh: Int
    )





}