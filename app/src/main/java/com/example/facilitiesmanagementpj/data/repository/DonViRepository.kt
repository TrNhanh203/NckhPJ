package com.example.facilitiesmanagementpj.data.repository

import com.example.facilitiesmanagementpj.data.dao.*
import com.example.facilitiesmanagementpj.data.entity.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DonViRepository @Inject constructor(private val donViDao: DonViDao) {
    fun getAllDonVi(): Flow<List<DonVi>> = donViDao.getAll()

    suspend fun insert(donVi: DonVi) {
        donViDao.insert(donVi)
    }

    suspend fun update(donVi: DonVi) {
        donViDao.update(donVi)
    }

    suspend fun delete(donVi: DonVi) {
        donViDao.delete(donVi)
    }
}
