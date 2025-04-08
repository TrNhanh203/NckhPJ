package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 14. ChiTietBaoCaoRepository
@Singleton
class ChiTietYeuCauRepository @Inject constructor(private val chiTietYeuCauDao: ChiTietYeuCauDao) {

    suspend fun getAllByYeuCauId(yeuCauId: Int): List<ChiTietYeuCau> = chiTietYeuCauDao.getAllByYeuCauId(yeuCauId)

    suspend fun getById(chiTietId: Int): ChiTietYeuCau? = chiTietYeuCauDao.getChiTietYeuCauByChiTietId(chiTietId)
    fun getAllChiTietYeuCau(): Flow<List<ChiTietYeuCau>> = chiTietYeuCauDao.getAll()
    suspend fun insert(chiTietYeuCau: ChiTietYeuCau) = chiTietYeuCauDao.insert(chiTietYeuCau)
    suspend fun update(chiTietYeuCau: ChiTietYeuCau) = chiTietYeuCauDao.update(chiTietYeuCau)
    suspend fun delete(chiTietYeuCau: ChiTietYeuCau) = chiTietYeuCauDao.delete(chiTietYeuCau)
}