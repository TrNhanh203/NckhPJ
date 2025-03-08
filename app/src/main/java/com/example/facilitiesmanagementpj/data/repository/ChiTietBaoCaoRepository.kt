package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 14. ChiTietBaoCaoRepository
@Singleton
class ChiTietBaoCaoRepository @Inject constructor(private val chiTietBaoCaoDao: ChiTietBaoCaoDao) {
    fun getAllChiTietBaoCao(): Flow<List<ChiTietBaoCao>> = chiTietBaoCaoDao.getAll()
    suspend fun insert(chiTietBaoCao: ChiTietBaoCao) = chiTietBaoCaoDao.insert(chiTietBaoCao)
    suspend fun update(chiTietBaoCao: ChiTietBaoCao) = chiTietBaoCaoDao.update(chiTietBaoCao)
    suspend fun delete(chiTietBaoCao: ChiTietBaoCao) = chiTietBaoCaoDao.delete(chiTietBaoCao)
}