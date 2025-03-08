package com.example.facilitiesmanagementpj.data.repository
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 9. ChuyenMonRepository
@Singleton
class ChuyenMonRepository @Inject constructor(private val chuyenMonDao: ChuyenMonDao) {
    fun getAllChuyenMon(): Flow<List<ChuyenMon>> = chuyenMonDao.getAll()
    suspend fun insert(chuyenMon: ChuyenMon) = chuyenMonDao.insert(chuyenMon)
    suspend fun update(chuyenMon: ChuyenMon) = chuyenMonDao.update(chuyenMon)
    suspend fun delete(chuyenMon: ChuyenMon) = chuyenMonDao.delete(chuyenMon)
}