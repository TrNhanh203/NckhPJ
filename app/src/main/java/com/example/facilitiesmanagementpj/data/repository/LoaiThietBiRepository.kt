package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 1. LoaiThietBiRepository
@Singleton
class LoaiThietBiRepository @Inject constructor(private val loaiThietBiDao: LoaiThietBiDao) {
    fun getLoaiThietBiByThietBiId(thietBiId: Int): Flow<LoaiThietBi> = loaiThietBiDao.getLoaiThietBiByThietBiId(thietBiId)

    fun getAllLoaiThietBi(): Flow<List<LoaiThietBi>> = loaiThietBiDao.getAll()
    suspend fun insert(loaiThietBi: LoaiThietBi) = loaiThietBiDao.insert(loaiThietBi)
    suspend fun update(loaiThietBi: LoaiThietBi) = loaiThietBiDao.update(loaiThietBi)
    suspend fun delete(loaiThietBi: LoaiThietBi) = loaiThietBiDao.delete(loaiThietBi)
}