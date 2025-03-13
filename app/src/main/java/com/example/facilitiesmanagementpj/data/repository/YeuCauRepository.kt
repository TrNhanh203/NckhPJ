package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


// 13. BaoCaoSuCoRepository
@Singleton
class YeuCauRepository @Inject constructor(private val yeuCauDao: YeuCauDao) {
    fun getAllYeuCau(): Flow<List<YeuCau>> = yeuCauDao.getAll()
    suspend fun insert(yeuCau: YeuCau) = yeuCauDao.insert(yeuCau)
    suspend fun update(yeuCau: YeuCau) = yeuCauDao.update(yeuCau)
    suspend fun delete(yeuCau: YeuCau) = yeuCauDao.delete(yeuCau)
}