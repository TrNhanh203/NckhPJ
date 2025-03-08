package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


// 13. BaoCaoSuCoRepository
@Singleton
class BaoCaoSuCoRepository @Inject constructor(private val baoCaoSuCoDao: BaoCaoSuCoDao) {
    fun getAllBaoCaoSuCo(): Flow<List<BaoCaoSuCo>> = baoCaoSuCoDao.getAll()
    suspend fun insert(baoCaoSuCo: BaoCaoSuCo) = baoCaoSuCoDao.insert(baoCaoSuCo)
    suspend fun update(baoCaoSuCo: BaoCaoSuCo) = baoCaoSuCoDao.update(baoCaoSuCo)
    suspend fun delete(baoCaoSuCo: BaoCaoSuCo) = baoCaoSuCoDao.delete(baoCaoSuCo)
}