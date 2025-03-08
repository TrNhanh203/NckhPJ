package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// 6. VaiTroRepository
@Singleton
class VaiTroRepository @Inject constructor(private val vaiTroDao: VaiTroDao) {
    fun getAllVaiTro(): Flow<List<VaiTro>> = vaiTroDao.getAll()
    suspend fun insert(vaiTro: VaiTro) = vaiTroDao.insert(vaiTro)
    suspend fun update(vaiTro: VaiTro) = vaiTroDao.update(vaiTro)
    suspend fun delete(vaiTro: VaiTro) = vaiTroDao.delete(vaiTro)
}