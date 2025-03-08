package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 17. PhanCongThietBiRepository
@Singleton
class PhanCongThietBiRepository @Inject constructor(private val phanCongThietBiDao: PhanCongThietBiDao) {
    fun getAllPhanCongThietBi(): Flow<List<PhanCongThietBi>> = phanCongThietBiDao.getAll()
    suspend fun insert(phanCongThietBi: PhanCongThietBi) = phanCongThietBiDao.insert(phanCongThietBi)
    suspend fun update(phanCongThietBi: PhanCongThietBi) = phanCongThietBiDao.update(phanCongThietBi)
    suspend fun delete(phanCongThietBi: PhanCongThietBi) = phanCongThietBiDao.delete(phanCongThietBi)
}