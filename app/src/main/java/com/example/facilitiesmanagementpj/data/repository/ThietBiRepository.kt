package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 8. ThietBiRepository
@Singleton
class ThietBiRepository @Inject constructor(private val thietBiDao: ThietBiDao) {
    fun getAllThietBi(): Flow<List<ThietBi>> = thietBiDao.getAll()
    fun getThietBiCanBaoDuong(ngayHienTai: Long): Flow<List<ThietBi>> =
        thietBiDao.getThietBiCanBaoDuong(ngayHienTai)
    suspend fun insert(thietBi: ThietBi) = thietBiDao.insert(thietBi)
    suspend fun update(thietBi: ThietBi) = thietBiDao.update(thietBi)
    suspend fun delete(thietBi: ThietBi) = thietBiDao.delete(thietBi)
}