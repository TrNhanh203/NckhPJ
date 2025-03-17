package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class YeuCauRepository @Inject constructor(
    private val yeuCauDao: YeuCauDao,
    private val chiTietYeuCauDao: ChiTietYeuCauDao
) {
    suspend fun removeChiTietYeuCau(chiTietId: Int) {
        chiTietYeuCauDao.deleteChiTietYeuCau(chiTietId)
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

    suspend fun addThietBiToYeuCau(yeuCauId: Int, thietBiId: Int, loaiYeuCau: String, moTa: String) {
        val chiTiet = ChiTietYeuCau(yeuCauId = yeuCauId, thietBiId = thietBiId, loaiYeuCau = loaiYeuCau, moTa = moTa)
        chiTietYeuCauDao.insertChiTietYeuCau(chiTiet)
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