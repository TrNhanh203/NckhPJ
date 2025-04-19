package com.example.facilitiesmanagementpj.data.repository
import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.sync.BaseFirestoreSyncService
import com.example.facilitiesmanagementpj.data.utils.TrangThaiChungCuaPhanCong
import com.example.facilitiesmanagementpj.data.utils.TrangThaiYeuCau
import com.example.facilitiesmanagementpj.data.utils.deleteFileFromFirebaseStorage
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import com.example.facilitiesmanagementpj.data.sync.SyncDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext


@Singleton
class YeuCauRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val yeuCauDao: YeuCauDao,
    private val chiTietYeuCauDao: ChiTietYeuCauDao,
    private val anhMinhChungBaoCaoDao: AnhMinhChungBaoCaoDao,
    private val phanCongDao: PhanCongDao,

    private val yeuCauSyncService: BaseFirestoreSyncService<YeuCau>,
    private val anhMinhBaoCaoSyncService: BaseFirestoreSyncService<AnhMinhChungBaoCao>,
    private val phanCongSyncService: BaseFirestoreSyncService<PhanCong>,
    private val chiTietYeuCauSyncService: BaseFirestoreSyncService<ChiTietYeuCau>,
) {
    fun getAllYeuCauTruNhap(): Flow<List<YeuCau>> = yeuCauDao.getAllYeuCauTruNhap(TrangThaiYeuCau.NHAP)

    fun getAllYeuCau(): Flow<List<YeuCau>> = yeuCauDao.getAll()

    suspend fun getAllYeuCauTruNhapOnce(): List<YeuCau> = yeuCauDao.getAllTruNhapOnce()

    suspend fun getById(id: Int): YeuCau? {
        return yeuCauDao.getById(id)
    }

    suspend fun getChiTietYeuCauByYeuCauId(yeuCauId: Int): List<ChiTietYeuCau> {
        return chiTietYeuCauDao.getByYeuCauId(yeuCauId)
    }

    fun getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId: Int): Flow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>> {
        return chiTietYeuCauDao.getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId)
    }

    suspend fun getChiTietYeuCauByYeuCauAndThietBi(yeuCauId: Int, thietBiId: Int): ChiTietYeuCau? {
        return chiTietYeuCauDao.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
    }

    suspend fun getYeuCauById(yeuCauId: Int): YeuCau? {
        return yeuCauDao.getYeuCauById(yeuCauId)
    }

    fun getYeuCauByDonVi(donViId: Int): Flow<List<YeuCau>> {
        return yeuCauDao.getYeuCauByDonVi(donViId)
    }

    fun getChiTietYeuCau(yeuCauId: Int): Flow<List<ChiTietYeuCau>> {
        return chiTietYeuCauDao.getChiTietYeuCauByYeuCau(yeuCauId)
    }

    suspend fun getChiTietYeuCauById(id: Int): ChiTietYeuCau? {
        return chiTietYeuCauDao.getChiTietYeuCauById(id)
    }


