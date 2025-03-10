package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 5. PhongRepository
@Singleton
class PhongRepository @Inject constructor(private val phongDao: PhongDao) {
    fun getAllPhong(): Flow<List<Phong>> = phongDao.getAll()

    //fun getPhongTheoDay(): Flow<List<Phong>> = phongDao.getPhongTheoDay()
    suspend fun insert(phong: Phong) = phongDao.insert(phong)
    suspend fun update(phong: Phong) = phongDao.update(phong)
    suspend fun delete(phong: Phong) = phongDao.delete(phong)

    suspend fun capNhatDonViId(phongId: Int, donViId: Int) = phongDao.capNhatDonViId(phongId, donViId)

    suspend fun getAllPhongList(): List<Phong> {
        return phongDao.getAllList() // ✅ Trả về List<Phong>
    }

}