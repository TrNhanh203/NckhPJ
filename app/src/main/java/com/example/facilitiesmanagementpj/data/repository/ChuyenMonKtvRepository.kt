package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 18. ChuyenMonKtvRepository
@Singleton
class ChuyenMonKtvRepository @Inject constructor(private val chuyenMonKtvDao: ChuyenMonKtvDao) {
    fun getAllChuyenMonKtv(): Flow<List<ChuyenMonKtv>> = chuyenMonKtvDao.getAll()
    suspend fun insert(chuyenMonKtv: ChuyenMonKtv) = chuyenMonKtvDao.insert(chuyenMonKtv)
    suspend fun update(chuyenMonKtv: ChuyenMonKtv) = chuyenMonKtvDao.update(chuyenMonKtv)
    suspend fun delete(chuyenMonKtv: ChuyenMonKtv) = chuyenMonKtvDao.delete(chuyenMonKtv)
}