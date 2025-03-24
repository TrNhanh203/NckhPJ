package com.example.facilitiesmanagementpj.data.repository

import androidx.room.Insert
import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 11. PhanCongRepository
@Singleton
class PhanCongRepository @Inject constructor(private val phanCongDao: PhanCongDao) {

    suspend fun hasPhanCongForChiTiet(chiTietId: Int): Boolean {
        return phanCongDao.hasPhanCongForChiTiet(chiTietId)
    }


    fun getAllPhanCong(): Flow<List<PhanCong>> = phanCongDao.getAll()
    suspend fun insert(phanCong: PhanCong) = phanCongDao.insert(phanCong)
    suspend fun update(phanCong: PhanCong) = phanCongDao.update(phanCong)
    suspend fun delete(phanCong: PhanCong) = phanCongDao.delete(phanCong)

    suspend fun insertAndGetId(phanCong: PhanCong): Long {
        return phanCongDao.insertAndGetId(phanCong)
    }

    suspend fun getPhanCongByChiTietYeuCau(chiTietId: Int): PhanCong? {
        return phanCongDao.getByChiTietYeuCauId(chiTietId)
    }

    suspend fun getPhanCongById(id: Int): PhanCong? {
        return phanCongDao.getById(id)
    }


}