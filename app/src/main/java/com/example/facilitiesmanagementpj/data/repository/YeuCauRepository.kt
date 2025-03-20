package com.example.facilitiesmanagementpj.data.repository
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import com.example.facilitiesmanagementpj.data.utils.deleteFileFromFirebaseStorage
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class YeuCauRepository @Inject constructor(
    private val yeuCauDao: YeuCauDao,
    private val chiTietYeuCauDao: ChiTietYeuCauDao,
    private val anhMinhChungBaoCaoDao: AnhMinhChungBaoCaoDao
) {


    fun getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId: Int): Flow<List<ChiTietYeuCauWithThietBiAndLoaiThietBi>> {
        return chiTietYeuCauDao.getChiTietYeuCauWithThietBiAndLoaiThietBi(yeuCauId)
    }


    suspend fun deleteYeuCauWithDetails(yeuCauId: Int) {
        val chiTietYeuCauList = chiTietYeuCauDao.getChiTietYeuCauByYeuCau(yeuCauId).firstOrNull()
        chiTietYeuCauList?.forEach { chiTietYeuCau ->
            val images = anhMinhChungBaoCaoDao.getImagesByChiTietBaoCaoId(chiTietYeuCau.id)
            val videos = anhMinhChungBaoCaoDao.getVideosByChiTietBaoCaoId(chiTietYeuCau.id)

            // Delete images from Firebase
            images.forEach { deleteFileFromFirebaseStorage(it.urlAnh) }
            // Delete videos from Firebase
            videos.forEach { deleteFileFromFirebaseStorage(it.urlAnh) }

            // Delete AnhMinhChungBaoCao from database
            anhMinhChungBaoCaoDao.deleteByChiTietBaoCaoId(chiTietYeuCau.id)
        }
        // Delete ChiTietYeuCau from database
        chiTietYeuCauDao.deleteByYeuCauId(yeuCauId)
        // Delete YeuCau from database
        yeuCauDao.deleteYeuCau(yeuCauId)
    }

    suspend fun deleteAnhMinhChungByPath(path: String) {
        anhMinhChungBaoCaoDao.deleteByPath(path)
    }

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
                    Log.d("insertAnhMinhChung", "Insertion successful: $anhMinhChung")
                } else {
                    Log.d("insertAnhMinhChung", "Record already exists: $existingRecord")
                }
            }
        } catch (e: Exception) {
            Log.e("insertAnhMinhChung", "Insertion failed", e)
        }
    }


    suspend fun deleteYeuCau(yeuCauId: Int) {
        chiTietYeuCauDao.deleteByYeuCauId(yeuCauId)
        yeuCauDao.deleteYeuCau(yeuCauId)
    }

    suspend fun updateChiTietYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String) {
        chiTietYeuCauDao.updateChiTietYeuCau(yeuCauId, thietBiId, loaiYeuCau, moTa)
    }

    suspend fun getChiTietYeuCauByYeuCauAndThietBi(yeuCauId: Int, thietBiId: Int): ChiTietYeuCau? {
        return chiTietYeuCauDao.getChiTietYeuCauByYeuCauAndThietBi(yeuCauId, thietBiId)
    }

    suspend fun removeChiTietYeuCau(chiTietId: Int) {
        val chiTietYeuCau = chiTietYeuCauDao.getChiTietYeuCauByChiTietId(chiTietId)
        chiTietYeuCau?.let {
            chiTietYeuCauDao.deleteAnhMinhChungByChiTietBaoCaoId(it.id)
            chiTietYeuCauDao.deleteChiTietYeuCau(it.id)
        }
    }


    suspend fun getYeuCauById(yeuCauId: Int): YeuCau? {
        return yeuCauDao.getYeuCauById(yeuCauId)
    }

    fun getYeuCauByDonVi(donViId: Int): Flow<List<YeuCau>> {
        return yeuCauDao.getYeuCauByDonVi(donViId)
    }

    suspend fun createYeuCau(taiKhoanId: Int, donViId: Int, moTa: String): Long {
        val yeuCau = YeuCau(taiKhoanId = taiKhoanId, donViId = donViId, moTa = moTa)
        return yeuCauDao.insertYeuCau(yeuCau)
    }

    fun getChiTietYeuCau(yeuCauId: Int): Flow<List<ChiTietYeuCau>> {
        return chiTietYeuCauDao.getChiTietYeuCauByYeuCau(yeuCauId)
    }

//    suspend fun addThietBiToYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String) {
//        val chiTiet = ChiTietYeuCau(yeuCauId = yeuCauId, thietBiId = thietBiId, loaiYeuCau = loaiYeuCau, moTa = moTa)
//        chiTietYeuCauDao.insertChiTietYeuCau(chiTiet)
//    }

    suspend fun addThietBiToYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String): Int {
        val chiTiet = ChiTietYeuCau(yeuCauId = yeuCauId, thietBiId = thietBiId, loaiYeuCau = loaiYeuCau, moTa = moTa)
        return chiTietYeuCauDao.insertChiTietYeuCau(chiTiet).toInt()
    }

    fun getAllYeuCau(): Flow<List<YeuCau>> = yeuCauDao.getAll()
    suspend fun insert(yeuCau: YeuCau) = yeuCauDao.insert(yeuCau)
    suspend fun update(yeuCau: YeuCau) = yeuCauDao.update(yeuCau)
    suspend fun delete(yeuCau: YeuCau) = yeuCauDao.delete(yeuCau)
}


// 13. BaoCaoSuCoRepository
//@Singleton
//class YeuCauRepository @Inject constructor(private val yeuCauDao: YeuCauDao) {
//    fun getAllYeuCau(): Flow<List<YeuCau>> = yeuCauDao.getAll()
//    suspend fun insert(yeuCau: YeuCau) = yeuCauDao.insert(yeuCau)
//    suspend fun update(yeuCau: YeuCau) = yeuCauDao.update(yeuCau)
//    suspend fun delete(yeuCau: YeuCau) = yeuCauDao.delete(yeuCau)
//}