package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 2. LoaiPhongRepository
@Singleton
class LoaiPhongRepository @Inject constructor(private val loaiPhongDao: LoaiPhongDao) {
    fun getAllLoaiPhong(): Flow<List<LoaiPhong>> = loaiPhongDao.getAll()
    suspend fun insert(loaiPhong: LoaiPhong) = loaiPhongDao.insert(loaiPhong)
    suspend fun update(loaiPhong: LoaiPhong) = loaiPhongDao.update(loaiPhong)
    suspend fun delete(loaiPhong: LoaiPhong) = loaiPhongDao.delete(loaiPhong)
}