// UPADATE LOGIC
    @OptIn(UnstableApi::class)
    suspend fun insertAnhMinhChung(chiTietBaoCaoId: Int, url: String, type: String) {
        try {
            withContext(NonCancellable) {
                Log.d("insertAnhMinhChung", "Attempting to insert: chiTietBaoCaoId=$chiTietBaoCaoId, url=$url, type=$type")
                val existingRecords = anhMinhChungBaoCaoDao.getAll().firstOrNull()
                val existingRecord = existingRecords?.firstOrNull { it.urlAnh == url }
                if (existingRecord == null) {
                    val anhMinhChung = AnhMinhChungBaoCao(chiTietBaoCaoId = chiTietBaoCaoId, urlAnh = url, type = type)
                    anhMinhChungBaoCaoDao.insert(anhMinhChung)

                    SyncDispatcher.dispatch( anhMinhBaoCaoSyncService, anhMinhChung, SyncDispatcher.SyncType.INSERT)
                    Log.d("insertAnhMinhChung", "Insertion successful: $anhMinhChung")
                } else {
                    Log.d("insertAnhMinhChung", "Record already exists: $existingRecord")
                }
            }
        } catch (e: Exception) {
            Log.e("insertAnhMinhChung", "Insertion failed", e)
        }
    }

    suspend fun deleteYeuCauWithDetails(yeuCauId: Int) {
        val chiTietYeuCauList = chiTietYeuCauDao.getChiTietYeuCauByYeuCau(yeuCauId).firstOrNull()
        chiTietYeuCauList?.forEach { chiTietYeuCau ->
            val images = anhMinhChungBaoCaoDao.getImagesByChiTietBaoCaoId(chiTietYeuCau.id)
            val videos = anhMinhChungBaoCaoDao.getVideosByChiTietBaoCaoId(chiTietYeuCau.id)

            // Delete images from Firebase and Firestore
            images.forEach {
                deleteFileFromFirebaseStorage(it.urlAnh)
                SyncDispatcher.dispatch(anhMinhBaoCaoSyncService, it, SyncDispatcher.SyncType.DELETE)
            }
            // Delete videos from Firebase and Firestore
            videos.forEach {
                deleteFileFromFirebaseStorage(it.urlAnh)
                SyncDispatcher.dispatch(anhMinhBaoCaoSyncService, it, SyncDispatcher.SyncType.DELETE)
            }

            // Delete AnhMinhChungBaoCao from database
            anhMinhChungBaoCaoDao.deleteByChiTietBaoCaoId(chiTietYeuCau.id)

            // Delete ChiTietYeuCau from Firestore
            SyncDispatcher.dispatch(chiTietYeuCauSyncService, chiTietYeuCau, SyncDispatcher.SyncType.DELETE)
        }
        // Delete ChiTietYeuCau from database
        chiTietYeuCauDao.deleteByYeuCauId(yeuCauId)
        // Delete YeuCau from database
        yeuCauDao.deleteYeuCau(yeuCauId)

        // Đồng bộ xoá trên Firestore
        val deletedYeuCau = YeuCau(id = yeuCauId)
        SyncDispatcher.dispatch( yeuCauSyncService, deletedYeuCau, SyncDispatcher.SyncType.DELETE)
    }

    suspend fun deleteAnhMinhChungByPath(path: String) {
        val item = anhMinhChungBaoCaoDao.getByPath(path)
        if (item != null) {
            anhMinhChungBaoCaoDao.deleteByPath(path)
            SyncDispatcher.dispatch(anhMinhBaoCaoSyncService, item, SyncDispatcher.SyncType.DELETE)

            deleteFileFromFirebaseStorage(path)
        }
    }

    suspend fun createYeuCau(taiKhoanId: Int, donViId: Int, moTa: String): Long {
        val yeuCau = YeuCau(taiKhoanId = taiKhoanId, donViId = donViId, moTa = moTa)

        val newId = yeuCauDao.insertYeuCau(yeuCau) // Room trả về ID mới (Long)

        val yeuCauWithId = yeuCau.copy(id = newId.toInt()) // 🆕 Gắn id vào

        SyncDispatcher.dispatch(yeuCauSyncService, yeuCauWithId, SyncDispatcher.SyncType.INSERT)

        return newId

    }
    suspend fun deleteYeuCau(yeuCauId: Int) {
        // 1. Lấy toàn bộ chi tiết liên quan trước khi xóa trong Room
        val chiTietList = chiTietYeuCauDao.getByYeuCauId(yeuCauId)

        // 2. Xoá từng chi tiết yêu cầu trên Firestore
        chiTietList.forEach { chiTiet ->
            chiTietYeuCauSyncService.deleteFromCloud(chiTiet.id.toString())
        }

        // 3. Xoá chi tiết trong Room
        chiTietYeuCauDao.deleteByYeuCauId(yeuCauId)

        // 4. Lấy yeuCau cần xóa
        val yeuCau = yeuCauDao.getById(yeuCauId)

        // 5. Xoá yeuCau document Firestore
        yeuCau?.let {
            yeuCauSyncService.deleteFromCloud(it.id.toString())
        }

        // 6. Xoá yeuCau trong Room
        yeuCauDao.deleteYeuCau(yeuCauId)
    }
    suspend fun removeChiTietYeuCau(chiTietId: Int) {
        val chiTietYeuCau = chiTietYeuCauDao.getChiTietYeuCauByChiTietId(chiTietId)

        chiTietYeuCau?.let {
            // 1. Fetch tất cả ảnh liên quan
            val anhList = anhMinhChungBaoCaoDao.getImagesByChiTietBaoCaoId(it.id)

            // 2. Xoá từng ảnh trên Firestore
            anhList.forEach { anh ->
                SyncDispatcher.dispatch(anhMinhBaoCaoSyncService, anh, SyncDispatcher.SyncType.DELETE)
                deleteFileFromFirebaseStorage(anh.urlAnh)
            }

            // 3. Xoá ảnh trong Room
            anhMinhChungBaoCaoDao.deleteByChiTietBaoCaoId(it.id)

            // 4. Xoá chi tiết yêu cầu trên Firestore
            SyncDispatcher.dispatch(chiTietYeuCauSyncService, it, SyncDispatcher.SyncType.DELETE)

            // 5. Xoá chi tiết yêu cầu trong Room
            chiTietYeuCauDao.deleteChiTietYeuCau(it.id)
        }
    }

    suspend fun updateChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String) {
        chiTietYeuCauDao.updateChiTietYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
        val updatedChiTiet = chiTietYeuCauDao.getById(yeuCauId)
        updatedChiTiet?.let {
            SyncDispatcher.dispatch(chiTietYeuCauSyncService, it, SyncDispatcher.SyncType.UPDATE)
        }
    }
    suspend fun addThietBiToYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String): Int {
        val chiTiet = ChiTietYeuCau(yeuCauId = yeuCauId, thietBiId = thietBiId, loaiYeuCau = loaiYeuCau, moTa = moTa)

        val newId = chiTietYeuCauDao.insertChiTietYeuCau(chiTiet) // Room insert

        val chiTietWithId = chiTiet.copy(id = newId.toInt()) // 🆕 Gán id mới

        SyncDispatcher.dispatch(chiTietYeuCauSyncService, chiTietWithId, SyncDispatcher.SyncType.INSERT)

        return newId.toInt()
    }

    suspend fun updateYeuCauStatus(yeuCauId: Int, status: String) {
        yeuCauDao.updateYeuCauStatus(yeuCauId, status)
        if(status == TrangThaiYeuCau.CHO_XAC_NHAN){
            yeuCauDao.updateYeuCauThoiGianGui(yeuCauId, System.currentTimeMillis())
            val updated = yeuCauDao.getById(yeuCauId)
            updated?.let { SyncDispatcher.dispatch(yeuCauSyncService, updated, SyncDispatcher.SyncType.UPDATE) }
        }
    }

    suspend fun updateYeuCauKhiTuChoi(yeuCauId: Int, status: String, lyDoTuChoi: String) {
        yeuCauDao.updateYeuCauKhiTuChoi(yeuCauId, status, lyDoTuChoi)
        val updated = yeuCauDao.getById(yeuCauId)
        updated?.let { SyncDispatcher.dispatch(yeuCauSyncService, updated, SyncDispatcher.SyncType.UPDATE) }
    }

    @OptIn(UnstableApi::class)
    suspend fun capNhatTrangThaiYeuCau(yeuCauId: Int) {
        val dsChiTiet = chiTietYeuCauDao.getAllByYeuCauId(yeuCauId)

        if (dsChiTiet.isEmpty()) return

        val chiTietIds = dsChiTiet.map { it.id }
        val dsPhanCong = phanCongDao.getByChiTietIds(chiTietIds)

        val daCoPhanCong = chiTietIds.any { chiTietId ->
            dsPhanCong.any { it.chiTietYeuCauId == chiTietId }
        }

        val daHoanThanhHet = chiTietIds.all { chiTietId ->
            val pcs = dsPhanCong.filter { it.chiTietYeuCauId == chiTietId }
            pcs.isNotEmpty() && pcs.all { it.trangThai == TrangThaiChungCuaPhanCong.HOAN_THANH }
        }

        val newStatus = when {
            daHoanThanhHet -> TrangThaiYeuCau.DA_XU_LY
            daCoPhanCong -> TrangThaiYeuCau.DANG_XU_LY
            else -> return
        }

        val yeuCau = yeuCauDao.getById(yeuCauId) ?: return

        if (yeuCau.trangThai != newStatus) {
            yeuCau.trangThai = newStatus
            yeuCauDao.insert(yeuCau)
            SyncDispatcher.dispatch(yeuCauSyncService, yeuCau, SyncDispatcher.SyncType.UPDATE)
        } else {
            Log.d("YeuCauRepository", "⏩ Không thay đổi trạng thái yêu cầu id=${yeuCau.id}, bỏ qua push Firestore")
        }
    }


    suspend fun insert(yeuCau: YeuCau) = yeuCauDao.insert(yeuCau)
    suspend fun update(yeuCau: YeuCau) = yeuCauDao.update(yeuCau)
    suspend fun delete(yeuCau: YeuCau) = yeuCauDao.delete(yeuCau)
}

