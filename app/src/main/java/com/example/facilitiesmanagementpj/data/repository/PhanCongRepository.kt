package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 11. PhanCongRepository
@Singleton
class PhanCongRepository @Inject constructor(private val phanCongDao: PhanCongDao) {
    fun getAllPhanCong(): Flow<List<PhanCong>> = phanCongDao.getAll()
    suspend fun insert(phanCong: PhanCong) = phanCongDao.insert(phanCong)
    suspend fun update(phanCong: PhanCong) = phanCongDao.update(phanCong)
    suspend fun delete(phanCong: PhanCong) = phanCongDao.delete(phanCong)
}