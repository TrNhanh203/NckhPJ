package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BienBanYeuCauRepository @Inject constructor(private val bienBanYeuCauDao: BienBanYeuCauDao) {
    fun getBienBanByYeuCau(yeuCauId: Int): Flow<List<BienBanYeuCau>> = bienBanYeuCauDao.getBienBanByYeuCau(yeuCauId)

    suspend fun insert(bienBanYeuCau: BienBanYeuCau) {
        bienBanYeuCauDao.insert(bienBanYeuCau)
    }

    suspend fun update(bienBanYeuCau: BienBanYeuCau) {
        bienBanYeuCauDao.update(bienBanYeuCau)
    }

    suspend fun delete(bienBanYeuCau: BienBanYeuCau) {
        bienBanYeuCauDao.delete(bienBanYeuCau)
    }
}
